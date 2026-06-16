package com.mojang.blaze3d.platform;

/**
 * EaglerCraft stub for WindowEventHandler.
 */
public interface WindowEventHandler {
    void windowResized();
    void cursorEntered();
    void cursorLeft();
    void updateVsync(boolean vsync);
    default void onClose() {}
}
