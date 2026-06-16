package com.mojang.blaze3d.platform;

/**
 * Stub for NativeImage.Format enum (MC 26.1.2 inner class).
 * Placed as top-level because inner class refs require outer class file
 * to compile first.
 */
public enum NativeImageFormat {
    RGBA(4, true, true, false, false),
    RGB(3, true, true, false, false),
    LUMINANCE_ALPHA(2, true, true, false, false),
    LUMINANCE(1, true, false, false, false);

    public final int components;
    public final boolean hasAlpha;
    public final boolean hasLuminance;
    public final boolean hasTransparent;
    public final boolean supportedByWebGL;

    NativeImageFormat(int components, boolean hasAlpha, boolean hasLuminance, boolean hasTransparent, boolean supportedByWebGL) {
        this.components = components;
        this.hasAlpha = hasAlpha;
        this.hasLuminance = hasLuminance;
        this.hasTransparent = hasTransparent;
        this.supportedByWebGL = supportedByWebGL;
    }
}
