package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

/**
 * EaglerCraft stub for PrimitiveCodec.
 *
 * Extends Codec but provides optionalFieldOf returning MapCodec (which is
 * now standalone, so no name clash).
 */
public interface PrimitiveCodec<A> extends Codec<A> {
    default MapCodec<A> optionalFieldOf(String name) { return null; }
    default MapCodec<A> optionalFieldOf(String name, A defaultValue) { return null; }
}
