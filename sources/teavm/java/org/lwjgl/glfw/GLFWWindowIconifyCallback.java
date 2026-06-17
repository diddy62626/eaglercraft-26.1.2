package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * TeaVM-compatible stub for GLFW window iconify callback.
 */
public class GLFWWindowIconifyCallback extends Callback implements GLFWWindowIconifyCallbackI {
    public GLFWWindowIconifyCallback(long pointer) {
        super(pointer);
    }

    public GLFWWindowIconifyCallback(GLFWWindowIconifyCallbackI callback) {
        super(0L);
    }

    @Override
    public void invoke(long window, boolean iconified) {
    }

    public static GLFWWindowIconifyCallback create(GLFWWindowIconifyCallbackI callback) {
        return new GLFWWindowIconifyCallback(callback);
    }
}
