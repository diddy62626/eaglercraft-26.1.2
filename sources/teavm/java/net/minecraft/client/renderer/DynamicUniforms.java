package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;

public class DynamicUniforms {
    public void setGlintTransform(float x, float y, float z) {}

    public static class ChunkSectionInfo {
        public int x, y, z;
        public int sectionX, sectionY, sectionZ;
        public float partialTick;
        public int renderTypeIndex;
        public int index;

        public ChunkSectionInfo() {}
        public ChunkSectionInfo(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
        public ChunkSectionInfo(org.joml.Matrix4fc transform, int sx, int sy, int sz, float partialTick, int renderTypeIndex, int index) {
            this.sectionX = sx; this.sectionY = sy; this.sectionZ = sz;
            this.partialTick = partialTick;
            this.renderTypeIndex = renderTypeIndex;
            this.index = index;
        }
    }

    public GpuBufferSlice[] writeChunkSections(ChunkSectionInfo[] sections) {
        return new GpuBufferSlice[0];
    }

    public GpuBufferSlice writeTransform(org.joml.Matrix4fc transform, org.joml.Vector4fc offset, org.joml.Vector3fc normalOffset, org.joml.Matrix4fc projMat) {
        return new GpuBufferSlice(new com.mojang.blaze3d.buffers.GpuBuffer() {}, 0, 64);
    }
}
