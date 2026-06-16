package org.lwjgl.glfw;

/**
 * TeaVM-compatible callback interface for GLFW window close events.
 */
public interface GLFWWindowCloseCallbackI {
    void invoke(long window);
}
