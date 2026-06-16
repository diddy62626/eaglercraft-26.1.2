package org.lwjgl.glfw;

import java.nio.ByteBuffer;
import org.lwjgl.system.CustomBuffer;

/**
 * TeaVM-compatible stub for LWJGL's GLFWImage.
 * Used for window icons and cursors.
 */
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

    public GLFWImage width(int width) {
        this.width = width;
        return this;
    }

    public GLFWImage height(int height) {
        this.height = height;
        return this;
    }

    public GLFWImage pixels(ByteBuffer pixels) {
        this.pixels = pixels;
        return this;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public ByteBuffer pixels() {
        return pixels instanceof ByteBuffer ? (ByteBuffer) pixels : null;
    }

    public static GLFWImage malloc() {
        return new GLFWImage();
    }

    public static GLFWImage create() {
        return new GLFWImage();
    }

    public static GLFWImage malloc(long address) {
        return new GLFWImage();
    }

    /**
     * TeaVM-compatible stub for GLFWImage.Buffer (struct array).
     * Used when setting multiple window icons.
     */
    public static class Buffer extends CustomBuffer<Buffer> {

        public Buffer(long address, int capacity) {
            super(address, capacity);
        }

        public Buffer(ByteBuffer container) {
            super(container, container != null ? container.capacity() / GLFWImage.SIZEOF : 0);
        }

        public GLFWImage get(int index) {
            return new GLFWImage();
        }

        public Buffer put(int index, GLFWImage value) {
            return this;
        }

        public int width(int index) {
            return 0;
        }

        public Buffer width(int index, int value) {
            return this;
        }

        public int height(int index) {
            return 0;
        }

        public Buffer height(int index, int value) {
            return this;
        }
    }

    public static final int SIZEOF = 16;
}
