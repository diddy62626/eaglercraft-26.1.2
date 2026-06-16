package com.mojang.blaze3d.systems;

/**
 * EaglerCraft stub for GpuBackend.
 * Browser-side: backed by WebGL2.
 */
public interface GpuBackend {
    default boolean isDisplay() { return false; }
    default boolean isVulkan() { return false; }
    default boolean isMetal() { return false; }
    default boolean isOpenGL() { return true; }
    default boolean isWebGPU() { return false; }
    String getName();
    String getVendor();
    String getVersion();
}
