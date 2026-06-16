package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * TeaVM-compatible stub for GLFW window size callback.
 */
public class GLFWWindowSizeCallback extends Callback implements GLFWWindowSizeCallbackI {
    public GLFWWindowSizeCallback(long pointer) {
        super(pointer);
    }

    public GLFWWindowSizeCallback(GLFWWindowSizeCallbackI callback) {
        super(0L);
    }

    @Override
    public void invoke(long window, int width, int height) {
    }

    public static GLFWWindowSizeCallback create(GLFWWindowSizeCallbackI callback) {
        return new GLFWWindowSizeCallback(callback);
    }
}
