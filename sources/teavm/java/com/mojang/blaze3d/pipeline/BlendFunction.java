package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.platform.DestFactor;

public class BlendFunction {
    // All known MC 26.1.2 blend functions as static fields
    public static final BlendFunction NONE = new BlendFunction();
    public static final BlendFunction TRANSPARENT = new BlendFunction();
    public static final BlendFunction TRANSLUCENT = new BlendFunction();
    public static final BlendFunction ADD = new BlendFunction();
    public static final BlendFunction ADDITIVE = new BlendFunction();
    public static final BlendFunction SUBTRACT = new BlendFunction();
    public static final BlendFunction MULTIPLY = new BlendFunction();
    public static final BlendFunction SCREEN = new BlendFunction();
    public static final BlendFunction OVERLAY = new BlendFunction();
    public static final BlendFunction GLINT = new BlendFunction();
    public static final BlendFunction LIGHTNING = new BlendFunction();
    public static final BlendFunction ENCHANTMENT = new BlendFunction();
    public static final BlendFunction END_PORTAL = new BlendFunction();
    public static final BlendFunction OUTLINE = new BlendFunction();
    public static final BlendFunction FOG = new BlendFunction();
    public static final BlendFunction PARTICLES = new BlendFunction();
    public static final BlendFunction CLOUDS = new BlendFunction();
    public static final BlendFunction RAIN_SNOW = new BlendFunction();
    public static final BlendFunction DESTROY = new BlendFunction();
    public static final BlendFunction BEACON_BEAM = new BlendFunction();
    public static final BlendFunction INVERT = new BlendFunction();
    public static final BlendFunction SPIDER_EYES = new BlendFunction();
    public static final BlendFunction WATER_MASK = new BlendFunction();

    public BlendFunction() {}

    public BlendFunction(SourceFactor srcColor, DestFactor dstColor, SourceFactor srcAlpha, DestFactor dstAlpha) {}
}
