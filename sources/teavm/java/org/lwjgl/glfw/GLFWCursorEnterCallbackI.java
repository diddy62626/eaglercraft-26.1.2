package org.lwjgl.glfw;

/**
 * TeaVM-compatible callback interface for GLFW cursor enter events.
 */
public interface GLFWCursorEnterCallbackI {
    void invoke(long window, boolean entered);
}
