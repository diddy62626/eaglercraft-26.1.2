package com.mojang.blaze3d.systems;

public interface GpuQuery {
    default void begin() {}
    default void end() {}
    default java.util.OptionalLong getValue() { return java.util.OptionalLong.empty(); }
    default long getValueAsLong() { return 0L; }
    default boolean isAvailable() { return false; }
    default void close() {}
}
