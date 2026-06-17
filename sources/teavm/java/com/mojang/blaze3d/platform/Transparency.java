package com.mojang.blaze3d.platform;

/**
 * EaglerCraft stub for MC 26.1.2 NativeImage transparency classification.
 */
public final class Transparency {
    public static final Transparency OPAQUE = new Transparency("opaque");
    public static final Transparency FULL = new Transparency("full");
    public static final Transparency TRANSLUCENT = new Transparency("translucent");
    public static final Transparency TRANSPARENT = new Transparency("transparent");

    private final String name;
    public Transparency(String name) { this.name = name; }
    public String name() { return name; }

    public boolean hasTransparent() { return "transparent".equals(name); }
}
