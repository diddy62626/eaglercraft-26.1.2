package com.mojang.blaze3d.platform;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformInput;

/**
 * EaglerCraft 26.1.2 browser override for ClipboardManager.
 * Uses the browser's async Clipboard API (navigator.clipboard)
 * or falls back to synchronous document.execCommand.
 */
public class ClipboardManager {

        public ClipboardManager() {
        }

        /**
         * Gets the current clipboard text.
         * Uses the browser's async Clipboard API if available,
         * otherwise falls back to a synchronous prompt.
         *
         * Note: The async API returns a Promise, but MC expects synchronous access.
         * We use the PlatformInput cached clipboard value which is updated
         * asynchronously via the clipboard API.
         *
         * @return The clipboard text, or empty string if unavailable
         */
        public String getClipboard() {
                return PlatformInput.getClipboardText();
        }

        /**
         * Sets the clipboard text.
         * Uses the browser's async Clipboard API if available,
         * otherwise falls back to a hidden textarea trick.
         *
         * @param text The text to copy to the clipboard
         */
        public void setClipboard(String text) {
                if (text == null) return;
                PlatformInput.setClipboardText(text);
        }
}
