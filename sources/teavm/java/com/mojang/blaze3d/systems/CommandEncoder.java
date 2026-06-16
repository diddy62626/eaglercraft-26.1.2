package com.mojang.blaze3d.systems;

public interface CommandEncoder {
    default void begin() {}
    default void end() {}
    default void submit() {}

    default void copyToBuffer(com.mojang.blaze3d.buffers.GpuBufferSlice src, com.mojang.blaze3d.buffers.GpuBufferSlice dst) {}
    default com.mojang.blaze3d.buffers.GpuFence createFence() { return () -> false; }
    default com.mojang.blaze3d.buffers.GpuBuffer.MappedView mapBuffer(com.mojang.blaze3d.buffers.GpuBuffer buffer, boolean read, boolean write) { return null; }
    default com.mojang.blaze3d.systems.GpuQuery timerQueryBegin() { return null; }
    default void timerQueryEnd(com.mojang.blaze3d.systems.GpuQuery query) {}
    default void writeToBuffer(com.mojang.blaze3d.buffers.GpuBufferSlice dst, java.nio.ByteBuffer src) {}
    default void writeToTexture(com.mojang.blaze3d.textures.GpuTexture texture, com.mojang.blaze3d.platform.NativeImage image) {}
    default void writeToTexture(com.mojang.blaze3d.textures.GpuTexture texture, com.mojang.blaze3d.platform.NativeImage image, int x, int y, int z, int w, int h, int d, int m) {}
}
