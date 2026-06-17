package com.mojang.blaze3d.shaders;

/**
 * EaglerCraft stub for MC 26.1.2 GpuDebugOptions.
 *
 * Carries debug options for GPU device creation (verbose logging,
 * shader validation, etc.). Browser: all options default off.
 *
 * MC 26.1.2 has multiple constructors:
 *   GpuDebugOptions()
 *   GpuDebugOptions(int verbosity, boolean enabled, boolean strictMode)
 */
public final class GpuDebugOptions {
    private boolean enabled = false;
    private int verbosity = 0;
    private boolean strictMode = false;

    public GpuDebugOptions() {}

    public GpuDebugOptions(int verbosity, boolean enabled, boolean strictMode) {
        this.verbosity = verbosity;
        this.enabled = enabled;
        this.strictMode = strictMode;
    }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getVerbosity() { return verbosity; }
    public boolean isStrictMode() { return strictMode; }

    public static GpuDebugOptions DEFAULT = new GpuDebugOptions();
}
