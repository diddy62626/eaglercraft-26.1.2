package org.lwjgl.glfw;

/**
 * TeaVM-compatible stub for LWJGL's GLFW Callbacks utility class.
 * In the browser, GLFW callbacks are handled through PlatformInput.
 */
public class Callbacks {

    public static void glfwFreeCallbacks(long window) {
        // No-op: browser doesn't use native callbacks
    }

    public static long glfwSetErrorCallback(GLFWErrorCallbackI callback) {
        return 0L;
    }

    public static long glfwSetWindowCloseCallback(long window, GLFWWindowCloseCallbackI callback) {
        return 0L;
    }

    public static long glfwSetWindowFocusCallback(long window, GLFWWindowFocusCallbackI callback) {
        return 0L;
    }

    public static long glfwSetWindowIconifyCallback(long window, GLFWWindowIconifyCallbackI callback) {
        return 0L;
    }

    public static long glfwSetWindowPosCallback(long window, GLFWWindowPosCallbackI callback) {
        return 0L;
    }

    public static long glfwSetWindowSizeCallback(long window, GLFWWindowSizeCallbackI callback) {
        return 0L;
    }

    public static long glfwSetFramebufferSizeCallback(long window, GLFWFramebufferSizeCallbackI callback) {
        return 0L;
    }

    public static long glfwSetCursorEnterCallback(long window, GLFWCursorEnterCallbackI callback) {
        return 0L;
    }
}
