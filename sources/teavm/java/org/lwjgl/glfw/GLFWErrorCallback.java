package org.lwjgl.glfw;

public abstract class GLFWErrorCallback {
    public abstract void invoke(int error, long description);
    public static String getDescription(long description) { return ""; }
}
