package com.mojang.blaze3d.buffers;

public abstract class GpuBuffer {
    public long size() { return 0L; }
    public void close() {}

    public interface MappedView {
        java.nio.ByteBuffer data();
        void unmap();
    }
}
