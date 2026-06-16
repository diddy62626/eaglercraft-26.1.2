package com.mojang.blaze3d.textures;

public abstract class GpuTexture {
    private int width = 0;
    private int height = 0;
    private int depth = 0;

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDepth() { return depth; }
    public int getWidth(int level) { return Math.max(1, width >> level); }
    public int getHeight(int level) { return Math.max(1, height >> level); }
    public TextureFormat getFormat() { return TextureFormat.RGBA8; }
    public void close() {}
}
