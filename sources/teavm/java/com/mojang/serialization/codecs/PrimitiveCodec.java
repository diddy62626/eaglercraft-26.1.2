package com.mojang.serialization.codecs;

import com.mojang.serialization.MapCodec;

/**
 * EaglerCraft stub for PrimitiveCodec.
 *
 * STANDALONE interface — does NOT extend Codec.
 * This allows optionalFieldOf(String) to return MapCodec without
 * clashing with Codec's version that returns Codec<Optional<T>>.
 *
 * MC code that uses PrimitiveCodec as a Codec will get a ClassCastException
 * at runtime, but TeaVM compilation will succeed.
 */
public interface PrimitiveCodec<A> {
    A decode(Object input);
    Object encode(A value);

    default MapCodec<A> optionalFieldOf(String name) { return null; }
    default MapCodec<A> optionalFieldOf(String name, A defaultValue) { return null; }
}
