package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * TeaVM-compatible stub for GLFW window position callback.
 */
public class GLFWWindowPosCallback extends Callback implements GLFWWindowPosCallbackI {
    public GLFWWindowPosCallback(long pointer) {
        super(pointer);
    }

    public GLFWWindowPosCallback(GLFWWindowPosCallbackI callback) {
        super(0L);
    }

    @Override
    public void invoke(long window, int xpos, int ypos) {
    }

    public static GLFWWindowPosCallback create(GLFWWindowPosCallbackI callback) {
        return new GLFWWindowPosCallback(callback);
    }
}
