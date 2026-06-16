package com.mojang.blaze3d.textures;

public abstract class GpuTexture {
    public int getWidth() { return 0; }
    public int getHeight() { return 0; }
    public int getDepth() { return 0; }
    public TextureFormat getFormat() { return TextureFormat.RGBA8; }
    public void close() {}
}
