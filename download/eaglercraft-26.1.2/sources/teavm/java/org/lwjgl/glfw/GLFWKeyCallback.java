package org.lwjgl.glfw;

public abstract class GLFWKeyCallback {
    public abstract void invoke(long window, int key, int scancode, int action, int mods);
}
