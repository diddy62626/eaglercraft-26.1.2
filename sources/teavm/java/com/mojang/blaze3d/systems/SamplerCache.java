package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;

/**
 * EaglerCraft stub for SamplerCache.
 *
 * Caches GPU sampler states. In MC 26.1.2, getSampler(...) returns a
 * GpuSampler based on address/filter modes. Browser: returns a stub.
 */
public class SamplerCache {

    private static final GpuSampler STUB_SAMPLER = new GpuSampler();

    /**
     * MC 26.1.2: Returns a cached sampler matching the given modes.
     */
    public GpuSampler getSampler(AddressMode u, AddressMode v,
                                 FilterMode min, FilterMode mag,
                                 boolean mipmaps) {
        return STUB_SAMPLER;
    }

    public com.mojang.blaze3d.textures.GpuSampler getClampToEdge(com.mojang.blaze3d.textures.FilterMode filter) {
        return new com.mojang.blaze3d.textures.GpuSampler();
    }
    public com.mojang.blaze3d.textures.GpuSampler getRepeat(com.mojang.blaze3d.textures.FilterMode filter) {
        return new com.mojang.blaze3d.textures.GpuSampler();
    }
}
