package com.mojang.blaze3d.buffers;

public abstract class GpuBuffer {
    public long size() { return 0L; }
    public void close() {}

    public interface MappedView {
        java.nio.ByteBuffer data();
        void unmap();
    }

    public com.mojang.blaze3d.buffers.GpuBufferSlice slice(long offset, long size) {
        return new com.mojang.blaze3d.buffers.GpuBufferSlice(this, offset, size);
    }
}
