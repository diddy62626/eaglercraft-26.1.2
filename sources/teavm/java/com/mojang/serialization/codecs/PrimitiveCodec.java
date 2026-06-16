package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public interface PrimitiveCodec<A> extends Codec<A> {
    default MapCodec<A> optionalFieldOf(String name) { return null; }
    default MapCodec<A> optionalFieldOf(String name, A defaultValue) { return null; }
}
