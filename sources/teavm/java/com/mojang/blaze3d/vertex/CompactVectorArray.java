package com.mojang.blaze3d.vertex;

public class CompactVectorArray {
    private final int stride;
    private final int count;

    public CompactVectorArray(int stride, int count) {
        this.stride = stride;
        this.count = count;
    }

    public CompactVectorArray(int count) {
        this(12, count);
    }

    public int stride() { return stride; }
    public int count() { return count; }
    public java.nio.ByteBuffer buffer() { return java.nio.ByteBuffer.allocate(stride * count); }

    public void set(int index, float x, float y, float z) {}
    public float[] get(int index) { return new float[]{0, 0, 0}; }
}
