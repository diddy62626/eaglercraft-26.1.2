package com.mojang.blaze3d.systems;

/**
 * EaglerCraft stub for GpuDevice.
 * Browser-side: backed by WebGL2 context.
 */
public interface GpuDevice extends GpuBackend {
    // Minimal interface for TeaVM compilation

    default java.util.List<String> getLastDebugMessages() {
        return new java.util.ArrayList<>();
    }
    default void addDebugMessage(String message) {}
    default void resetDebugMessages() {}

}
