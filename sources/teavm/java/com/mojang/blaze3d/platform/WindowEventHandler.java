package com.mojang.blaze3d.platform;

/**
 * EaglerCraft stub for WindowEventHandler.
 */
public interface WindowEventHandler {
    void resizeDisplay();
    default void cursorEntered() {}
    default void cursorLeft() {}
    default void updateVsync(boolean vsync) {}
    default void onClose() {}
    default void windowResized() {}
}
