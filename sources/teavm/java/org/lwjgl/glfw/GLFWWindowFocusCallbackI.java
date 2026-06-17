package org.lwjgl.glfw;

/**
 * TeaVM-compatible callback interface for GLFW window focus events.
 */
public interface GLFWWindowFocusCallbackI {
    void invoke(long window, boolean focused);
}
