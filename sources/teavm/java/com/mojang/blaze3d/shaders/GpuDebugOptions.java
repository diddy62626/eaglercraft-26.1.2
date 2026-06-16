package com.mojang.blaze3d.shaders;

/**
 * EaglerCraft stub for MC 26.1.2 GpuDebugOptions.
 *
 * Carries debug options for GPU device creation (verbose logging,
 * shader validation, etc.). Browser: all options default off.
 */
public final class GpuDebugOptions {
    private boolean enabled = false;

    public GpuDebugOptions() {}

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public static GpuDebugOptions DEFAULT = new GpuDebugOptions();
}
