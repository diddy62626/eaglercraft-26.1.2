package com.mojang.blaze3d.systems;

import java.util.ArrayList;
import java.util.List;

/**
 * EaglerCraft stub for GpuDevice.
 * Browser-side: backed by WebGL2 context.
 */
public interface GpuDevice extends GpuBackend {

    /**
     * MC 26.1.2: Returns the list of enabled GPU extensions (WebGL2: empty).
     */
    default List<String> getEnabledExtensions() {
        return new ArrayList<>();
    }

    default List<String> getLastDebugMessages() {
        return new ArrayList<>();
    }
    default void addDebugMessage(String message) {}
    default void resetDebugMessages() {}

}
