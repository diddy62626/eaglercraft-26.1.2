package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * TeaVM-compatible stub for GLFW cursor enter callback.
 */
public class GLFWCursorEnterCallback extends Callback implements GLFWCursorEnterCallbackI {
    public GLFWCursorEnterCallback(long pointer) {
        super(pointer);
    }

    public GLFWCursorEnterCallback(GLFWCursorEnterCallbackI callback) {
        super(0L);
    }

    @Override
    public void invoke(long window, boolean entered) {
    }

    public static GLFWCursorEnterCallback create(GLFWCursorEnterCallbackI callback) {
        return new GLFWCursorEnterCallback(callback);
    }
}
