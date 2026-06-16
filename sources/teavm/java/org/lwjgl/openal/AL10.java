package org.lwjgl.openal;

public class AL10 {
    // ========== Constants ==========
    public static final int AL_NONE = 0;
    public static final int AL_FALSE = 0;
    public static final int AL_TRUE = 1;

    // Source properties
    public static final int AL_SOURCE_RELATIVE = 0x202;
    public static final int AL_CONE_INNER_ANGLE = 0x1001;
    public static final int AL_CONE_OUTER_ANGLE = 0x1002;
    public static final int AL_PITCH = 0x1003;
    public static final int AL_POSITION = 0x1004;
    public static final int AL_DIRECTION = 0x1005;
    public static final int AL_VELOCITY = 0x1006;
    public static final int AL_LOOPING = 0x1007;
    public static final int AL_BUFFER = 0x1009;
    public static final int AL_GAIN = 0x100A;
    public static final int AL_MIN_GAIN = 0x100D;
    public static final int AL_MAX_GAIN = 0x100E;
    public static final int AL_ORIENTATION = 0x100F;

    // Source state
    public static final int AL_SOURCE_STATE = 0x1010;
    public static final int AL_INITIAL = 0x1011;
    public static final int AL_PLAYING = 0x1012;
    public static final int AL_PAUSED = 0x1013;
    public static final int AL_STOPPED = 0x1014;

    // Buffer queries
    public static final int AL_BUFFERS_PROCESSED = 0x1016;
    public static final int AL_BUFFERS_QUEUED = 0x1015;

    // Offset
    public static final int AL_SEC_OFFSET = 0x1024;
    public static final int AL_SAMPLE_OFFSET = 0x1025;
    public static final int AL_BYTE_OFFSET = 0x1026;

    // Format
    public static final int AL_FORMAT_MONO8 = 0x1100;
    public static final int AL_FORMAT_MONO16 = 0x1101;
    public static final int AL_FORMAT_STEREO8 = 0x1102;
    public static final int AL_FORMAT_STEREO16 = 0x1103;

    // Buffer properties
    public static final int AL_FREQUENCY = 0x2001;
    public static final int AL_BITS = 0x2002;
    public static final int AL_CHANNELS = 0x2003;
    public static final int AL_SIZE = 0x2004;

    // Errors
    public static final int AL_INVALID_NAME = 0xA001;
    public static final int AL_INVALID_ENUM = 0xA002;
    public static final int AL_INVALID_VALUE = 0xA003;
    public static final int AL_INVALID_OPERATION = 0xA004;
    public static final int AL_OUT_OF_MEMORY = 0xA005;
    public static final int AL_NO_ERROR = 0;

    // Distance model
    public static final int AL_INVERSE_DISTANCE = 0xD001;
    public static final int AL_INVERSE_DISTANCE_CLAMPED = 0xD002;
    public static final int AL_LINEAR_DISTANCE = 0xD003;
    public static final int AL_LINEAR_DISTANCE_CLAMPED = 0xD004;
    public static final int AL_EXPONENT_DISTANCE = 0xD005;
    public static final int AL_EXPONENT_DISTANCE_CLAMPED = 0xD006;

    // Dummy ID counter for generated sources/buffers
    private static int nextId = 1;

    // ========== Source management ==========

    public static void alGenSources(int n, int[] sources, int offset) {
        for (int i = 0; i < n; i++) {
            sources[offset + i] = nextId++;
        }
    }

    public static int alGenSources() {
        return nextId++;
    }

    public static void alDeleteSources(int n, int[] sources, int offset) {
        // no-op
    }

    public static void alDeleteSources(int source) {
        // no-op
    }

    public static void alSourcePlay(int source) {
        // no-op - handled by PlatformAudio
    }

    public static void alSourcePause(int source) {
        // no-op
    }

    public static void alSourceStop(int source) {
        // no-op
    }

    public static void alSourceRewind(int source) {
        // no-op
    }

    // ========== Source parameters ==========

    public static void alSourcei(int source, int param, int value) {
        // no-op
    }

    public static void alSourcef(int source, int param, float value) {
        // no-op
    }

    public static void alSource3f(int source, int param, float v1, float v2, float v3) {
        // no-op
    }

    public static void alSourcefv(int source, int param, float[] values) {
        // no-op
    }

    // ========== Source queries ==========

    public static void alGetSourcei(int source, int param, int[] value, int offset) {
        if (param == AL_SOURCE_STATE) {
            value[offset] = AL_STOPPED;
        } else if (param == AL_BUFFERS_PROCESSED) {
            value[offset] = 0;
        } else if (param == AL_BUFFERS_QUEUED) {
            value[offset] = 0;
        } else {
            value[offset] = 0;
        }
    }

    public static int alGetSourcei(int source, int param) {
        if (param == AL_SOURCE_STATE) {
            return AL_STOPPED;
        }
        return 0;
    }

    public static void alGetSourcef(int source, int param, float[] value, int offset) {
        value[offset] = 0.0f;
    }

    public static float alGetSourcef(int source, int param) {
        return 0.0f;
    }

    // ========== Buffer queueing ==========

    public static void alSourceQueueBuffers(int source, int n, int[] buffers, int offset) {
        // no-op
    }

    public static void alSourceUnqueueBuffers(int source, int n, int[] buffers, int offset) {
        for (int i = 0; i < n; i++) {
            buffers[offset + i] = 0;
        }
    }

    // ========== Buffer management ==========

    public static void alGenBuffers(int n, int[] buffers, int offset) {
        for (int i = 0; i < n; i++) {
            buffers[offset + i] = nextId++;
        }
    }

    public static int alGenBuffers() {
        return nextId++;
    }

    public static void alDeleteBuffers(int n, int[] buffers, int offset) {
        // no-op
    }

    public static void alDeleteBuffers(int buffer) {
        // no-op
    }

    public static void alBufferData(int buffer, int format, Object data, int size, int frequency) {
        // no-op - data is typically a ByteBuffer, use Object for flexibility
    }

    public static void alBufferData(int buffer, int format, java.nio.ByteBuffer data, int size, int frequency) {
        // no-op
    }

    public static void alGetBufferi(int buffer, int param, int[] value, int offset) {
        value[offset] = 0;
    }

    // ========== Error/query ==========

    public static int alGetError() {
        return AL_NO_ERROR;
    }

    public static int alGetEnumValue(String enumName) {
        return 0;
    }

    public static boolean alIsExtensionPresent(String extName) {
        return false;
    }

    // ========== Listener ==========

    public static void alListenerf(int param, float value) {
        // no-op
    }

    public static void alListener3f(int param, float v1, float v2, float v3) {
        // no-op
    }

    public static void alListenerfv(int param, float[] values) {
        // no-op
    }

    public static void alListeneri(int param, int value) {
        // no-op
    }

    // ========== Global ==========

    public static String alGetString(int param) {
        return "";
    }

    public static boolean alIsSource(int source) {
        return false;
    }

    public static boolean alIsBuffer(int buffer) {
        return false;
    }

    public static void alDistanceModel(int model) {
        // no-op
    }

    public static void alSpeedOfSound(float speed) {
        // no-op
    }

    public static void alDopplerFactor(float factor) {
        // no-op
    }

    public static void alDopplerVelocity(float velocity) {
        // no-op
    }

    public static void alEnable(int capability) {
        // no-op
    }

    public static void alDisable(int capability) {
        // no-op
    }

    public static boolean alIsEnabled(int capability) {
        return false;
    }

    public static void alGetFloatv(int param, float[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = 0.0f;
        }
    }

    public static void alGetIntegerv(int param, int[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = 0;
        }
    }

    public static float alGetFloat(int param) {
        return 0.0f;
    }

    public static int alGetInteger(int param) {
        return 0;
    }

    /**
     * MC 26.1.2: Deletes a list of buffer IDs.
     */
    public static void alDeleteBuffers(int[] buffers) {
        if (buffers != null) {
            for (int buf : buffers) {
                alDeleteBuffers(buf);
            }
        }
    }

    public static void alGenSources(int[] sources) {
        if (sources != null) {
            for (int i = 0; i < sources.length; i++) sources[i] = alGenSources();
        }
    }

    public static void alBufferData(int buffer, int format, java.nio.ByteBuffer data, int frequency) {}
    public static void alDeleteSources(int[] sources) { if (sources != null) for (int s : sources) alDeleteSources(s); }
    public static void alGenBuffers(int[] buffers) { if (buffers != null) for (int i = 0; i < buffers.length; i++) buffers[i] = alGenBuffers(); }
    public static void alSourceQueueBuffers(int source, int[] buffers) {}
    public static void alSourceUnqueueBuffers(int source, int[] buffers) {}
}
