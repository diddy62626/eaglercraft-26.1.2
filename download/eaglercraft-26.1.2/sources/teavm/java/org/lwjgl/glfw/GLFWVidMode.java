package org.lwjgl.glfw;

public class GLFWVidMode {
    public int width;
    public int height;
    public int redBits;
    public int greenBits;
    public int blueBits;
    public int refreshRate;

    public GLFWVidMode() {
    }

    public GLFWVidMode(int width, int height, int redBits, int greenBits, int blueBits, int refreshRate) {
        this.width = width;
        this.height = height;
        this.redBits = redBits;
        this.greenBits = greenBits;
        this.blueBits = blueBits;
        this.refreshRate = refreshRate;
    }
}
