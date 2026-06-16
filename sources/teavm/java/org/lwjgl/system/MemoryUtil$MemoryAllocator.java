package org.lwjgl.system;

public interface MemoryUtil$MemoryAllocator {
    long malloc(long size);
    long calloc(long num, long size);
    long realloc(long ptr, long size);
    void free(long ptr);
    long aligned_alloc(long alignment, long size);
    void aligned_free(long ptr);
}
