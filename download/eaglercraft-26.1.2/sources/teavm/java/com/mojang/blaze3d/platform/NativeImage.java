package com.mojang.blaze3d.platform;

import java.io.InputStream;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext;

/**
 * EaglerCraft 26.1.2 browser override for NativeImage.
 * Implements image pixel operations using a Java int[] buffer
 * and uploads to WebGL2 textures.
 */
public class NativeImage implements AutoCloseable {

        /** Image format enum matching MC's InternalGlFormat. */
        public enum InternalGlFormat {
                RGBA(WebGL2RenderingContext.RGBA, 4),
                RGB(WebGL2RenderingContext.RGB, 3),
                LUMINANCE_ALPHA(WebGL2RenderingContext.LUMINANCE_ALPHA, 2),
                LUMINANCE(WebGL2RenderingContext.LUMINANCE, 1);

                private final int glFormat;
                private final int componentCount;

                InternalGlFormat(int glFormat, int componentCount) {
                        this.glFormat = glFormat;
                        this.componentCount = componentCount;
                }

                public int glFormat() {
                        return glFormat;
                }

                public int componentCount() {
                        return componentCount;
                }
        }

        /** Pixel format for this image. */
        private final InternalGlFormat format;

        /** Image width in pixels. */
        private final int width;

        /** Image height in pixels. */
        private final int height;

        /** Pixel data as RGBA packed ints (0xAARRGGBB). */
        private final int[] pixels;

        /** Whether this image has been closed. */
        private boolean closed;

        /** BACKING_WIDTH constant used by Minecraft for texture alignment. */
        public static final int BACKING_WIDTH = 4;

        public NativeImage(int width, int height) {
                this(InternalGlFormat.RGBA, width, height, false);
        }

        public NativeImage(InternalGlFormat format, int width, int height, boolean useStb) {
                this.format = format;
                this.width = width;
                this.height = height;
                this.pixels = new int[width * height];
                this.closed = false;
        }

        // ========== Size ==========

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }

        public InternalGlFormat format() {
                return format;
        }

        // ========== Pixel Access ==========

        /**
         * Gets the RGBA pixel value at (x, y).
         * Returns in ABGR format (as MC expects): 0xAABBGGRR.
         */
        public int getPixelRGBA(int x, int y) {
                if (closed) throw new IllegalStateException("Image is closed");
                if (x < 0 || x >= width || y < 0 || y >= height) return 0;
                return pixels[y * width + x];
        }

        /**
         * Sets the RGBA pixel value at (x, y).
         * Expects ABGR format (as MC uses): 0xAABBGGRR.
         */
        public void setPixelRGBA(int x, int y, int rgba) {
                if (closed) throw new IllegalStateException("Image is closed");
                if (x >= 0 && x < width && y >= 0 && y < height) {
                        pixels[y * width + x] = rgba;
                }
        }

        /**
         * Gets the alpha component of the pixel at (x, y).
         */
        public int getAlpha(int x, int y) {
                return (getPixelRGBA(x, y) >> 24) & 0xFF;
        }

        /**
         * Sets the alpha component of the pixel at (x, y).
         */
        public void setAlpha(int x, int y, int alpha) {
                int pixel = getPixelRGBA(x, y);
                pixel = (pixel & 0x00FFFFFF) | ((alpha & 0xFF) << 24);
                setPixelRGBA(x, y, pixel);
        }

        /**
         * Fills the entire image with the given RGBA color.
         */
        public void fill(int rgba) {
                if (closed) throw new IllegalStateException("Image is closed");
                for (int i = 0; i < pixels.length; i++) {
                        pixels[i] = rgba;
                }
        }

        // ========== Texture Upload ==========

        /**
         * Uploads this image to the currently bound WebGL2 texture.
         * Converts the pixel buffer to a Uint8Array and calls texSubImage2D.
         */
        public void upload(int level, int offsetX, int offsetY, int x, int y, int width, int height) {
                if (closed) throw new IllegalStateException("Image is closed");
                upload0(level, offsetX, offsetY, x, y, width, height, false);
        }

        /**
         * Uploads this image to the currently bound WebGL2 texture with mipmap auto-generation.
         */
        public void upload(int level, int offsetX, int offsetY, int x, int y, int width, int height, boolean mipmap) {
                if (closed) throw new IllegalStateException("Image is closed");
                upload0(level, offsetX, offsetY, x, y, width, height, mipmap);
        }

        private void upload0(int level, int offsetX, int offsetY, int x, int y, int w, int h, boolean mipmap) {
                if (ClientMain.getWebGL2() == null) return;

                // Convert pixel data to RGBA byte array
                byte[] data = new byte[w * h * 4];
                for (int py = 0; py < h; py++) {
                        for (int px = 0; px < w; px++) {
                                int srcX = x + px;
                                int srcY = y + py;
                                int pixel = 0;
                                if (srcX >= 0 && srcX < this.width && srcY >= 0 && srcY < this.height) {
                                        pixel = pixels[srcY * this.width + srcX];
                                }
                                int idx = (py * w + px) * 4;
                                // MC stores as ABGR (0xAABBGGRR), WebGL expects RGBA byte order
                                data[idx]     = (byte) ((pixel >> 0)  & 0xFF); // R
                                data[idx + 1] = (byte) ((pixel >> 8)  & 0xFF); // G
                                data[idx + 2] = (byte) ((pixel >> 16) & 0xFF); // B
                                data[idx + 3] = (byte) ((pixel >> 24) & 0xFF); // A
                        }
                }

                // Upload to WebGL via PlatformOpenGL
                ArrayBuffer buffer = byteArrayToArrayBuffer(data);
                Uint8Array view = Uint8Array.create(buffer);
                PlatformOpenGL._wglPixelStorei(WebGL2RenderingContext.UNPACK_ROW_LENGTH, this.width);
                PlatformOpenGL._wglPixelStorei(WebGL2RenderingContext.UNPACK_SKIP_PIXELS, 0);
                PlatformOpenGL._wglPixelStorei(WebGL2RenderingContext.UNPACK_SKIP_ROWS, 0);
                PlatformOpenGL._wglTexSubImage2D(WebGL2RenderingContext.TEXTURE_2D, level, offsetX, offsetY,
                        w, h, WebGL2RenderingContext.RGBA, WebGL2RenderingContext.UNSIGNED_BYTE, view);

                if (mipmap) {
                        PlatformOpenGL._wglGenerateMipmap(WebGL2RenderingContext.TEXTURE_2D);
                }
        }

        // ========== Static Factory Methods ==========

        /**
         * Reads a NativeImage from an InputStream.
         * In the browser, this returns an empty 1x1 image.
         * Real image loading is handled by the EPK resource system.
         */
        public static NativeImage read(InputStream stream) {
                return new NativeImage(1, 1);
        }

        /**
         * Reads a NativeImage of the given format from an InputStream.
         */
        public static NativeImage read(InternalGlFormat format, InputStream stream) {
                return new NativeImage(format, 1, 1, false);
        }

        /**
         * Creates an empty NativeImage of the specified size.
         */
        public static NativeImage create(InternalGlFormat format, int width, int height) {
                return new NativeImage(format, width, height, false);
        }

        // ========== Lifecycle ==========

        @Override
        public void close() {
                closed = true;
                // In the browser, no native memory to free
        }

        public boolean isClosed() {
                return closed;
        }

        // ========== Utility ==========

        /**
         * Converts a Java byte array to a TeaVM ArrayBuffer.
         */
        private static ArrayBuffer byteArrayToArrayBuffer(byte[] arr) {
                ArrayBuffer buffer = ArrayBuffer.create(arr.length);
                Uint8Array view = Uint8Array.create(buffer);
                for (int i = 0; i < arr.length; i++) {
                        view.set(i, (short) (arr[i] & 0xFF));
                }
                return buffer;
        }
}
