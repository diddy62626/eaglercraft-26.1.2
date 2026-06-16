package org.lwjgl.glfw;

/**
 * TeaVM-compatible callback interface for GLFW window position events.
 */
public interface GLFWWindowPosCallbackI {
    void invoke(long window, int xpos, int ypos);
}
