package org.lwjgl.openal;

/**
 * TeaVM-compatible stub for LWJGL's ALC11 class.
 * Extension functions for OpenAL 1.1 context management.
 */
public class ALC11 {

    public static void alcSetThreadContext(long context) {
        // No-op: Web Audio API doesn't use thread-local contexts
    }

    public static long alcGetThreadContext() {
        return 1L; // Return a non-zero context handle
    }

    /**
     * MC 26.1.2: Returns an ALC integer value for the given device and parameter.
     */
    public static int alcGetInteger(long device, int param) {
        return 0;
    }
}
