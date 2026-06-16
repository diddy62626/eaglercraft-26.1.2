package com.mojang.blaze3d.buffers;

public interface GpuFence {
    default void waitFence() {}
    default boolean isSignaled() { return false; }
}
