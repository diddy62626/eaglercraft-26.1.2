package com.mojang.blaze3d.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;

/**
 * EaglerCraft stub for GpuDevice.
 * Browser-side: backed by WebGL2 context.
 */
public interface GpuDevice extends GpuBackend {

    /**
     * MC 26.1.2: Returns the list of enabled GPU extensions (WebGL2: empty).
     */
    default List<String> getEnabledExtensions() {
        return new ArrayList<>();
    }

    /**
     * MC 26.1.2: Returns the max supported texture size.
     */
    default int getMaxTextureSize() {
        return 16384;
    }

    /**
     * MC 26.1.2: Returns the renderer string.
     */
    default String getRenderer() {
        return "WebGL2";
    }

    /**
     * MC 26.1.2: Returns whether GPU debugging is enabled.
     */
    default boolean isDebuggingEnabled() {
        return false;
    }

    /**
     * MC 26.1.2: Creates a command encoder for recording GPU commands.
     */
    default CommandEncoder createCommandEncoder() {
        return new CommandEncoder() {};
    }

    /**
     * MC 26.1.2: Creates a new GPU texture.
     */
    default GpuTexture createTexture(Supplier<String> labelSupplier, int usage,
                                     TextureFormat format, int width, int height, int depth, int mipLevels) {
        return new GpuTexture() {};
    }

    default List<String> getLastDebugMessages() {
        return new ArrayList<>();
    }
    default void addDebugMessage(String message) {}
    default void resetDebugMessages() {}


    default com.mojang.blaze3d.buffers.GpuBuffer createBuffer(java.util.function.Supplier<String> label, int usage, long size) { return new com.mojang.blaze3d.buffers.GpuBuffer() {}; }
    default com.mojang.blaze3d.buffers.GpuBuffer createBuffer(java.util.function.Supplier<String> label, int usage, java.nio.ByteBuffer data) { return new com.mojang.blaze3d.buffers.GpuBuffer() {}; }
    default com.mojang.blaze3d.textures.GpuTexture createTexture(String label, int usage, com.mojang.blaze3d.textures.TextureFormat format, int w, int h, int d, int levels) { return new com.mojang.blaze3d.textures.GpuTexture() {}; }
    default com.mojang.blaze3d.textures.GpuTextureView createTextureView(com.mojang.blaze3d.textures.GpuTexture texture) { return new com.mojang.blaze3d.textures.GpuTextureView(texture); }
    default String getBackendName() { return "WebGL2"; }
    default int getMaxSupportedAnisotropy() { return 0; }
    default int getUniformOffsetAlignment() { return 256; }
    default boolean isZZeroToOne() { return false; }

    default com.mojang.blaze3d.pipeline.CompiledRenderPipeline precompilePipeline(com.mojang.blaze3d.pipeline.RenderPipeline pipeline, com.mojang.blaze3d.shaders.ShaderSource source) { return null; }

    default com.mojang.blaze3d.textures.GpuSampler createSampler(com.mojang.blaze3d.textures.AddressMode u, com.mojang.blaze3d.textures.AddressMode v,
            com.mojang.blaze3d.textures.FilterMode min, com.mojang.blaze3d.textures.FilterMode mag, int maxAnisotropy, java.util.OptionalDouble maxLod) {
        return new com.mojang.blaze3d.textures.GpuSampler();
    }
}
