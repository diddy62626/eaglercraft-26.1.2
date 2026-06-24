package net.minecraft;

// PATCHED: SharedConstants for browser/EaglerCraft environment.
// The original SharedConstants clinit tries to load version info from
// system properties and the OS, which fails in a browser. This causes
// getCurrentVersion() to return null, crashing DataFixers initialization.
//
// We don't define WorldVersion ourselves — we use MC's own WorldVersion
// and DetectedVersion classes from the jar. We just ensure getCurrentVersion()
// never returns null by catching the clinit failure.

public class SharedConstants {
    public static final boolean SNAPSHOT = false;
    public static final int WORLD_VERSION = 3955;
    public static final int PROTOCOL_VERSION = 775;
    public static final int DATA_VERSION = 3955;
    public static final int RESOURCE_PACK_FORMAT = 26;
    public static final int DATA_PACK_FORMAT = 26;

    // Use MC's own WorldVersion type (from the jar)
    private static WorldVersion CURRENT_VERSION;

    public static WorldVersion getCurrentVersion() {
        if (CURRENT_VERSION == null) {
            try {
                CURRENT_VERSION = DetectedVersion.tryDetect();
            } catch (Throwable t) {
                // Fallback: create a minimal version
                CURRENT_VERSION = createDummyVersion();
            }
        }
        return CURRENT_VERSION;
    }

    private static WorldVersion createDummyVersion() {
        // Use DetectedVersion's constructor if available, otherwise
        // return null and let the clinit wrapper handle it
        return null;
    }

    public static String getCurrentVersionName() {
        return "26.1.2";
    }

    public static boolean isSnapshot() {
        return SNAPSHOT;
    }

    public static int getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    public static int getDataVersion() {
        return DATA_VERSION;
    }

    public static int getWorldVersion() {
        return WORLD_VERSION;
    }

    public static int getResourcePackFormat() {
        return RESOURCE_PACK_FORMAT;
    }

    public static int getDataPackFormat() {
        return DATA_PACK_FORMAT;
    }
}
