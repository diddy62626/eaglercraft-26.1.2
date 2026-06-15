package org.lwjgl.glfw;

public class GLFWGammaRamp {
    public short[] red;
    public short[] green;
    public short[] blue;

    public GLFWGammaRamp() {
    }

    public GLFWGammaRamp(short[] red, short[] green, short[] blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
