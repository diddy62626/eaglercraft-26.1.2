package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.platform.DestFactor;

/**
 * EaglerCraft stub for MC 26.1.2 BlendFunction.
 * Contains ALL known blend function constants from MC 26.1.2.
 */
public class BlendFunction {
    // Standard blend functions
    public static final BlendFunction NONE = new BlendFunction();
    public static final BlendFunction TRANSPARENT = new BlendFunction();
    public static final BlendFunction TRANSLUCENT = new BlendFunction();
    public static final BlendFunction TRANSLUCENT_PREMULTIPLIED_ALPHA = new BlendFunction();
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
    public static final BlendFunction SPIDER_EYES = new BlendFunction();
    public static final BlendFunction WATER_MASK = new BlendFunction();
    public static final BlendFunction INVERT = new BlendFunction();

    // Render pipeline specific blend functions
    public static final BlendFunction ENTITY_OUTLINE_BLIT = new BlendFunction();
    public static final BlendFunction ENTITY_GLINT = new BlendFunction();
    public static final BlendFunction ENTITY_GLINT_DIRECT = new BlendFunction();
    public static final BlendFunction GLINT_DIRECT = new BlendFunction();
    public static final BlendFunction ARMOR_GLINT = new BlendFunction();
    public static final BlendFunction ARMOR_ENTITY_GLINT = new BlendFunction();
    public static final BlendFunction CRUMBLING = new BlendFunction();
    public static final BlendFunction CUTOUT = new BlendFunction();
    public static final BlendFunction CUTOUT_MIPPED = new BlendFunction();
    public static final BlendFunction SOLID = new BlendFunction();
    public static final BlendFunction TRANSLUCENT_MOVING_BLOCK = new BlendFunction();
    public static final BlendFunction TRANSLUCENT_NO_CULL = new BlendFunction();
    public static final BlendFunction TRIPWIRE = new BlendFunction();
    public static final BlendFunction EYES = new BlendFunction();
    public static final BlendFunction ENERGY_SWEEP = new BlendFunction();
    public static final BlendFunction LEASH = new BlendFunction();
    public static final BlendFunction BARRIER = new BlendFunction();
    public static final BlendFunction LIGHTNING_BOLT = new BlendFunction();
    public static final BlendFunction END_GATEWAY = new BlendFunction();
    public static final BlendFunction END_CRYSTAL = new BlendFunction();
    public static final BlendFunction DRAGON = new BlendFunction();
    public static final BlendFunction SHULKER_BOX = new BlendFunction();
    public static final BlendFunction WATER = new BlendFunction();
    public static final BlendFunction LAVA = new BlendFunction();
    public static final BlendFunction PORTAL = new BlendFunction();
    public static final BlendFunction FIRE = new BlendFunction();
    public static final BlendFunction REDSTONE = new BlendFunction();
    public static final BlendFunction EXPERIENCE_ORB = new BlendFunction();
    public static final BlendFunction ITEM_FRAME = new BlendFunction();
    public static final BlendFunction FISHING_LINE = new BlendFunction();
    public static final BlendFunction LEASH_KNOT = new BlendFunction();

    public BlendFunction() {}

    public BlendFunction(SourceFactor srcColor, DestFactor dstColor) {}

    public BlendFunction(SourceFactor srcColor, DestFactor dstColor, SourceFactor srcAlpha, DestFactor dstAlpha) {}
}
