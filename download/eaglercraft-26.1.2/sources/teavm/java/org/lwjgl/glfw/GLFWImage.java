package org.lwjgl.glfw;

public class GLFWImage {
    public int width;
    public int height;
    public Object pixels;

    public GLFWImage() {
    }

    public GLFWImage(int width, int height, Object pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }
}
