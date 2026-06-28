package com.mojang.datafixers.types.families;
import com.mojang.datafixers.types.Type;
public interface TypeFamily {
    Type<?> apply(int index);
    default int size() { return Integer.MAX_VALUE; }
}
