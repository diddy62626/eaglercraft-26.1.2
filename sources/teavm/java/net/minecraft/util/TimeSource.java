package net.minecraft.util;

/**
 * EaglerCraft stub for net.minecraft.util.TimeSource.
 */
public interface TimeSource {
    interface NanoTimeSource extends TimeSource {
        long get();
    }
}
