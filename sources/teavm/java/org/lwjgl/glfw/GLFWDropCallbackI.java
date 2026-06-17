package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWDropCallbackI extends CallbackI {
    void invoke(long window, int count, long names);
}
