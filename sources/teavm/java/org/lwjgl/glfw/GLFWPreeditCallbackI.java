package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWPreeditCallbackI extends CallbackI {
    void invoke(long window, int magic);
}
