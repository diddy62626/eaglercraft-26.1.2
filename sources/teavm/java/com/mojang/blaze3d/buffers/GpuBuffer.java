package com.mojang.blaze3d.buffers;

public abstract class GpuBuffer {
    public long size() { return 0L; }
    public void close() {}

    public GpuBufferSlice slice() { return new GpuBufferSlice(this, 0, 0); }
    public GpuBufferSlice slice(long offset, long size) { return new GpuBufferSlice(this, offset, size); }

    public interface MappedView {
        java.nio.ByteBuffer data();
        void unmap();
        default void close() { unmap(); }
    }

    public int usage() { return 0; }
}
