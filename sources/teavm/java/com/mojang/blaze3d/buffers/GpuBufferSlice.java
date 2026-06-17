package com.mojang.blaze3d.buffers;

public class GpuBufferSlice {
    private final GpuBuffer buffer;
    private final long offset;
    private final long size;

    public GpuBufferSlice(GpuBuffer buffer, long offset, long size) {
        this.buffer = buffer;
        this.offset = offset;
        this.size = size;
    }

    public GpuBuffer buffer() { return buffer; }
    public long offset() { return offset; }
    public long size() { return size; }
}
