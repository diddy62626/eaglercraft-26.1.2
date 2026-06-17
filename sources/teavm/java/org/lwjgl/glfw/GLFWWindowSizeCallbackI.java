package org.lwjgl.glfw;

/**
 * TeaVM-compatible callback interface for GLFW window size events.
 */
public interface GLFWWindowSizeCallbackI {
    void invoke(long window, int width, int height);
}
