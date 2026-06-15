package net.lax1dude.eaglercraft.v2_6.internal.teavm.opts;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;

/**
 * Client configuration JSO interface for EaglerCraft 26.1.2.
 * Maps to the window.__eaglercraftXClientConfig object set by the bootstrap HTML.
 * Provides type-safe access to all configuration options.
 */
public interface IClientConfig extends JSObject {

        /**
         * Gets the name of the container element (default: "game_frame").
         * Used to locate the parent DOM element for the canvas.
         */
        @JSProperty
        String getContainer();

        @JSProperty
        void setContainer(String value);

        /**
         * Gets the name of the assets EPK file (default: "assets.epk").
         * This file is loaded at startup and contains all game assets.
         */
        @JSProperty
        String getEpkName();

        @JSProperty
        void setEpkName(String value);

        /**
         * Gets the download URL of the assets EPK file.
         * If null, defaults to the same directory as the HTML page.
         */
        @JSProperty
        String getEpkDownloadURL();

        @JSProperty
        void setEpkDownloadURL(String value);

        /**
         * Gets the list of servers to display in the server list.
         * Each element is an IClientConfigServer object.
         */
        @JSProperty
        JSArray<IClientConfigServer> getServers();

        @JSProperty
        void setServers(JSArray<IClientConfigServer> value);

        /**
         * Gets the worlds database name for IndexedDB (default: "EAGLERWORLDS").
         * Used to namespace world storage per-client-instance.
         */
        @JSProperty
        String getWorldsDB();

        @JSProperty
        void setWorldsDB(String value);

        /**
         * Gets the resource packs database name for IndexedDB (default: "EAGLERRESOURCEPACKS").
         * Used to namespace resource pack storage per-client-instance.
         */
        @JSProperty
        String getResourcePacksDB();

        @JSProperty
        void setResourcePacksDB(String value);

        /**
         * Gets the localStorage namespace prefix (default: "eaglercraft").
         * All localStorage keys are prefixed with this to avoid collisions.
         */
        @JSProperty
        String getLocalStorageNamespace();

        @JSProperty
        void setLocalStorageNamespace(String value);

        /**
         * Gets whether the client should attempt to auto-connect to the first server.
         * Default: false.
         */
        @JSProperty
        boolean isAutoConnect();

        @JSProperty
        void setAutoConnect(boolean value);

        /**
         * Gets the default server URI to auto-connect to.
         * Only used if autoConnect is true.
         */
        @JSProperty
        String getDefaultServerURI();

        @JSProperty
        void setDefaultServerURI(String value);

        /**
         * Gets whether the demo mode is enabled.
         * Demo mode restricts certain features.
         */
        @JSProperty
        boolean isDemoMode();

        @JSProperty
        void setDemoMode(boolean value);

        /**
         * Gets whether the client should use smooth lighting.
         * Default: true.
         */
        @JSProperty
        boolean isSmoothLighting();

        @JSProperty
        void setSmoothLighting(boolean value);

        /**
         * Gets the maximum render distance in chunks.
         * Default: 8.
         */
        @JSProperty
        int getRenderDistance();

        @JSProperty
        void setRenderDistance(int value);

        /**
         * Gets the default language code (e.g., "en_US").
         */
        @JSProperty
        String getLang();

        @JSProperty
        void setLang(String value);

        /**
         * Gets whether the crash screen should show full error details.
         * Default: true in debug, false in release.
         */
        @JSProperty
        boolean isVerboseCrashScreen();

        @JSProperty
        void setVerboseCrashScreen(boolean value);

        /**
         * Gets whether the F3 debug screen is enabled.
         * Default: true.
         */
        @JSProperty
        boolean isEnableF3Debug();

        @JSProperty
        void setEnableF3Debug(boolean value);

        /**
         * Gets the WebGL2 context attributes override.
         * If null, default context attributes are used.
         */
        @JSProperty
        IClientConfigGL getGLContextAttributes();

        @JSProperty
        void setGLContextAttributes(IClientConfigGL value);

        /**
         * Gets whether to enable the integrated server for singleplayer.
         * Default: true.
         */
        @JSProperty
        boolean isEnableIntegratedServer();

        @JSProperty
        void setEnableIntegratedServer(boolean value);

        /**
         * Gets whether to enable singleplayer command blocks.
         * Default: true.
         */
        @JSProperty
        boolean isEnableCommandBlocks();

        @JSProperty
        void setEnableCommandBlocks(boolean value);

        /**
         * Gets whether to enable multiplayer.
         * Default: true.
         */
        @JSProperty
        boolean isEnableMultiplayer();

        @JSProperty
        void setEnableMultiplayer(boolean value);

        /**
         * Gets whether to enable voice chat (WebRTC).
         * Default: false.
         */
        @JSProperty
        boolean isEnableVoiceChat();

        @JSProperty
        void setEnableVoiceChat(boolean value);

        /**
         * Gets whether to enable the shader editor.
         * Default: false.
         */
        @JSProperty
        boolean isEnableShaderEditor();

        @JSProperty
        void setEnableShaderEditor(boolean value);

        /**
         * Gets the force OpenGL version string override.
         * If null, the actual WebGL2 version is reported.
         */
        @JSProperty
        String getForceOpenGLVersion();

        @JSProperty
        void setForceOpenGLVersion(String value);

        /**
         * Gets the force OpenGL vendor string override.
         * If null, the actual WebGL2 vendor is reported.
         */
        @JSProperty
        String getForceOpenGLVendor();

        @JSProperty
        void setForceOpenGLVendor(String value);

        /**
         * Gets the force OpenGL renderer string override.
         * If null, the actual WebGL2 renderer is reported.
         */
        @JSProperty
        String getForceOpenGLRenderer();

        @JSProperty
        void setForceOpenGLRenderer(String value);

        /**
         * Gets whether to use the offscreen canvas for the integrated server.
         * Default: true if OffscreenCanvas is available.
         */
        @JSProperty
        boolean isUseOffscreenCanvas();

        @JSProperty
        void setUseOffscreenCanvas(boolean value);

        /**
         * Gets the audio sample rate preference.
         * Default: 48000.
         */
        @JSProperty
        int getAudioSampleRate();

        @JSProperty
        void setAudioSampleRate(int value);

        /**
         * Gets whether the game loop should use fixed timestep.
         * Default: false (variable timestep with vsync).
         */
        @JSProperty
        boolean isFixedTimeStep();

        @JSProperty
        void setFixedTimeStep(boolean value);

        /**
         * Gets the fixed timestep in milliseconds.
         * Default: 50 (20 TPS).
         */
        @JSProperty
        int getFixedTimeStepMs();

        @JSProperty
        void setFixedTimeStepMs(int value);
}
