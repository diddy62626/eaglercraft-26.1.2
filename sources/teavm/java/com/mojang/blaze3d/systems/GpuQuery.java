package com.mojang.blaze3d.systems;

public interface GpuQuery {
    default void begin() {}
    default void end() {}
    default long getValue() { return 0L; }
    default boolean isAvailable() { return false; }
    default void close() {}
    default java.util.OptionalLong getValueOptional() { return java.util.OptionalLong.empty(); }
}
