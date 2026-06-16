package com.mojang.blaze3d.buffers;

public interface GpuFence {
    default void waitFence() {}
    default boolean awaitCompletion(long timeoutNanos) { return true; }
    default boolean isSignaled() { return false; }
    default void close() {}
}
