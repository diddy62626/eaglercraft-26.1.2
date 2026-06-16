package com.mojang.blaze3d.systems;

public interface CommandEncoder {
    default void begin() {}
    default void end() {}
    default void submit() {}
}
