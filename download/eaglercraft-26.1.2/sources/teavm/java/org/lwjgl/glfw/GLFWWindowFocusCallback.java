package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * TeaVM-compatible stub for GLFW window focus callback.
 */
public class GLFWWindowFocusCallback extends Callback implements GLFWWindowFocusCallbackI {
    public GLFWWindowFocusCallback(long pointer) {
        super(pointer);
    }

    public GLFWWindowFocusCallback(GLFWWindowFocusCallbackI callback) {
        super(0L);
    }

    @Override
    public void invoke(long window, boolean focused) {
    }

    public static GLFWWindowFocusCallback create(GLFWWindowFocusCallbackI callback) {
        return new GLFWWindowFocusCallback(callback);
    }
}
