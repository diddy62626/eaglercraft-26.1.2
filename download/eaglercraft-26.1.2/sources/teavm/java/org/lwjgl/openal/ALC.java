package org.lwjgl.openal;

/**
 * TeaVM-compatible stub for LWJGL's ALC class.
 * OpenAL context management is handled by PlatformAudio using Web Audio API.
 */
public class ALC {

    private static ALCCapabilities capabilities;

    public static long create() {
        return 1L; // Return a non-zero device handle
    }

    public static long openDevice(String deviceName) {
        return 1L; // Return a non-zero device handle
    }

    public static boolean closeDevice(long device) {
        return true;
    }

    public static long createContext(long device) {
        return 1L;
    }

    public static long createContext(long device, int[] attrs) {
        return 1L;
    }

    public static boolean makeContextCurrent(long context) {
        return true;
    }

    public static void destroyContext(long context) {
        // No-op
    }

    public static ALCCapabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = new ALCCapabilities();
        }
        return capabilities;
    }

    public static ALCCapabilities createCapabilities(long device) {
        return new ALCCapabilities();
    }

    public static boolean isCreated() {
        return true;
    }

    public static String getString(long device, int param) {
        return "";
    }
}
