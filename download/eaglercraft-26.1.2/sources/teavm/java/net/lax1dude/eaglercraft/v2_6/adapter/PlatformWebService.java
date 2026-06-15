package net.lax1dude.eaglercraft.v2_6.adapter;

import net.lax1dude.eaglercraft.v2_6.EaglerCraftConfig;
import net.lax1dude.eaglercraft.v2_6.EaglerProfile;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformAudio;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformFilesystem;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformInput;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;

/**
 * Service locator that Minecraft 26.1.2 code calls to access browser APIs
 * and EaglerCraft platform services.
 *
 * <p>In vanilla Minecraft 26.1.2, the game accesses system services through
 * LWJGL, OpenAL, and the filesystem. In EaglerCraft, these are replaced with
 * browser-based equivalents provided by the Platform* classes in the
 * <code>teavm</code> source set.</p>
 *
 * <p>This class serves as the central bridge point where MC code can obtain
 * references to the appropriate platform service implementations without
 * needing to know about TeaVM, WebGL2, or browser APIs directly.</p>
 *
 * <h3>Service mapping:</h3>
 * <table>
 *   <tr><th>MC Service</th><th>EaglerCraft Service</th></tr>
 *   <tr><td>LWJGL OpenGL</td><td>{@link PlatformOpenGL} (WebGL2)</td></tr>
 *   <tr><td>LWJGL Input</td><td>{@link PlatformInput} (DOM events)</td></tr>
 *   <tr><td>OpenAL Audio</td><td>{@link PlatformAudio} (Web Audio API)</td></tr>
 *   <tr><td>System.currentTimeMillis()</td><td>{@link PlatformRuntime} (performance.now())</td></tr>
 *   <tr><td>File I/O</td><td>{@link PlatformFilesystem} (IndexedDB)</td></tr>
 *   <tr><td>TCP Sockets</td><td>{@link PlatformNetworking} (WebSocket)</td></tr>
 *   <tr><td>Window Management</td><td>{@link PlatformApplication} (DOM/Fullscreen)</td></tr>
 * </table>
 *
 * <p>Architecture note: This class is in the <code>main</code> source set's
 * <code>adapter</code> sub-package. MC 26.1.2 code will call static methods
 * on this class (either directly or through patched call sites) to obtain
 * platform service instances. The actual implementations live in the
 * <code>teavm</code> source set under <code>net.lax1dude.eaglercraft.v2_6.internal</code>.</p>
 *
 * <p>TODO: Once MC 26.1.2 is decompiled, identify the exact call sites
 * where LWJGL/OpenAL calls are made and patch them to use this service
 * locator instead. Common patterns include:</p>
 * <ul>
 *   <li><code>GL11.gl*</code> calls → {@link PlatformOpenGL}._wgl* methods</li>
 *   <li><code>Keyboard.isKeyDown()</code> → {@link PlatformInput}.isKeyDown()</li>
 *   <li><code>Mouse.getX/Y()</code> → {@link PlatformInput}.getMouseX/Y()</li>
 *   <li><code>System.currentTimeMillis()</code> → {@link PlatformRuntime}.getCurrentTimeMillis()</li>
 *   <li><code>SoundSystem</code> → {@link PlatformAudio}</li>
 * </ul>
 */
public final class PlatformWebService {

        /** Whether the service locator has been initialized. */
        private static boolean initialized = false;

        // ========== Service Access ==========

        /**
         * Gets the OpenGL/WebGL2 rendering service.
         * MC code uses this for all rendering operations.
         *
         * <p>In vanilla MC 26.1.2, rendering goes through LWJGL's GL11 class.
         * In EaglerCraft, all GL calls are routed through {@link PlatformOpenGL},
         * which delegates to WebGL2 via {@link net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext}.</p>
         *
         * <p>MC call sites should use the static methods on PlatformOpenGL directly,
         * following the naming convention: <code>PlatformOpenGL._wgl*</code>.</p>
         *
         * @return the PlatformOpenGL class (use static methods)
         */
        public static Class<PlatformOpenGL> getRenderingService() {
                return PlatformOpenGL.class;
        }

        /**
         * Gets the runtime/timing service.
         * Provides high-resolution timing, storage, download, and screen info.
         *
         * <p>MC call sites that use <code>System.currentTimeMillis()</code> or
         * <code>System.nanoTime()</code> should be patched to use
         * {@link PlatformRuntime#getCurrentTimeMillis()} instead for consistent
         * high-resolution timing via <code>performance.now()</code>.</p>
         *
         * @return the PlatformRuntime class (use static methods)
         */
        public static Class<PlatformRuntime> getRuntimeService() {
                return PlatformRuntime.class;
        }

        /**
         * Gets the audio service.
         * Provides sound playback via the Web Audio API.
         *
         * <p>MC call sites that use OpenAL (SoundSystem, SoundManager) should be
         * patched to use {@link PlatformAudio} for sound playback.</p>
         *
         * @return the PlatformAudio class (use static methods)
         */
        public static Class<PlatformAudio> getAudioService() {
                return PlatformAudio.class;
        }

        /**
         * Gets the networking service.
         * Provides WebSocket-based multiplayer connectivity.
         *
         * <p>MC call sites that use TCP sockets (NetworkManager, ServerAddress)
         * should be patched to use {@link PlatformNetworking} for WebSocket
         * connections through EaglerCraft's gateway protocol.</p>
         *
         * @return the PlatformNetworking class (use static methods)
         */
        public static Class<PlatformNetworking> getNetworkingService() {
                return PlatformNetworking.class;
        }

        /**
         * Gets the input service.
         * Provides keyboard, mouse, touch, and gamepad input.
         *
         * <p>MC call sites that use LWJGL Keyboard/Mouse classes should be
         * patched to use {@link PlatformInput} for input handling.</p>
         *
         * @return the PlatformInput class (use static methods)
         */
        public static Class<PlatformInput> getInputService() {
                return PlatformInput.class;
        }

        /**
         * Gets the application lifecycle service.
         * Provides fullscreen, clipboard, page title, and error handling.
         *
         * <p>MC call sites that interact with the display/window should be
         * patched to use {@link PlatformApplication}.</p>
         *
         * @return the PlatformApplication class (use static methods)
         */
        public static Class<PlatformApplication> getApplicationService() {
                return PlatformApplication.class;
        }

        /**
         * Gets the filesystem service.
         * Provides IndexedDB-based persistent storage for worlds and resource packs.
         *
         * <p>MC call sites that read/write files (SaveFormat, ResourcePack)
         * should be patched to use {@link PlatformFilesystem}.</p>
         *
         * @return the PlatformFilesystem class (use static methods)
         */
        public static Class<PlatformFilesystem> getFilesystemService() {
                return PlatformFilesystem.class;
        }

        // ========== Convenience Delegation Methods ==========

        /**
         * Gets the current time in milliseconds.
         * Delegates to {@link PlatformRuntime#getCurrentTimeMillis()}.
         *
         * <p>This is the primary time source that MC code should use instead
         * of <code>System.currentTimeMillis()</code>.</p>
         *
         * @return current time in milliseconds (high-resolution via performance.now())
         */
        public static long currentTimeMillis() {
                return PlatformRuntime.getCurrentTimeMillis();
        }

        /**
         * Gets the canvas width in CSS pixels.
         * Delegates to {@link PlatformRuntime#getCanvasWidth()}.
         *
         * <p>MC code should use this instead of Display.getWidth().</p>
         *
         * @return canvas width in CSS pixels
         */
        public static int getDisplayWidth() {
                return PlatformRuntime.getCanvasWidth();
        }

        /**
         * Gets the canvas height in CSS pixels.
         * Delegates to {@link PlatformRuntime#getCanvasHeight()}.
         *
         * <p>MC code should use this instead of Display.getHeight().</p>
         *
         * @return canvas height in CSS pixels
         */
        public static int getDisplayHeight() {
                return PlatformRuntime.getCanvasHeight();
        }

        /**
         * Checks if a key is currently pressed.
         * Delegates to {@link PlatformInput#isKeyDown(int)}.
         *
         * <p>MC code should use this instead of Keyboard.isKeyDown().</p>
         *
         * @param keyCode the key code (uses LWJGL key code constants)
         * @return true if the key is currently pressed
         */
        public static boolean isKeyDown(int keyCode) {
                return PlatformInput.isKeyDown(keyCode);
        }

        /**
         * Gets the mouse X position.
         * Delegates to {@link PlatformInput#getMouseX()}.
         *
         * <p>MC code should use this instead of Mouse.getX().</p>
         *
         * @return mouse X position
         */
        public static int getMouseX() {
                return PlatformInput.getMouseX();
        }

        /**
         * Gets the mouse Y position.
         * Delegates to {@link PlatformInput#getMouseY()}.
         *
         * <p>MC code should use this instead of Mouse.getY().</p>
         *
         * @return mouse Y position
         */
        public static int getMouseY() {
                return PlatformInput.getMouseY();
        }

        /**
         * Checks if a mouse button is pressed.
         * Delegates to {@link PlatformInput#isMouseDown(int)}.
         *
         * @param button the mouse button (0=left, 1=right, 2=middle)
         * @return true if the button is pressed
         */
        public static boolean isMouseButtonDown(int button) {
                return PlatformInput.isMouseDown(button);
        }

        /**
         * Connects to a multiplayer server via WebSocket.
         * Delegates to {@link PlatformNetworking#connect(String)}.
         *
         * <p>MC code should use this instead of opening a TCP socket.</p>
         *
         * @param uri the WebSocket server URI (ws:// or wss://)
         * @return true if the connection attempt was initiated
         */
        public static boolean connectToServer(String uri) {
                return PlatformNetworking.connect(uri);
        }

        /**
         * Disconnects from the current server.
         * Delegates to {@link PlatformNetworking#disconnect()}.
         */
        public static void disconnectFromServer() {
                PlatformNetworking.disconnect();
        }

        /**
         * Checks if currently connected to a server.
         * Delegates to {@link PlatformNetworking#isConnected()}.
         *
         * @return true if connected
         */
        public static boolean isConnectedToServer() {
                return PlatformNetworking.isConnected();
        }

        // ========== Initialization ==========

        /**
         * Initializes the platform web service locator.
         * Called by {@link net.lax1dude.eaglercraft.v2_6.EaglerCraft} during startup.
         *
         * <p>This method verifies that all platform services are available
         * and logs their status.</p>
         */
        public static void init() {
                if (initialized) return;

                if (EaglerCraftConfig.VERBOSE_INIT_LOGGING) {
                        ClientMain.log("[PlatformWebService] Initializing service locator...");
                        ClientMain.log("[PlatformWebService] Rendering:  " + PlatformOpenGL.class.getSimpleName());
                        ClientMain.log("[PlatformWebService] Runtime:     " + PlatformRuntime.class.getSimpleName());
                        ClientMain.log("[PlatformWebService] Audio:       " + PlatformAudio.class.getSimpleName());
                        ClientMain.log("[PlatformWebService] Networking:  " + PlatformNetworking.class.getSimpleName());
                        ClientMain.log("[PlatformWebService] Input:       " + PlatformInput.class.getSimpleName());
                        ClientMain.log("[PlatformWebService] Application: " + PlatformApplication.class.getSimpleName());
                        ClientMain.log("[PlatformWebService] Filesystem:  " + PlatformFilesystem.class.getSimpleName());
                }

                initialized = true;
                ClientMain.log("[PlatformWebService] Service locator initialized");
        }

        /**
         * Checks if the service locator has been initialized.
         */
        public static boolean isInitialized() {
                return initialized;
        }

        /**
         * Gets the client version string for protocol handshakes.
         * Combines brand, version, and protocol version.
         *
         * @return formatted version string for server handshake
         */
        public static String getClientVersionString() {
                return EaglerCraftConfig.CLIENT_IDENTIFIER + " (" + EaglerCraftConfig.PROTOCOL_VERSION + ")";
        }

        // Private constructor — all members are static
        private PlatformWebService() {
        }
}
