package com.mojang.blaze3d.platform;

/**
 * EaglerCraft 26.1.2 browser override for VideoMode.
 * Simple data class representing a screen video mode (resolution, refresh rate, bpp).
 * In the browser, we use window.screen dimensions as the "mode".
 */
public class VideoMode {

        private final int width;
        private final int height;
        private final int refreshRate;
        private final int bitsPerPixel;

        public VideoMode(int width, int height, int refreshRate, int bitsPerPixel) {
                this.width = width;
                this.height = height;
                this.refreshRate = refreshRate;
                this.bitsPerPixel = bitsPerPixel;
        }

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }

        public int getRefreshRate() {
                return refreshRate;
        }

        public int getBitsPerPixel() {
                return bitsPerPixel;
        }

        @Override
        public String toString() {
                return width + "x" + height + "@" + refreshRate + "Hz (" + bitsPerPixel + "bpp)";
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof VideoMode)) return false;
                VideoMode other = (VideoMode) obj;
                return width == other.width && height == other.height
                        && refreshRate == other.refreshRate && bitsPerPixel == other.bitsPerPixel;
        }

        @Override
        public int hashCode() {
                return 31 * (31 * (31 * width + height) + refreshRate) + bitsPerPixel;
        }

        /**
         * MC 26.1.2: Serializes the video mode to a string for storage.
         */
        public String write() {
                return width + "x" + height + "@" + refreshRate + "Hz," + bitsPerPixel + "bpp";
        }
}
