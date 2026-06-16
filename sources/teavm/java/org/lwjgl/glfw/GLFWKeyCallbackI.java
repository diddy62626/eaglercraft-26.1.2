package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;

public interface GLFWKeyCallbackI extends CallbackI {
    void invoke(long window, int key, int scancode, int action, int mods);
}
