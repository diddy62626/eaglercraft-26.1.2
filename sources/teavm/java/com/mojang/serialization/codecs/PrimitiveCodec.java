package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.function.Function;

/**
 * EaglerCraft stub for PrimitiveCodec.
 *
 * STANDALONE interface — does NOT extend Codec.
 * This allows optionalFieldOf(String) to return MapCodec without
 * clashing with Codec's version that returns Codec<Optional<T>>.
 *
 * All Codec methods that MC code calls on PrimitiveCodec are provided
 * as default methods returning null/this.
 */
public interface PrimitiveCodec<A> {
    A decode(Object input);
    Object encode(A value);

    // Codec methods that MC expects (since PrimitiveCodec extends Codec in real MC)
    default <S> Codec<S> comapFlatMap(Function<A, DataResult<S>> fn, Function<S, A> inverse) { return null; }
    default <S> Codec<S> flatXmap(Function<A, DataResult<S>> to, Function<S, DataResult<A>> from) { return null; }
    default <S> Codec<S> xmap(Function<A, S> to, Function<S, A> from) { return null; }
    default <S> Codec<S> flatComapMap(Function<S, DataResult<A>> decoder, Function<A, S> encoder) { return null; }
    default <S> Codec<S> comap(Function<S, A> fn) { return null; }
    default <S> Codec<S> flatComap(Function<A, DataResult<S>> fn) { return null; }
    default Codec<A> listOf() { return null; }
    default Codec<A> sizeLimitedListOf(int maxSize) { return null; }
    default Codec<A> validate(Function<A, DataResult<A>> validator) { return null; }
    default MapCodec<A> fieldOf(String name) { return null; }

    // The method that caused the original clash — now works since we don't extend Codec
    default MapCodec<A> optionalFieldOf(String name) { return null; }
    default MapCodec<A> optionalFieldOf(String name, A defaultValue) { return null; }
}
