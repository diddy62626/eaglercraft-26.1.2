package org.lwjgl.openal;

public class ALC10 {
    // ========== Constants ==========
    public static final int ALC_DEFAULT_DEVICE_SPECIFIER = 0x1004;
    public static final int ALC_DEVICE_SPECIFIER = 0x1005;
    public static final int ALC_NO_ERROR = 0;
    public static final int ALC_INVALID_DEVICE = 0xA001;
    public static final int ALC_INVALID_CONTEXT = 0xA002;
    public static final int ALC_INVALID_ENUM = 0xA003;
    public static final int ALC_INVALID_VALUE = 0xA004;
    public static final int ALC_OUT_OF_MEMORY = 0xA005;
    public static final int ALC_FREQUENCY = 0x1007;
    public static final int ALC_REFRESH = 0x1008;
    public static final int ALC_SYNC = 0x1009;
    public static final int ALC_MONO_SOURCES = 0x1010;
    public static final int ALC_STEREO_SOURCES = 0x1011;
    public static final int ALC_EXTENSIONS = 0x1006;
    public static final int ALC_MAJOR_VERSION = 0x1000;
    public static final int ALC_MINOR_VERSION = 0x1001;
    public static final int ALC_ATTRIBUTES_SIZE = 0x1002;
    public static final int ALC_ALL_ATTRIBUTES = 0x1003;

    // ========== Device management ==========

    public static long alcOpenDevice(String devicename) {
        return 1L;
    }

    public static boolean alcCloseDevice(long device) {
        return true;
    }

    // ========== Context management ==========

    public static long alcCreateContext(long device, int[] attrList) {
        return 1L;
    }

    public static boolean alcMakeContextCurrent(long context) {
        return true;
    }

    public static void alcDestroyContext(long context) {
        // no-op
    }

    // ========== Error/query ==========

    public static int alcGetError(long device) {
        return ALC_NO_ERROR;
    }

    public static String alcGetString(long device, int param) {
        return "";
    }

    public static void alcGetIntegerv(long device, int param, int size, int[] values, int offset) {
        for (int i = 0; i < size; i++) {
            values[offset + i] = 0;
        }
    }

    public static long alcGetCurrentContext() {
        return 1L;
    }

    public static long alcGetContextsDevice(long context) {
        return 1L;
    }

    public static boolean alcIsExtensionPresent(long device, String extName) {
        return false;
    }

    public static int alcGetEnumValue(long device, String enumName) {
        return 0;
    }

    // ========== Convenience overloads ==========

    public static long alcOpenDevice() {
        return alcOpenDevice(null);
    }

    public static long alcCreateContext(long device) {
        return alcCreateContext(device, (int[]) null);
    }

    public static void alcGetIntegerv(long device, int param, int size, int[] values) {
        alcGetIntegerv(device, param, size, values, 0);
    }

    /**
     * MC 26.1.2: Creates an ALC context with optional attributes.
     */
    public static long alcCreateContext(long device, java.nio.IntBuffer attrlist) {
        // Browser: Web Audio API doesn't use ALC contexts; return a non-zero handle.
        return 1L;
    }

    /**
     * MC 26.1.2: Returns whether an ALC extension is present.
     */
    public static boolean alcIsExtensionPresent(long device, CharSequence extname) {
        return false;
    }

    /**
     * MC 26.1.2: Opens an ALC device by name.
     */
    public static long alcOpenDevice(CharSequence devicename) {
        return 1L;
    }

    public static int alcGetInteger(long device, int param) { return 0; }
    public static void alcGetIntegerv(long device, int param, java.nio.IntBuffer values) {
        if (values != null && values.remaining() > 0) values.put(0, 0);
    }
}
