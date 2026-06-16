package com.mojang.blaze3d.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;

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

    /**
     * MC 26.1.2: Returns the max supported texture size.
     */
    default int getMaxTextureSize() {
        return 16384;
    }

    /**
     * MC 26.1.2: Returns the renderer string.
     */
    default String getRenderer() {
        return "WebGL2";
    }

    /**
     * MC 26.1.2: Returns whether GPU debugging is enabled.
     */
    default boolean isDebuggingEnabled() {
        return false;
    }

    /**
     * MC 26.1.2: Creates a command encoder for recording GPU commands.
     */
    default CommandEncoder createCommandEncoder() {
        return new CommandEncoder() {};
    }

    /**
     * MC 26.1.2: Creates a new GPU texture.
     */
    default GpuTexture createTexture(Supplier<String> labelSupplier, int usage,
                                     TextureFormat format, int width, int height, int depth, int mipLevels) {
        return new GpuTexture() {};
    }

    default List<String> getLastDebugMessages() {
        return new ArrayList<>();
    }
    default void addDebugMessage(String message) {}
    default void resetDebugMessages() {}

}
