package com.mojang.blaze3d.textures;

public class GpuTextureView {
    private final GpuTexture texture;

    public GpuTextureView(GpuTexture texture) {
        this.texture = texture;
    }

    public GpuTexture texture() { return texture; }
    public void close() {}

    public int getWidth(int level) { return texture.getWidth() >> level; }

    public int getHeight(int level) { return texture.getHeight() >> level; }
}
