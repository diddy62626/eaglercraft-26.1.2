package net.lax1dude.eaglercraft.v2_6.internal.sp;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

/**
 * Base Web Worker abstraction for EaglerCraft 26.1.2.
 *
 * <p>Wraps the browser's DedicatedWorkerGlobalScope and provides
 * a clean Java API for Web Worker operations including message
 * port handling, ArrayBuffer transfer, and error propagation
 * to the main thread.</p>
 *
 * <h3>Usage from Main Thread (Client)</h3>
 * <pre>
 *   // Create a worker from a blob URL
 *   EaglerWorker worker = EaglerWorker.create(blobURL);
 *
 *   // Send data with transfer
 *   worker.postMessage(dataBuffer);
 *
 *   // Receive data
 *   worker.setOnMessage(handler);
 *
 *   // Terminate when done
 *   worker.terminate();
 * </pre>
 *
 * <h3>Usage from Worker Thread (Server)</h3>
 * <pre>
 *   // Inside the worker context
 *   EaglerWorker.sendToMain(dataBuffer);
 *
 *   // Listen for messages from main
 *   EaglerWorker.onMessageFromMain(handler);
 * </pre>
 *
 * <h3>ArrayBuffer Transfer</h3>
 * <p>When posting messages to/from workers, ArrayBuffers can be
 * <i>transferred</i> rather than cloned. Transfer is zero-copy: the
 * ArrayBuffer becomes unusable in the sender's context but is
 * immediately available in the receiver's context without a copy.
 * This is critical for large chunk data (up to 128KB per chunk).</p>
 *
 * <h3>SharedArrayBuffer</h3>
 * <p>When the hosting page provides the proper COOP/COEP headers,
 * SharedArrayBuffer can be used for true shared memory between
 * the main thread and the worker, enabling lock-free ring buffers
 * for high-throughput data exchange.</p>
 *
 * @author Eaglercraft Team
 * @version 2.6
 * @since 2.6
 */
public class EaglerWorker {

        // ========== JS Functor Interfaces ==========

        /**
         * Callback for receiving messages from the worker.
         */
        @JSFunctor
        public interface MessageHandler extends JSObject {
                void onMessage(ArrayBuffer data);
        }

        /**
         * Callback for receiving error events from the worker.
         */
        @JSFunctor
        public interface ErrorHandler extends JSObject {
                void onError(String message, String filename, int lineno);
        }

        /**
         * Callback for receiving messages inside the worker (from main thread).
         */
        @JSFunctor
        public interface WorkerMessageHandler extends JSObject {
                void onMessage(ArrayBuffer data);
        }

        /**
         * Callback for error events inside the worker.
         */
        @JSFunctor
        public interface WorkerErrorHandler extends JSObject {
                void onError(String message, String filename, int lineno, int colno);
        }

        // ========== Worker Handle (Main Thread) ==========

        /** The underlying Worker JS object. */
        private JSObject workerHandle;

        /** Whether this worker is still active. */
        private boolean active;

        /**
         * Private constructor. Use {@link #create(String)} to instantiate.
         */
        private EaglerWorker(JSObject handle) {
                this.workerHandle = handle;
                this.active = true;
        }

        /**
         * Creates a new Web Worker from the given URL.
         *
         * @param workerURL the URL of the worker script (blob URL or same-origin path)
         * @return a new EaglerWorker instance, or null if creation failed
         */
        public static EaglerWorker create(String workerURL) {
                try {
                        JSObject handle = createWorker0(workerURL);
                        if (handle != null) {
                                return new EaglerWorker(handle);
                        }
                } catch (Exception e) {
                        reportErrorToMain0("Failed to create worker: " + e.getMessage());
                }
                return null;
        }

        /**
         * Posts an ArrayBuffer to the worker with transfer semantics.
         * The buffer becomes unusable in this thread after the call.
         *
         * @param data the ArrayBuffer to send (will be transferred, not copied)
         */
        public void postMessage(ArrayBuffer data) {
                if (!active || workerHandle == null) return;
                postMessage0(workerHandle, data);
        }

        /**
         * Posts a raw byte array to the worker (copies into a new ArrayBuffer).
         *
         * @param data the byte array to send
         */
        public void postBytes(byte[] data) {
                if (!active || workerHandle == null) return;
                ArrayBuffer buffer = bytesToArrayBuffer(data);
                postMessage0(workerHandle, buffer);
        }

        /**
         * Sets the message handler for receiving data from the worker.
         *
         * @param handler the handler to call when a message arrives
         */
        public void setOnMessage(MessageHandler handler) {
                if (!active || workerHandle == null) return;
                setOnMessage0(workerHandle, handler);
        }

        /**
         * Sets the error handler for the worker.
         *
         * @param handler the handler to call when an error occurs
         */
        public void setOnError(ErrorHandler handler) {
                if (!active || workerHandle == null) return;
                setOnError0(workerHandle, handler);
        }

        /**
         * Terminates the worker immediately.
         * The worker cannot be restarted after termination.
         */
        public void terminate() {
                if (!active) return;
                if (workerHandle != null) {
                        terminateWorker0(workerHandle);
                }
                active = false;
                workerHandle = null;
        }

        /**
         * Checks if this worker is still active.
         */
        public boolean isActive() {
                return active;
        }

        // ========== Worker Context (Inside Worker Thread) ==========

        /**
         * Sends an ArrayBuffer to the main thread with transfer semantics.
         * Called from within the worker context.
         *
         * @param data the ArrayBuffer to send
         */
        public static void sendToMain(ArrayBuffer data) {
                sendToMain0(data);
        }

        /**
         * Sends a raw byte array to the main thread.
         *
         * @param data the byte array to send
         */
        public static void sendBytesToMain(byte[] data) {
                ArrayBuffer buffer = bytesToArrayBuffer(data);
                sendToMain0(buffer);
        }

        /**
         * Registers a message handler inside the worker for messages
         * from the main thread.
         *
         * @param handler the handler to call when a message arrives
         */
        public static void onMessageFromMain(WorkerMessageHandler handler) {
                registerWorkerOnMessage0(handler);
        }

        /**
         * Registers an error handler inside the worker.
         *
         * @param handler the handler to call when an error occurs
         */
        public static void onWorkerError(WorkerErrorHandler handler) {
                registerWorkerOnError0(handler);
        }

        /**
         * Reports an error to the main thread by posting an error message.
         * Can be called from either the main thread or the worker.
         *
         * @param message the error message
         */
        public static void reportError(String message) {
                reportErrorToMain0(message);
        }

        // ========== SharedArrayBuffer Support ==========

        /**
         * Checks if SharedArrayBuffer is available.
         * Requires COOP/COEP headers on the hosting page.
         *
         * @return true if SharedArrayBuffer can be created
         */
        public static boolean isSharedArrayBufferAvailable() {
                return probeSharedArrayBuffer0();
        }

        /**
         * Creates a SharedArrayBuffer of the specified size.
         *
         * @param size the buffer size in bytes
         * @return the SharedArrayBuffer, or null if not supported
         */
        public static JSObject createSharedBuffer(int size) {
                if (!probeSharedArrayBuffer0()) return null;
                return createSharedBuffer0(size);
        }

        /**
         * Gets a typed array view of a SharedArrayBuffer.
         *
         * @param sharedBuffer the SharedArrayBuffer
         * @return an Int32Array view of the buffer
         */
        public static JSObject getSharedInt32View(JSObject sharedBuffer) {
                return createInt32View0(sharedBuffer);
        }

        // ========== Conversion Utilities ==========

        /**
         * Converts a Java byte array to a JavaScript ArrayBuffer.
         */
        static ArrayBuffer bytesToArrayBuffer(byte[] arr) {
                ArrayBuffer buffer = ArrayBuffer.create(arr.length);
                Uint8Array view = Uint8Array.create(buffer);
                for (int i = 0; i < arr.length; i++) {
                        view.set(i, (short)(arr[i] & 0xFF));
                }
                return buffer;
        }

        /**
         * Converts a JavaScript ArrayBuffer to a Java byte array.
         */
        static byte[] arrayBufferToBytes(ArrayBuffer buffer) {
                Uint8Array view = Uint8Array.create(buffer);
                byte[] result = new byte[view.getLength()];
                for (int i = 0; i < result.length; i++) {
                        result[i] = (byte) view.get(i);
                }
                return result;
        }

        // ========== Native JS Methods (Main Thread) ==========

        @JSBody(params = { "url" }, script = ""
                        + "try {"
                        + "  return new Worker(url);"
                        + "} catch(e) {"
                        + "  console.error('EaglerWorker: create failed:', e);"
                        + "  return null;"
                        + "}")
        private static native JSObject createWorker0(String url);

        @JSBody(params = { "worker", "data" }, script = ""
                        + "worker.postMessage(data, [data]);")
        private static native void postMessage0(JSObject worker, ArrayBuffer data);

        @JSBody(params = { "worker", "handler" }, script = ""
                        + "worker.onmessage = function(e) {"
                        + "  if (e.data instanceof ArrayBuffer) {"
                        + "    handler.onMessage(e.data);"
                        + "  }"
                        + "};")
        private static native void setOnMessage0(JSObject worker, MessageHandler handler);

        @JSBody(params = { "worker", "handler" }, script = ""
                        + "worker.onerror = function(e) {"
                        + "  handler.onError(e.message || 'Unknown worker error', e.filename || '', e.lineno || 0);"
                        + "};")
        private static native void setOnError0(JSObject worker, ErrorHandler handler);

        @JSBody(params = { "worker" }, script = "worker.terminate();")
        private static native void terminateWorker0(JSObject worker);

        // ========== Native JS Methods (Worker Context) ==========

        @JSBody(params = { "data" }, script = ""
                        + "self.postMessage(data, [data]);")
        private static native void sendToMain0(ArrayBuffer data);

        @JSBody(params = { "handler" }, script = ""
                        + "self.onmessage = function(e) {"
                        + "  if (e.data instanceof ArrayBuffer) {"
                        + "    handler.onMessage(e.data);"
                        + "  }"
                        + "};")
        private static native void registerWorkerOnMessage0(WorkerMessageHandler handler);

        @JSBody(params = { "handler" }, script = ""
                        + "self.onerror = function(e) {"
                        + "  handler.onError(e.message || '', e.filename || '', e.lineno || 0, e.colno || 0);"
                        + "};")
        private static native void registerWorkerOnError0(WorkerErrorHandler handler);

        @JSBody(params = { "message" }, script = ""
                        + "try {"
                        + "  var errorData = new TextEncoder().encode('\\xFF' + message);"
                        + "  self.postMessage(errorData.buffer, [errorData.buffer]);"
                        + "} catch(e) {"
                        + "  console.error('EaglerWorker: reportError failed:', e);"
                        + "}")
        private static native void reportErrorToMain0(String message);

        // ========== SharedArrayBuffer Native Methods ==========

        @JSBody(params = {}, script = "return (typeof SharedArrayBuffer !== 'undefined');")
        private static native boolean probeSharedArrayBuffer0();

        @JSBody(params = { "size" }, script = ""
                        + "try {"
                        + "  return new SharedArrayBuffer(size);"
                        + "} catch(e) {"
                        + "  return null;"
                        + "}")
        private static native JSObject createSharedBuffer0(int size);

        @JSBody(params = { "buffer" }, script = "return new Int32Array(buffer);")
        private static native JSObject createInt32View0(JSObject buffer);
}
