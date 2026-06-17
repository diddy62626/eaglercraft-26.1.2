package com.mojang.blaze3d.textures;

public enum TextureFormat {
    RGBA8, RGB8, BGRA8, R8, RG8, R16F, RG16F, RGBA16F, R32F, RG32F, RGBA32F,
    DEPTH_COMPONENT, DEPTH_STENCIL, RED, GREEN, BLUE, ALPHA,
    RED8, RED8I, RED8UI, RED16, RED16F, RG16, RGB16F, RGBA32I,
    DEPTH24_STENCIL8, DEPTH32F_STENCIL8, DEPTH_COMPONENT32F, DEPTH32, DEPTH16, DEPTH24;

    public int pixelSize() { return 4; }
    public boolean isCompressed() { return false; }
}
