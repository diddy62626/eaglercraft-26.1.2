package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;

public class DynamicUniforms {
    public void setGlintTransform(float x, float y, float z) {}

    public static class ChunkSectionInfo {
        public int x, y, z;
        public ChunkSectionInfo() {}
        public ChunkSectionInfo(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
    }

    public GpuBufferSlice[] writeChunkSections(ChunkSectionInfo[] sections) {
        return new GpuBufferSlice[0];
    }
}
