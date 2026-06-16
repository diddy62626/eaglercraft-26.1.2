package com.mojang.blaze3d.textures;

/**
 * EaglerCraft stub for MC 26.1.2 AddressMode.
 *
 * Controls texture addressing (wrap/clamp/mirror) on U/V coordinates.
 * Browser: WebGL2 supports these but we just need the enum for compile.
 */
public enum AddressMode {
    REPEAT,
    MIRRORED_REPEAT,
    CLAMP_TO_EDGE,
    CLAMP_TO_BORDER;
}
