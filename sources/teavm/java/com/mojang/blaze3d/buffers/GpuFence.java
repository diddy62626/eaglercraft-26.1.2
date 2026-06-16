package com.mojang.blaze3d.buffers;

public interface GpuFence {
    void waitFence() {}
    boolean isSignaled() { return false; }
}
