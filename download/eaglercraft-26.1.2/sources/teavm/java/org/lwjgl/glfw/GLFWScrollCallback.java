package org.lwjgl.glfw;

public abstract class GLFWScrollCallback {
    public abstract void invoke(long window, double xoffset, double yoffset);
}
