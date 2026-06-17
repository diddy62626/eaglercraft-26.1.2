package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWCharCallbackI extends CallbackI {
    void invoke(long window, int codepoint);
}
