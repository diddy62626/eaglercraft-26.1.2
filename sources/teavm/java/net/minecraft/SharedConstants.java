package net.minecraft;

// PATCHED: SharedConstants for browser/EaglerCraft environment.
// The original SharedConstants clinit tries to load version info from
// system properties and the OS, which fails in a browser. This causes
// getCurrentVersion() to return null, crashing DataFixers initialization.
//
// This patch provides a dummy WorldVersion that lets DataFixers build
// its fixer upper without a real version lookup.

public class SharedConstants {
    public static final boolean SNAPSHOT = false;
    public static final int WORLD_VERSION = 3955;
    public static final int PROTOCOL_VERSION = 775;
    public static final int DATA_VERSION = 3955;
    public static final int RESOURCE_PACK_FORMAT = 26;
    public static final int DATA_PACK_FORMAT = 26;

    private static WorldVersion CURRENT_VERSION;

    public static WorldVersion getCurrentVersion() {
        if (CURRENT_VERSION == null) {
            CURRENT_VERSION = new DummyVersion();
        }
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

    // Dummy WorldVersion implementation
    public static class DummyVersion implements WorldVersion {
        @Override
        public int getDataVersion() {
            return DATA_VERSION;
        }

        @Override
        public int getProtocolVersion() {
            return PROTOCOL_VERSION;
        }

        @Override
        public int getResourcePackVersion() {
            return RESOURCE_PACK_FORMAT;
        }

        @Override
        public int getDataPackVersion() {
            return DATA_PACK_FORMAT;
        }

        @Override
        public String getName() {
            return "26.1.2";
        }

        @Override
        public boolean isStable() {
            return true;
        }
    }

    // WorldVersion interface (matches MC's WorldVersion)
    public interface WorldVersion {
        int getDataVersion();
        int getProtocolVersion();
        int getResourcePackVersion();
        int getDataPackVersion();
        String getName();
        boolean isStable();
    }
}
