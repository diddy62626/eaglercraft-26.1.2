package com.mojang.blaze3d.platform;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformInput;

/**
 * EaglerCraft 26.1.2 browser override for InputConstants.
 * Maps browser key events to Minecraft key constants (matching GLFW key codes).
 * Delegates key state queries to PlatformInput.
 */
public class InputConstants {

        // ========== Key Type Enum ==========

        public enum Type {
                KEYSYM,
                SCANCODE,
                MOUSE;

                private static final Type[] VALUES = values();

                public static Type byName(String name) {
                        for (Type t : VALUES) {
                                if (t.name().equalsIgnoreCase(name)) return t;
                        }
                        return KEYSYM;
                }

                /**
                 * Returns the Key for the given integer value within this Type,
                 * creating a new Key if one hasn't been registered yet.
                 * Matches MC 26.1.2's getOrCreate(int) signature.
                 */
                public Key getOrCreate(int value) {
                        Key key = InputConstants.getKey(this, value);
                        return key;
                }
        }

        // ========== Key Class ==========

        public static class Key {
                private final int value;
                private final String name;
                private final Type type;

                public Key(int value, String name, Type type) {
                        this.value = value;
                        this.name = name;
                        this.type = type;
                }

                public int getValue() {
                        return value;
                }

                public String getName() {
                        return name;
                }

                public Type getType() {
                        return type;
                }

                @Override
                public String toString() {
                        return "Key[" + type.name() + ":" + value + ",'" + name + "']";
                }
        
	/**
	 * MC 26.1.2: Returns the display name Component for this key.
	 * Browser: returns null (Component is a Minecraft class not reachable in TeaVM yet).
	 */
	public net.minecraft.network.chat.Component getDisplayName() {
		return null;
	}
}


        // ========== Key Registry ==========

        private static final Map<String, Key> keyByName = new HashMap<>();
        private static final Map<Integer, Key> keyByValue = new HashMap<>();

        /** Standard GLFW key constants - matching browser keyCode/which values. */
        public static final int KEY_UNKNOWN = -1;

        /** MC 26.1.2 UNKNOWN Key singleton (static field). */
        public static final Key UNKNOWN = new Key(KEY_UNKNOWN, "unknown", Type.KEYSYM);
        public static final int KEY_ESCAPE = 256;
        public static final int KEY_0 = 48;
        public static final int KEY_1 = 49;
        public static final int KEY_2 = 50;
        public static final int KEY_3 = 51;
        public static final int KEY_4 = 52;
        public static final int KEY_5 = 53;
        public static final int KEY_6 = 54;
        public static final int KEY_7 = 55;
        public static final int KEY_8 = 56;
        public static final int KEY_9 = 57;
        public static final int KEY_A = 65;
        public static final int KEY_B = 66;
        public static final int KEY_C = 67;
        public static final int KEY_D = 68;
        public static final int KEY_E = 69;
        public static final int KEY_F = 70;
        public static final int KEY_G = 71;
        public static final int KEY_H = 72;
        public static final int KEY_I = 73;
        public static final int KEY_J = 74;
        public static final int KEY_K = 75;
        public static final int KEY_L = 76;
        public static final int KEY_M = 77;
        public static final int KEY_N = 78;
        public static final int KEY_O = 79;
        public static final int KEY_P = 80;
        public static final int KEY_Q = 81;
        public static final int KEY_R = 82;
        public static final int KEY_S = 83;
        public static final int KEY_T = 84;
        public static final int KEY_U = 85;
        public static final int KEY_V = 86;
        public static final int KEY_W = 87;
        public static final int KEY_X = 88;
        public static final int KEY_Y = 89;
        public static final int KEY_Z = 90;
        public static final int KEY_F1 = 290;
        public static final int KEY_F2 = 291;
        public static final int KEY_F3 = 292;
        public static final int KEY_F4 = 293;
        public static final int KEY_F5 = 294;
        public static final int KEY_F6 = 295;
        public static final int KEY_F7 = 296;
        public static final int KEY_F8 = 297;
        public static final int KEY_F9 = 298;
        public static final int KEY_F10 = 299;
        public static final int KEY_F11 = 300;
        public static final int KEY_F12 = 301;
        public static final int KEY_SPACE = 32;
        public static final int KEY_LEFTSHIFT = 340;
        public static final int KEY_RIGHTSHIFT = 344;
        public static final int KEY_LEFTCONTROL = 341;
        public static final int KEY_RIGHTCONTROL = 345;
        public static final int KEY_LEFTALT = 342;
        public static final int KEY_RIGHTALT = 346;
        public static final int KEY_TAB = 258;
        public static final int KEY_ENTER = 257;
        public static final int KEY_BACKSPACE = 259;
        public static final int KEY_INSERT = 260;
        public static final int KEY_DELETE = 261;
        public static final int KEY_UP = 265;
        public static final int KEY_DOWN = 264;
        public static final int KEY_LEFT = 263;
        public static final int KEY_RIGHT = 262;
        public static final int KEY_PAGE_UP = 266;
        public static final int KEY_PAGE_DOWN = 267;
        public static final int KEY_HOME = 268;
        public static final int KEY_END = 269;
        public static final int KEY_CAPS_LOCK = 280;
        public static final int KEY_SCROLL_LOCK = 281;
        public static final int KEY_NUM_LOCK = 282;
        public static final int KEY_PRINTSCREEN = 283;
        public static final int KEY_PAUSE = 284;
        public static final int KEY_GRAVE = 96;
        public static final int KEY_MINUS = 45;
        public static final int KEY_EQUALS = 61;
        public static final int KEY_LBRACKET = 91;
        public static final int KEY_RBRACKET = 93;
        public static final int KEY_BACKSLASH = 92;
        public static final int KEY_SEMICOLON = 59;
        public static final int KEY_APOSTROPHE = 39;
        public static final int KEY_COMMA = 44;
        public static final int KEY_PERIOD = 46;
        public static final int KEY_SLASH = 47;
        public static final int KEY_NUMPAD0 = 320;
        public static final int KEY_NUMPAD1 = 321;
        public static final int KEY_NUMPAD2 = 322;
        public static final int KEY_NUMPAD3 = 323;
        public static final int KEY_NUMPAD4 = 324;
        public static final int KEY_NUMPAD5 = 325;
        public static final int KEY_NUMPAD6 = 326;
        public static final int KEY_NUMPAD7 = 327;
        public static final int KEY_NUMPAD8 = 328;
        public static final int KEY_NUMPAD9 = 329;
        public static final int KEY_NUMPAD_ENTER = 335;
        public static final int KEY_NUMPAD_ADD = 334;
        public static final int KEY_NUMPAD_SUBTRACT = 333;
        public static final int KEY_NUMPAD_MULTIPLY = 332;
        public static final int KEY_NUMPAD_DIVIDE = 331;
        public static final int KEY_NUMPAD_DECIMAL = 330;
        public static final int KEY_LWIN = 343;
        public static final int KEY_RWIN = 347;
        public static final int KEY_MENU = 348;

        // Mouse button constants
        public static final int MOUSE_BUTTON_LEFT = 0;
        public static final int MOUSE_BUTTON_MIDDLE = 1;
        public static final int MOUSE_BUTTON_RIGHT = 2;
        public static final int MOUSE_BUTTON_4 = 3;
        public static final int MOUSE_BUTTON_5 = 4;
        public static final int MOUSE_BUTTON_6 = 5;
        public static final int MOUSE_BUTTON_7 = 6;
        public static final int MOUSE_BUTTON_8 = 7;

        static {
                // Register common keys by name
                registerKey("key.keyboard.0", KEY_0, Type.KEYSYM);
                registerKey("key.keyboard.1", KEY_1, Type.KEYSYM);
                registerKey("key.keyboard.2", KEY_2, Type.KEYSYM);
                registerKey("key.keyboard.3", KEY_3, Type.KEYSYM);
                registerKey("key.keyboard.4", KEY_4, Type.KEYSYM);
                registerKey("key.keyboard.5", KEY_5, Type.KEYSYM);
                registerKey("key.keyboard.6", KEY_6, Type.KEYSYM);
                registerKey("key.keyboard.7", KEY_7, Type.KEYSYM);
                registerKey("key.keyboard.8", KEY_8, Type.KEYSYM);
                registerKey("key.keyboard.9", KEY_9, Type.KEYSYM);
                registerKey("key.keyboard.a", KEY_A, Type.KEYSYM);
                registerKey("key.keyboard.b", KEY_B, Type.KEYSYM);
                registerKey("key.keyboard.c", KEY_C, Type.KEYSYM);
                registerKey("key.keyboard.d", KEY_D, Type.KEYSYM);
                registerKey("key.keyboard.e", KEY_E, Type.KEYSYM);
                registerKey("key.keyboard.f", KEY_F, Type.KEYSYM);
                registerKey("key.keyboard.g", KEY_G, Type.KEYSYM);
                registerKey("key.keyboard.h", KEY_H, Type.KEYSYM);
                registerKey("key.keyboard.i", KEY_I, Type.KEYSYM);
                registerKey("key.keyboard.j", KEY_J, Type.KEYSYM);
                registerKey("key.keyboard.k", KEY_K, Type.KEYSYM);
                registerKey("key.keyboard.l", KEY_L, Type.KEYSYM);
                registerKey("key.keyboard.m", KEY_M, Type.KEYSYM);
                registerKey("key.keyboard.n", KEY_N, Type.KEYSYM);
                registerKey("key.keyboard.o", KEY_O, Type.KEYSYM);
                registerKey("key.keyboard.p", KEY_P, Type.KEYSYM);
                registerKey("key.keyboard.q", KEY_Q, Type.KEYSYM);
                registerKey("key.keyboard.r", KEY_R, Type.KEYSYM);
                registerKey("key.keyboard.s", KEY_S, Type.KEYSYM);
                registerKey("key.keyboard.t", KEY_T, Type.KEYSYM);
                registerKey("key.keyboard.u", KEY_U, Type.KEYSYM);
                registerKey("key.keyboard.v", KEY_V, Type.KEYSYM);
                registerKey("key.keyboard.w", KEY_W, Type.KEYSYM);
                registerKey("key.keyboard.x", KEY_X, Type.KEYSYM);
                registerKey("key.keyboard.y", KEY_Y, Type.KEYSYM);
                registerKey("key.keyboard.z", KEY_Z, Type.KEYSYM);
                registerKey("key.keyboard.escape", KEY_ESCAPE, Type.KEYSYM);
                registerKey("key.keyboard.space", KEY_SPACE, Type.KEYSYM);
                registerKey("key.keyboard.left.shift", KEY_LEFTSHIFT, Type.KEYSYM);
                registerKey("key.keyboard.right.shift", KEY_RIGHTSHIFT, Type.KEYSYM);
                registerKey("key.keyboard.left.control", KEY_LEFTCONTROL, Type.KEYSYM);
                registerKey("key.keyboard.right.control", KEY_RIGHTCONTROL, Type.KEYSYM);
                registerKey("key.keyboard.left.alt", KEY_LEFTALT, Type.KEYSYM);
                registerKey("key.keyboard.right.alt", KEY_RIGHTALT, Type.KEYSYM);
                registerKey("key.keyboard.tab", KEY_TAB, Type.KEYSYM);
                registerKey("key.keyboard.enter", KEY_ENTER, Type.KEYSYM);
                registerKey("key.keyboard.backspace", KEY_BACKSPACE, Type.KEYSYM);
                registerKey("key.keyboard.f1", KEY_F1, Type.KEYSYM);
                registerKey("key.keyboard.f2", KEY_F2, Type.KEYSYM);
                registerKey("key.keyboard.f3", KEY_F3, Type.KEYSYM);
                registerKey("key.keyboard.f4", KEY_F4, Type.KEYSYM);
                registerKey("key.keyboard.f5", KEY_F5, Type.KEYSYM);
                registerKey("key.keyboard.f6", KEY_F6, Type.KEYSYM);
                registerKey("key.keyboard.f7", KEY_F7, Type.KEYSYM);
                registerKey("key.keyboard.f8", KEY_F8, Type.KEYSYM);
                registerKey("key.keyboard.f9", KEY_F9, Type.KEYSYM);
                registerKey("key.keyboard.f10", KEY_F10, Type.KEYSYM);
                registerKey("key.keyboard.f11", KEY_F11, Type.KEYSYM);
                registerKey("key.keyboard.f12", KEY_F12, Type.KEYSYM);
                registerKey("key.keyboard.up", KEY_UP, Type.KEYSYM);
                registerKey("key.keyboard.down", KEY_DOWN, Type.KEYSYM);
                registerKey("key.keyboard.left", KEY_LEFT, Type.KEYSYM);
                registerKey("key.keyboard.right", KEY_RIGHT, Type.KEYSYM);
                registerKey("key.keyboard.insert", KEY_INSERT, Type.KEYSYM);
                registerKey("key.keyboard.delete", KEY_DELETE, Type.KEYSYM);
                registerKey("key.keyboard.page.up", KEY_PAGE_UP, Type.KEYSYM);
                registerKey("key.keyboard.page.down", KEY_PAGE_DOWN, Type.KEYSYM);
                registerKey("key.keyboard.home", KEY_HOME, Type.KEYSYM);
                registerKey("key.keyboard.end", KEY_END, Type.KEYSYM);
                registerKey("key.keyboard.caps.lock", KEY_CAPS_LOCK, Type.KEYSYM);
                registerKey("key.keyboard.grave.accent", KEY_GRAVE, Type.KEYSYM);
                registerKey("key.keyboard.minus", KEY_MINUS, Type.KEYSYM);
                registerKey("key.keyboard.equal", KEY_EQUALS, Type.KEYSYM);
                registerKey("key.keyboard.left.bracket", KEY_LBRACKET, Type.KEYSYM);
                registerKey("key.keyboard.right.bracket", KEY_RBRACKET, Type.KEYSYM);
                registerKey("key.keyboard.backslash", KEY_BACKSLASH, Type.KEYSYM);
                registerKey("key.keyboard.semicolon", KEY_SEMICOLON, Type.KEYSYM);
                registerKey("key.keyboard.apostrophe", KEY_APOSTROPHE, Type.KEYSYM);
                registerKey("key.keyboard.comma", KEY_COMMA, Type.KEYSYM);
                registerKey("key.keyboard.period", KEY_PERIOD, Type.KEYSYM);
                registerKey("key.keyboard.slash", KEY_SLASH, Type.KEYSYM);

                // Mouse buttons
                registerKey("key.mouse.left", MOUSE_BUTTON_LEFT, Type.MOUSE);
                registerKey("key.mouse.middle", MOUSE_BUTTON_MIDDLE, Type.MOUSE);
                registerKey("key.mouse.right", MOUSE_BUTTON_RIGHT, Type.MOUSE);
                registerKey("key.mouse.4", MOUSE_BUTTON_4, Type.MOUSE);
                registerKey("key.mouse.5", MOUSE_BUTTON_5, Type.MOUSE);
        }

        private static void registerKey(String name, int value, Type type) {
                Key key = new Key(value, name, type);
                keyByName.put(name, key);
                keyByValue.put(value, key);
        }

        // ========== Key Lookup ==========

        /**
         * Gets a Key by its string name.
         */
        public static Key getKey(String name) {
                Key key = keyByName.get(name);
                if (key != null) return key;
                return new Key(KEY_UNKNOWN, name, Type.KEYSYM);
        }

        /**
         * Gets a Key by type and value.
         */
        public static Key getKey(Type type, int value) {
                Key key = keyByValue.get(value);
                if (key != null && key.getType() == type) return key;
                return new Key(value, type.name().toLowerCase() + "." + value, type);
        }

        /**
         * Creates a key for a keysym value.
         */
        public static Key keyForKeysym(int keysym) {
                return getKey(Type.KEYSYM, keysym);
        }

        /**
         * Creates a key for a scancode value.
         */
        public static Key keyForScancode(int scancode) {
                return getKey(Type.SCANCODE, scancode);
        }

        // ========== Key State ==========

        /**
         * Checks if a key is currently pressed.
         * Delegates to PlatformInput's key state tracking.
         *
         * @param window The window handle (unused in browser)
         * @param key    The key code (GLFW key code)
         * @return true if the key is pressed
         */
        public static boolean isKeyDown(long window, int key) {
                return PlatformInput.isKeyDown(key);
        }

        /**
         * Simulates a mouse button press.
         * Notifies PlatformInput of the button event.
         */
        public static void mouseButtonDown(int button) {
                // PlatformInput handles mouse events via DOM callbacks
        }

        /**
         * Simulates a mouse button release.
         */
        public static void releaseMouseButton(int button) {
                // PlatformInput handles mouse events via DOM callbacks
        }

        /**
         * Returns the GLFW key name for the given key and scancode.
         */
        public static String getKeyName(int key, int scancode) {
                Key k = keyByValue.get(key);
                if (k != null) return k.getName();
                return "unknown";
        }

        // ========== Constants for input type checks ==========

        /** Constant for the GLFW window pointer (unused in browser). */
        public static final long WINDOW_NULL = 0L;

    public static boolean isKeyDown(Window window, int key) {
        return net.lax1dude.eaglercraft.v2_6.internal.PlatformInput.isKeyDown(key);
    }

	// ========== MC 26.1.2 input callback setup ==========

	/**
	 * MC 26.1.2: Grabs or releases the mouse cursor (Pointer Lock API in browser).
	 */
	public static void grabOrReleaseMouse(Window window, int action, double xpos, double ypos) {
		// Browser: Pointer Lock API is handled by PlatformInput
	}

	/**
	 * MC 26.1.2: Sets up keyboard callbacks (key, char, preedit, IME status).
	 * Browser: PlatformInput handles all keyboard events via DOM, no-op here.
	 */
	public static void setupKeyboardCallbacks(Window window,
			org.lwjgl.glfw.GLFWKeyCallbackI keyCallback,
			org.lwjgl.glfw.GLFWCharCallbackI charCallback,
			org.lwjgl.glfw.GLFWPreeditCallbackI preeditCallback,
			org.lwjgl.glfw.GLFWIMEStatusCallbackI imeStatusCallback) {
		// no-op in browser - PlatformInput handles keyboard
	}

	/**
	 * MC 26.1.2: Sets up mouse callbacks (cursor pos, mouse button, scroll, drop).
	 * Browser: PlatformInput handles all mouse events via DOM, no-op here.
	 */
	public static void setupMouseCallbacks(Window window,
			org.lwjgl.glfw.GLFWCursorPosCallbackI cursorPosCallback,
			org.lwjgl.glfw.GLFWMouseButtonCallbackI mouseButtonCallback,
			org.lwjgl.glfw.GLFWScrollCallbackI scrollCallback,
			org.lwjgl.glfw.GLFWDropCallbackI dropCallback) {
		// no-op in browser - PlatformInput handles mouse
	}
}
