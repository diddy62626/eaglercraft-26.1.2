package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWCursorPosCallbackI extends CallbackI {
    void invoke(long window, double xpos, double ypos);
}
