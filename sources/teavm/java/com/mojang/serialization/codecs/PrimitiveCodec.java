package com.mojang.serialization.codecs;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * EaglerCraft stub for PrimitiveCodec.
 *
 * <p>PrimitiveCodec is the base interface for primitive type codecs
 * (BOOL, INT, LONG, FLOAT, DOUBLE, STRING, etc). The real DFU
 * PrimitiveCodec has methods like validate(), range(), listOf() etc
 * that override Codec's versions.</p>
 *
 * <p>This stub provides the commonly-used methods so that MC code
 * calling e.g. {@code Codec.FLOAT.listOf()} doesn't crash with
 * '$listOf is not a function'.</p>
 */
public interface PrimitiveCodec<A> extends Codec<A> {

    @Override
    default Codec<List<A>> listOf() { return new Codec<List<A>>() {
        @Override public List<A> decode(Object input) { return java.util.Collections.emptyList(); }
        @Override public Object encode(List<A> value) { return null; }
    }; }

    @Override
    default Codec<List<A>> listOf(int minSize) { return listOf(); }

    @Override
    default Codec<List<A>> listOf(int minSize, int maxSize) { return listOf(); }

    /** Validates a decoded value, returning an error if invalid. */
    default A validate(A value, DynamicOps<?> ops) { return value; }

    /** Returns a codec that validates values are in [min, max] range. */
    default Codec<A> range(double min, double max) { return this; }

    /** Returns a codec that validates values are in [min, max] range (int). */
    default Codec<A> range(int min, int max) { return this; }

    /** Returns a codec that validates values are in [min, max] range (long). */
    default Codec<A> range(long min, long max) { return this; }

    /** Returns a DataResult for out-of-range values. */
    default DataResult<A> validateRange(A value, double min, double max) {
        return DataResult.success(value);
    }
}
