package com.mojang.blaze3d.platform;

import java.util.OptionalInt;

/**
 * EaglerCraft 26.1.2 browser override for DisplayData.
 * Simple data class holding initial display/window size preferences.
 * In the browser, most of these are irrelevant since the canvas fills the container.
 */
public class DisplayData {

        private final int width;
        private final int height;
        private final int fullscreenWidth;
        private final int fullscreenHeight;
        private final boolean isFullscreen;

        public DisplayData(int width, int height, int fullscreenWidth, int fullscreenHeight, boolean isFullscreen) {
                this.width = width;
                this.height = height;
                this.fullscreenWidth = fullscreenWidth;
                this.fullscreenHeight = fullscreenHeight;
                this.isFullscreen = isFullscreen;
        }

        /**
         * MC 26.1.2 constructor with OptionalInt for fullscreen dimensions.
         * If OptionalInt is empty, fullscreen dimensions default to 0.
         */
        public DisplayData(int width, int height, OptionalInt fullscreenWidth, OptionalInt fullscreenHeight, boolean isFullscreen) {
                this.width = width;
                this.height = height;
                this.fullscreenWidth = fullscreenWidth != null ? fullscreenWidth.orElse(0) : 0;
                this.fullscreenHeight = fullscreenHeight != null ? fullscreenHeight.orElse(0) : 0;
                this.isFullscreen = isFullscreen;
        }

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }

        public int getFullscreenWidth() {
                return fullscreenWidth;
        }

        public int getFullscreenHeight() {
                return fullscreenHeight;
        }

        public boolean isFullscreen() {
                return isFullscreen;
        }

    public DisplayData withFullscreen(boolean fullscreen) {
        return new DisplayData(width, height, fullscreenWidth, fullscreenHeight, fullscreen);
    }

    public DisplayData withSize(int width, int height) {
        return new DisplayData(width, height, fullscreenWidth, fullscreenHeight, isFullscreen);
    }
}
