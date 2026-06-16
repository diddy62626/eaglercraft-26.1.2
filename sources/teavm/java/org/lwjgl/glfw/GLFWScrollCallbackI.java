package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWScrollCallbackI extends CallbackI {
    void invoke(long window, double xoffset, double yoffset);
}
