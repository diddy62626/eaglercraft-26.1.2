package org.lwjgl.glfw;

/**
 * TeaVM-compatible callback interface for GLFW framebuffer size events.
 */
public interface GLFWFramebufferSizeCallbackI {
    void invoke(long window, int width, int height);
}
