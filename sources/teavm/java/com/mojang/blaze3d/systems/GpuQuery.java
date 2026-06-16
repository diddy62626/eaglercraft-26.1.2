package com.mojang.blaze3d.systems;

public interface GpuQuery {
    void begin();
    void end();
    long getValue();
    boolean isAvailable();
}
