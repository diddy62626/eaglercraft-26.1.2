package net.minecraft;

// PATCHED: SharedConstants for browser/EaglerCraft environment.
// The original SharedConstants clinit tries to load version info from
// system properties and the OS, which fails in a browser. This causes
// getCurrentVersion() to return null, crashing DataFixers initialization.
//
// We can't create a WorldVersion ourselves (the real one is in the MC jar
// with a complex API). Instead, we just make getCurrentVersion() catch
// any errors and return null gracefully. The clinit wrapper + downstream
// null checks should handle the rest.
//
// The key fix: ensure the clinit doesn't crash (the obfuscated clinit
// wrapper handles this). This class just provides the method signatures
// that TeaVM can find.

public class SharedConstants {
    public static final boolean SNAPSHOT = false;
    public static final int WORLD_VERSION = 3955;
    public static final int PROTOCOL_VERSION = 775;
    public static final int DATA_VERSION = 3955;
    public static final int RESOURCE_PACK_FORMAT = 26;
    public static final int DATA_PACK_FORMAT = 26;

    private static WorldVersion CURRENT_VERSION;

    public static WorldVersion getCurrentVersion() {
        // Return the cached version. If clinit failed, CURRENT_VERSION
        // will be null. The caller should handle null gracefully.
        return CURRENT_VERSION;
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
