package org.lwjgl.openal;

/**
 * TeaVM-compatible stub for LWJGL's AL class.
 * OpenAL initialization is handled by PlatformAudio using Web Audio API.
 */
public class AL {

    private static ALCapabilities capabilities;

    public static void create() {
        // No-op: Web Audio API doesn't need explicit creation
    }

    public static void create(ALCCapabilities alcc, int... attrs) {
        // No-op
    }

    public static void destroy() {
        // No-op: Web Audio API cleanup handled by PlatformAudio
    }

    public static ALCapabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = new ALCapabilities();
        }
        return capabilities;
    }

    public static boolean isCreated() {
        return true;
    }

    /**
     * MC 26.1.2: Creates ALCapabilities from an ALCCapabilities (device).
     */
    public static ALCapabilities createCapabilities(ALCCapabilities caps) {
        if (caps == null) return new ALCapabilities();
        return new ALCapabilities(caps.device);
    }
}
