package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWIMEStatusCallbackI extends CallbackI {
    void invoke(long window, int status);
}
