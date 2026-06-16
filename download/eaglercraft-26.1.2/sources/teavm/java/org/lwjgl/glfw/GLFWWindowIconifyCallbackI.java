package org.lwjgl.glfw;

/**
 * TeaVM-compatible callback interface for GLFW window iconify events.
 */
public interface GLFWWindowIconifyCallbackI {
    void invoke(long window, boolean iconified);
}
