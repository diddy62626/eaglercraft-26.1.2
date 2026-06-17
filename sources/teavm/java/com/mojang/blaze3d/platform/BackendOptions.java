package com.mojang.blaze3d.platform;

/**
 * EaglerCraft stub for BackendOptions.
 * Browser-side: default options for WebGL2.
 */
public class BackendOptions {
    private final boolean async;

    public BackendOptions() {
        this(false);
    }

    public BackendOptions(boolean async) {
        this.async = async;
    }

    public boolean isAsync() {
        return async;
    }

    public static BackendOptions defaultInstance() {
        return new BackendOptions();
    }
}
