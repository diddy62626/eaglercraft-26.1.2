package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * TeaVM-compatible stub for GLFW window close callback.
 */
public class GLFWWindowCloseCallback extends Callback implements GLFWWindowCloseCallbackI {
    public GLFWWindowCloseCallback(long pointer) {
        super(pointer);
    }

    public GLFWWindowCloseCallback(GLFWWindowCloseCallbackI callback) {
        super(0L);
    }

    @Override
    public void invoke(long window) {
        // Browser handles window close events through PlatformInput
    }

    public static GLFWWindowCloseCallback create(long pointer) {
        return new GLFWWindowCloseCallback(pointer);
    }

    public static GLFWWindowCloseCallback create(GLFWWindowCloseCallbackI callback) {
        return new GLFWWindowCloseCallback(callback);
    }
}
