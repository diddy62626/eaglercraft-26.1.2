package net.minecraft.util;

/**
 * EaglerCraft stub for net.minecraft.util.TimeSource.
 * Real MC has NanoTimeSource.getAsLong() (LongSupplier-style).
 */
public interface TimeSource {
    interface NanoTimeSource extends TimeSource, java.util.function.LongSupplier {
        @Override
        long getAsLong();
    }

    default long get(java.util.concurrent.TimeUnit unit) {
        return unit.convert(System.currentTimeMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
