package com.mojang.blaze3d.platform.cursor;

/**
 * EaglerCraft stub for CursorType.
 */
public enum CursorType {
    DEFAULT,
    HAND,
    CROSSHAIR,
    TEXT,
    RESIZE_VERTICAL,
    RESIZE_HORIZONTAL,
    RESIZE_UP_LEFT_DOWN_RIGHT,
    RESIZE_UP_RIGHT_DOWN_LEFT,
    MOVE,
    NOT_ALLOWED,
    WAIT,
    PROGRESS;

    public static CursorType createStandardCursor(int shape, String name, CursorType fallback) {
        return fallback != null ? fallback : DEFAULT;
    }
}
