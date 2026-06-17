package com.mojang.blaze3d.platform;

/**
 * EaglerCraft 26.1.2 browser override for FramerateLimitTracker.
 * Tracks and reports the current framerate limit.
 * In the browser, the limit is controlled by requestAnimationFrame (vsync)
 * and the Window's framerateLimit setting.
 */
public class FramerateLimitTracker {

        /** Default framerate limit when no window is available. */
        private static final int DEFAULT_FRAMERATE_LIMIT = 260;

        /** Reference to the game window. */
        private Window window;

        public FramerateLimitTracker() {
        }

        public void setWindow(Window window) {
                this.window = window;
        }

        /**
         * Returns the current framerate limit.
         * If the window has a limit set, uses that; otherwise defaults to 260.
         *
         * @return The framerate limit in FPS
         */
        public int getFramerateLimit() {
                if (window != null) {
                        return window.getFramerateLimit();
                }
                return DEFAULT_FRAMERATE_LIMIT;
        }

        /**
         * Returns whether the framerate limit is below the maximum (260 FPS).
         * If true, the game should use vsync or frame pacing.
         *
         * @return true if the limit is below 260
         */
        public boolean isFramerateLimitBelowMax() {
                return getFramerateLimit() < DEFAULT_FRAMERATE_LIMIT;
        }

        /**
         * Returns whether the framerate should be limited this frame.
         *
         * @return true if framerate limiting is active
         */
        public boolean isFramerateLimited() {
                return getFramerateLimit() > 0 && getFramerateLimit() < DEFAULT_FRAMERATE_LIMIT;
        }

    public FramerateLimitTracker(net.minecraft.client.Options options, net.minecraft.client.Minecraft minecraft) {
        // Browser stub: real tracker needs the Minecraft instance
    }

    /**
     * MC 26.1.2: Sets the framerate limit.
     */
    public void setFramerateLimit(int limit) {
        if (window != null) {
            window.setFramerateLimit(limit);
        }
    }
}
