package com.mojang.blaze3d.systems;

public interface CommandEncoder {
    default void begin() {}
    default void end() {}
    default void submit() {}

    default void copyToBuffer(com.mojang.blaze3d.buffers.GpuBufferSlice src, com.mojang.blaze3d.buffers.GpuBufferSlice dst) {}
    default com.mojang.blaze3d.buffers.GpuFence createFence() {
        return new com.mojang.blaze3d.buffers.GpuFence() {};
    }
    default com.mojang.blaze3d.buffers.GpuBuffer.MappedView mapBuffer(com.mojang.blaze3d.buffers.GpuBuffer buffer, boolean read, boolean write) { return null; }
    default com.mojang.blaze3d.systems.GpuQuery timerQueryBegin() { return null; }
    default void timerQueryEnd(com.mojang.blaze3d.systems.GpuQuery query) {}
    default void writeToBuffer(com.mojang.blaze3d.buffers.GpuBufferSlice dst, java.nio.ByteBuffer src) {}
    default void writeToTexture(com.mojang.blaze3d.textures.GpuTexture texture, com.mojang.blaze3d.platform.NativeImage image) {}
    default void writeToTexture(com.mojang.blaze3d.textures.GpuTexture texture, com.mojang.blaze3d.platform.NativeImage image, int x, int y, int z, int w, int h, int d, int m) {}

    default void clearColorAndDepthTextures(com.mojang.blaze3d.textures.GpuTexture color, int colorValue, com.mojang.blaze3d.textures.GpuTexture depth, double depthValue) {}
    default void clearColorTexture(com.mojang.blaze3d.textures.GpuTexture texture, int value) {}
    default void clearDepthTexture(com.mojang.blaze3d.textures.GpuTexture texture, double value) {}

    default com.mojang.blaze3d.systems.RenderPass createRenderPass(java.util.function.Supplier<String> labelSupplier, com.mojang.blaze3d.textures.GpuTextureView colorAttachment, java.util.OptionalInt clearColor) {
        return new com.mojang.blaze3d.systems.RenderPass() {};
    }
    default void copyTextureToBuffer(com.mojang.blaze3d.textures.GpuTexture src, com.mojang.blaze3d.buffers.GpuBuffer dst, long offset, java.lang.Runnable callback, int flags) {}
    default com.mojang.blaze3d.buffers.GpuBuffer.MappedView mapBuffer(com.mojang.blaze3d.buffers.GpuBufferSlice slice, boolean read, boolean write) { return null; }

    default com.mojang.blaze3d.systems.RenderPass createRenderPass(java.util.function.Supplier<String> labelSupplier,
            com.mojang.blaze3d.textures.GpuTextureView colorAttachment, java.util.OptionalInt clearColor,
            com.mojang.blaze3d.textures.GpuTextureView depthAttachment, java.util.OptionalDouble clearDepth) {
        return new com.mojang.blaze3d.systems.RenderPass() {};
    }
    
    default void clearColorAndDepthTextures(com.mojang.blaze3d.textures.GpuTexture color, int colorValue, com.mojang.blaze3d.textures.GpuTexture depth, double depthValue, int x, int y, int w, int h) {}

    default void writeToTexture(com.mojang.blaze3d.textures.GpuTexture texture, com.mojang.blaze3d.platform.NativeImage image, int x, int y, int z, int w, int h, int d, int m, int s) {}
}
