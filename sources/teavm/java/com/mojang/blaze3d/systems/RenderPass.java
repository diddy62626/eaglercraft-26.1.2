package com.mojang.blaze3d.systems;

public interface RenderPass {
    default void close() {}
    default void setPipeline(com.mojang.blaze3d.pipeline.CompiledRenderPipeline pipeline) {}
    default void setPipeline(com.mojang.blaze3d.pipeline.RenderPipeline pipeline) {}
    default void setVertexBuffer(int slot, com.mojang.blaze3d.buffers.GpuBufferSlice slice) {}
    default void setVertexBuffer(int slot, com.mojang.blaze3d.buffers.GpuBuffer buffer) {}
    default void setIndexBuffer(com.mojang.blaze3d.buffers.GpuBufferSlice slice, com.mojang.blaze3d.vertex.VertexFormat.IndexType indexType) {}
    default void setIndexBuffer(com.mojang.blaze3d.buffers.GpuBuffer buffer, com.mojang.blaze3d.vertex.VertexFormat.IndexType indexType) {}
    default void drawIndexed(int indexCount) {}
    default void drawIndexed(int baseVertex, int indexCount, int startIndex) {}
    default void draw(int vertexCount) {}
    default void bindSampler(String samplerName, com.mojang.blaze3d.textures.GpuTextureView view) {}
    default void bindSampler(String samplerName, com.mojang.blaze3d.textures.GpuSampler sampler) {}
    default void bindTexture(String name, com.mojang.blaze3d.textures.GpuTextureView view, com.mojang.blaze3d.textures.GpuSampler sampler) {}
    default void bindTexture(String name, com.mojang.blaze3d.textures.GpuTextureView view) {}
    default void setUniform(String uniformName, com.mojang.blaze3d.buffers.GpuBufferSlice slice) {}
    default void setUniform(String uniformName, java.nio.ByteBuffer data) {}
    default void enableScissor(int x, int y, int width, int height) {}
    default void disableScissor() {}

    default void drawIndexed(int baseVertex, int indexCount, int startIndex, int instanceCount) {}
    default void drawMultipleIndexed(java.util.Collection<?> draws, com.mojang.blaze3d.buffers.GpuBuffer indexBuffer,
            com.mojang.blaze3d.vertex.VertexFormat.IndexType indexType, java.util.Collection<?> params, Object multiDrawData) {}
    default void setUniform(String name, com.mojang.blaze3d.buffers.GpuBuffer buffer) {}

    default void draw(int vertexCount, int instanceCount) {}
}
