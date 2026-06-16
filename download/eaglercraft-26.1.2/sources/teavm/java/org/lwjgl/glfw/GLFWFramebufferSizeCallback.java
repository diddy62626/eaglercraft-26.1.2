package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * TeaVM-compatible stub for GLFW framebuffer size callback.
 */
public class GLFWFramebufferSizeCallback extends Callback implements GLFWFramebufferSizeCallbackI {
    public GLFWFramebufferSizeCallback(long pointer) {
        super(pointer);
    }

    public GLFWFramebufferSizeCallback(GLFWFramebufferSizeCallbackI callback) {
        super(0L);
    }

    @Override
    public void invoke(long window, int width, int height) {
    }

    public static GLFWFramebufferSizeCallback create(GLFWFramebufferSizeCallbackI callback) {
        return new GLFWFramebufferSizeCallback(callback);
    }
}
