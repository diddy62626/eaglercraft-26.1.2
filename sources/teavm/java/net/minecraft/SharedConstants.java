package net.minecraft;

// PATCHED: SharedConstants for browser/EaglerCraft environment.
// getCurrentVersion() creates a WorldVersion.Simple directly instead of
// calling DetectedVersion.createBuiltIn() which may fail due to Record issues.

public class SharedConstants {
    public static final boolean SNAPSHOT = false;
    public static final int WORLD_VERSION = 3955;
    public static final int PROTOCOL_VERSION = 775;
    public static final int DATA_VERSION = 3955;
    public static final int RESOURCE_PACK_FORMAT = 26;
    public static final int DATA_PACK_FORMAT = 26;

    // Debug fields (all false for production)
    public static final boolean IS_RUNNING_IN_IDE = false;
    public static final boolean DEBUG_SYNCHRONOUS_GL_LOGS = false;
    public static final boolean DEBUG_ALLOW_LOW_SIM_DISTANCE = false;
    public static final boolean DEBUG_SUBTITLES = false;
    public static final boolean DEBUG_PATHFINDING = false;
    public static final boolean DEBUG_NEIGHBORSUPDATE = false;
    public static final boolean DEBUG_EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER = false;
    public static final boolean DEBUG_STRUCTURES = false;
    public static final boolean DEBUG_VILLAGE_SECTIONS = false;
    public static final boolean DEBUG_BRAIN = false;
    public static final boolean DEBUG_BEES = false;
    public static final boolean DEBUG_RAIDS = false;
    public static final boolean DEBUG_POI = false;
    public static final boolean DEBUG_GOAL_SELECTOR = false;
    public static final boolean DEBUG_GAME_EVENT_LISTENERS = false;
    public static final boolean DEBUG_SHAPES = false;
    public static final boolean DEBUG_SHOW_SERVER_DEBUG_VALUES = false;
    public static final boolean DEBUG_SOCIAL_INTERACTIONS = false;
    public static final boolean DEBUG_HOTKEYS = false;
    public static final boolean DEBUG_CURSOR_POS = false;
    public static final boolean DEBUG_RENDER_UI_LAYERING_RECTANGLES = false;
    public static final boolean DEBUG_SHUFFLE_MODELS = false;
    public static final boolean DEBUG_SHUFFLE_UI_RENDERING_ORDER = false;
    public static final boolean DEBUG_ACTIVE_TEXT_AREAS = false;
    public static final boolean DEBUG_ENTITY_BLOCK_INTERSECTION = false;
    public static final boolean DEBUG_BREEZE_MOB = false;
    public static final boolean DEBUG_OPEN_INCOMPATIBLE_WORLDS = false;
    public static final boolean DEBUG_FORCE_ONBOARDING_SCREEN = false;
    public static final boolean DEBUG_FORCE_TELEMETRY = false;
    public static final boolean DEBUG_DONT_SEND_TELEMETRY_TO_BACKEND = false;
    public static final boolean DEBUG_BYPASS_REALMS_VERSION_CHECK = false;
    public static final boolean DEBUG_WORLD_RECREATE = false;
    public static final boolean DEBUG_VALIDATE_RESOURCE_PATH_CASE = false;
    public static final boolean DEBUG_DISABLE_BELOW_ZERO_RETROGENERATION = false;
    public static final boolean DEBUG_UI_NARRATION = false;
    public static final boolean DEBUG_DEFAULT_SKIN_OVERRIDE = false;
    public static final boolean DEBUG_SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES = false;
    public static final boolean CHECK_DATA_FIXER_SCHEMA = false;

    public static final char[] ILLEGAL_FILE_CHARACTERS = new char[]{'/', '\n', '\r', '\t', '\0', '\\', ':', '*', '?', '"', '<', '>', '|'};

    private static WorldVersion CURRENT_VERSION;

    public static WorldVersion getCurrentVersion() {
        if (CURRENT_VERSION == null) {
            try {
                System.out.println("[SharedConstants] Creating built-in version...");
                CURRENT_VERSION = DetectedVersion.createBuiltIn("26.1.2", "26.1.2", true);
                System.out.println("[SharedConstants] Version created: " + CURRENT_VERSION);
            } catch (Throwable t) {
                System.out.println("[SharedConstants] createBuiltIn failed: " + t);
                try {
                    // Try using the BUILT_IN field directly
                    java.lang.reflect.Field f = DetectedVersion.class.getDeclaredField("BUILT_IN");
                    f.setAccessible(true);
                    CURRENT_VERSION = (WorldVersion) f.get(null);
                    System.out.println("[SharedConstants] BUILT_IN field: " + CURRENT_VERSION);
                } catch (Throwable t2) {
                    System.out.println("[SharedConstants] BUILT_IN field failed: " + t2);
                }
            }
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
}
