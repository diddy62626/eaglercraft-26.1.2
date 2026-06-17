package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;

/**
 * EaglerCraft stub for PrimitiveCodec.
 *
 * Extends Codec but does NOT override optionalFieldOf (which would clash
 * with Codec's version returning Codec<Optional<T>>). The TeaVM plugin
 * handles the return type difference at IR level.
 */
public interface PrimitiveCodec<A> extends Codec<A> {
}
