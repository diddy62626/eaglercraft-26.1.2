package net.lax1dude.eaglercraft.v2_6.internal.sp;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

/**
 * Web Worker entry point for the EaglerCraft 26.1.2 integrated server.
 *
 * <p>This class runs inside a dedicated Web Worker thread, providing a
 * separate execution context for the Minecraft integrated server. The
 * server loop handles world simulation (chunk generation, entity ticking,
 * block ticking, player management) while the main thread focuses on
 * rendering and input.</p>
 *
 * <h3>Architecture</h3>
 * <pre>
 *   Main Thread (Client)          Worker Thread (Server)
 *   ┌──────────────────┐          ┌──────────────────────┐
 *   │  Rendering        │  IPC     │  World Simulation    │
 *   │  Input Handling   │◄───────►│  Chunk Generation    │
 *   │  Audio            │  Array   │  Entity Ticking      │
 *   │  UI               │ Transfer │  Block Ticking       │
 *   │  Network (WS)     │          │  Player Management   │
 *   └──────────────────┘          └──────────────────────┘
 * </pre>
 *
 * <h3>IPC Message Protocol</h3>
 * <p>Messages are exchanged as ArrayBuffer transfers for zero-copy:</p>
 * <ul>
 *   <li>Client -> Worker: Player input, chunk requests, block changes</li>
 *   <li>Worker -> Client: Chunk data, entity positions, render commands</li>
 * </ul>
 *
 * <h3>SharedArrayBuffer Support</h3>
 * <p>When available, SharedArrayBuffer is used for fast data sharing
 * between threads without transfer overhead. This requires proper
 * COOP/COEP headers on the hosting page.</p>
 *
 * @author Eaglercraft Team
 * @version 2.6
 * @since 2.6
 */
public class WorkerMain {

        /** The integrated server instance. */
        private static IntegratedServer server;

        /** Whether the worker is currently running. */
        private static boolean running = false;

        /** Whether SharedArrayBuffer is available for IPC. */
        private static boolean sharedArrayBufferAvailable = false;

        /** Shared memory region for fast IPC (null if not available). */
        private static JSObject sharedBuffer;

        /** The size of the shared buffer in bytes. */
        private static final int SHARED_BUFFER_SIZE = 16 * 1024 * 1024; // 16MB

        /** IPC channel constants. */
        public static final int IPC_CHANNEL_PLAYER_INPUT = 0x01;
        public static final int IPC_CHANNEL_CHUNK_REQUEST = 0x02;
        public static final int IPC_CHANNEL_BLOCK_CHANGE = 0x03;
        public static final int IPC_CHANNEL_CHAT_MESSAGE = 0x04;
        public static final int IPC_CHANNEL_PLAYER_JOIN = 0x05;
        public static final int IPC_CHANNEL_PLAYER_LEAVE = 0x06;
        public static final int IPC_CHANNEL_SAVE_WORLD = 0x07;
        public static final int IPC_CHANNEL_LOAD_WORLD = 0x08;
        public static final int IPC_CHANNEL_PAUSE = 0x09;
        public static final int IPC_CHANNEL_RESUME = 0x0A;
        public static final int IPC_CHANNEL_SHUTDOWN = 0x0B;
        public static final int IPC_CHANNEL_INIT = 0x0C;

        public static final int IPC_CHANNEL_CHUNK_DATA = 0x81;
        public static final int IPC_CHANNEL_ENTITY_UPDATE = 0x82;
        public static final int IPC_CHANNEL_RENDER_COMMAND = 0x83;
        public static final int IPC_CHANNEL_PLAYER_SPAWN = 0x84;
        public static final int IPC_CHANNEL_TIME_UPDATE = 0x85;
        public static final int IPC_CHANNEL_WORLD_SAVE_DONE = 0x86;
        public static final int IPC_CHANNEL_WORLD_LOAD_DONE = 0x87;
        public static final int IPC_CHANNEL_SERVER_STATUS = 0x88;
        public static final int IPC_CHANNEL_ERROR = 0xFF;

        /** Target tick rate (20 TPS). */
        private static final int TARGET_TPS = 20;
        private static final long TICK_TIME_MS = 1000 / TARGET_TPS;

        /** Tick timing accumulator. */
        private static long tickAccumulator = 0;
        private static long lastTickTime = 0;

        /**
         * Worker entry point. Called by the TeaVM bootstrap when the
         * Web Worker script is loaded.
         */
        public static void main(String[] args) {
                // Probe SharedArrayBuffer support
                sharedArrayBufferAvailable = probeSharedArrayBuffer0();
                if (sharedArrayBufferAvailable) {
                        sharedBuffer = createSharedBuffer0(SHARED_BUFFER_SIZE);
                }

                // Register message handler for IPC from main thread
                registerMessageHandler0(data -> __onIPCMessage(data));

                // Send ready signal
                sendStatusMessage("WORKER_READY", sharedArrayBufferAvailable ? 1 : 0);

                running = true;
                lastTickTime = getCurrentTimeMillis0();

                // Main server loop
                while (running) {
                        long now = getCurrentTimeMillis0();
                        long delta = now - lastTickTime;
                        lastTickTime = now;

                        tickAccumulator += delta;

                        // Fixed timestep tick
                        int ticksThisFrame = 0;
                        while (tickAccumulator >= TICK_TIME_MS && ticksThisFrame < 4) {
                                if (server != null) {
                                        try {
                                                server.tick();
                                        } catch (Exception e) {
                                                sendError("Server tick error: " + e.getMessage());
                                        }
                                }
                                tickAccumulator -= TICK_TIME_MS;
                                ticksThisFrame++;
                        }

                        // Prevent spiral of death
                        if (tickAccumulator > TICK_TIME_MS * 4) {
                                tickAccumulator = 0;
                        }

                        // Yield to the browser event loop
                        sleep0(1);
                }

                // Cleanup
                if (server != null) {
                        server.shutdown();
                        server = null;
                }
        }

        /**
         * Called from JS when an IPC message arrives from the main thread.
         */
        private static void __onIPCMessage(ArrayBuffer data) {
                if (data == null) return;
                byte[] bytes = arrayBufferToBytes(data);
                if (bytes.length < 1) return;

                int channel = bytes[0] & 0xFF;
                byte[] payload = new byte[bytes.length - 1];
                if (payload.length > 0) {
                        System.arraycopy(bytes, 0, payload, 0, payload.length);
                        // Shift by 1 to skip channel byte
                        for (int i = 0; i < payload.length; i++) {
                                payload[i] = bytes[i + 1];
                        }
                }

                try {
                        handleIPCMessage(channel, payload);
                } catch (Exception e) {
                        sendError("IPC handler error (channel 0x" + Integer.toHexString(channel) + "): " + e.getMessage());
                }
        }

        /**
         * Dispatches incoming IPC messages to the appropriate handler.
         */
        private static void handleIPCMessage(int channel, byte[] payload) {
                switch (channel) {
                        case IPC_CHANNEL_INIT:
                                handleInit(payload);
                                break;
                        case IPC_CHANNEL_PLAYER_INPUT:
                                if (server != null) server.handlePlayerInput(payload);
                                break;
                        case IPC_CHANNEL_CHUNK_REQUEST:
                                if (server != null) server.handleChunkRequest(payload);
                                break;
                        case IPC_CHANNEL_BLOCK_CHANGE:
                                if (server != null) server.handleBlockChange(payload);
                                break;
                        case IPC_CHANNEL_CHAT_MESSAGE:
                                if (server != null) server.handleChatMessage(payload);
                                break;
                        case IPC_CHANNEL_PLAYER_JOIN:
                                handlePlayerJoin(payload);
                                break;
                        case IPC_CHANNEL_PLAYER_LEAVE:
                                if (server != null) server.handlePlayerLeave(payload);
                                break;
                        case IPC_CHANNEL_SAVE_WORLD:
                                if (server != null) server.handleSaveWorld(payload);
                                break;
                        case IPC_CHANNEL_LOAD_WORLD:
                                if (server != null) server.handleLoadWorld(payload);
                                break;
                        case IPC_CHANNEL_PAUSE:
                                if (server != null) server.setPaused(true);
                                break;
                        case IPC_CHANNEL_RESUME:
                                if (server != null) server.setPaused(false);
                                break;
                        case IPC_CHANNEL_SHUTDOWN:
                                running = false;
                                break;
                        default:
                                break;
                }
        }

        /**
         * Handles the initialization message from the client.
         */
        private static void handleInit(byte[] payload) {
                if (server != null) {
                        server.shutdown();
                }
                server = new IntegratedServer();
                boolean success = server.initialize(payload);
                if (success) {
                        sendStatusMessage("SERVER_INITIALIZED", 0);
                } else {
                        sendError("Failed to initialize integrated server");
                }
        }

        /**
         * Handles a player join request.
         */
        private static void handlePlayerJoin(byte[] payload) {
                if (server == null) return;
                String playerName = new String(payload);
                server.handlePlayerJoin(playerName);
        }

        /**
         * Sends an IPC message to the main thread.
         */
        public static void sendIPCMessage(int channel, byte[] payload) {
                byte[] message = new byte[1 + (payload != null ? payload.length : 0)];
                message[0] = (byte) channel;
                if (payload != null && payload.length > 0) {
                        System.arraycopy(payload, 0, message, 1, payload.length);
                }
                ArrayBuffer buffer = bytesToArrayBuffer(message);
                postMessageToMain0(buffer);
        }

        /**
         * Sends a status message to the main thread.
         */
        public static void sendStatusMessage(String status, int code) {
                String msg = status + ":" + code;
                byte[] bytes = msg.getBytes();
                sendIPCMessage(IPC_CHANNEL_SERVER_STATUS, bytes);
        }

        /**
         * Sends an error message to the main thread.
         */
        public static void sendError(String error) {
                byte[] bytes = error.getBytes();
                sendIPCMessage(IPC_CHANNEL_ERROR, bytes);
        }

        // ========== Conversion Utilities ==========

        private static byte[] arrayBufferToBytes(ArrayBuffer buffer) {
                Uint8Array view = Uint8Array.create(buffer);
                byte[] result = new byte[view.getLength()];
                for (int i = 0; i < result.length; i++) {
                        result[i] = (byte) view.get(i);
                }
                return result;
        }

        private static ArrayBuffer bytesToArrayBuffer(byte[] arr) {
                ArrayBuffer buffer = ArrayBuffer.create(arr.length);
                Uint8Array view = Uint8Array.create(buffer);
                for (int i = 0; i < arr.length; i++) {
                        view.set(i, (short)(arr[i] & 0xFF));
                }
                return buffer;
        }

        // ========== Native JS Methods ==========

        @JSBody(params = {}, script = "return (typeof SharedArrayBuffer !== 'undefined');")
        private static native boolean probeSharedArrayBuffer0();

        @JSBody(params = { "size" }, script = "return new SharedArrayBuffer(size);")
        private static native JSObject createSharedBuffer0(int size);

        @JSFunctor
        private interface IPCMessageCallback extends JSObject {
                void call(ArrayBuffer data);
        }

        @JSBody(params = { "callback" }, script = ""
                        + "self.onmessage = function(e) {"
                        + "  if (e.data instanceof ArrayBuffer) {"
                        + "    callback(e.data);"
                        + "  }"
                        + "};")
        private static native void registerMessageHandler0(IPCMessageCallback callback);

        @JSBody(params = { "data" }, script = "self.postMessage(data, [data]);")
        private static native void postMessageToMain0(ArrayBuffer data);

        @JSBody(params = {}, script = ""
                        + "if (self.performance && self.performance.now) {"
                        + "  return BigInt(Math.floor(self.performance.now()));"
                        + "} else {"
                        + "  return BigInt(Date.now());"
                        + "}")
        private static native long getCurrentTimeMillis0();

        @JSBody(params = { "ms" }, script = ""
                        + "var end = Date.now() + ms;"
                        + "while (Date.now() < end) { /* busy wait for short sleeps */ }")
        private static native void sleep0(int ms);
}
