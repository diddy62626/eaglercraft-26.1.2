package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWMouseButtonCallbackI extends CallbackI {
    void invoke(long window, int button, int action, int mods);
}
