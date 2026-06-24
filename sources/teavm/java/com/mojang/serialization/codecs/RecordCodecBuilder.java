package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.function.Function;

// PATCHED: Returns no-op Codec/MapCodec instances instead of null.

public class RecordCodecBuilder {
    
    @SuppressWarnings("unchecked")
    public static <O> Codec<O> create(Function<O, MapCodec<O>> builder) {
        return (Codec<O>) Codec.PASSTHROUGH;
    }
    
    @SuppressWarnings("unchecked")
    public static <O> MapCodec<O> mapCodec(Function<O, MapCodec<O>> builder) {
        return (MapCodec<O>) EMPTY_MAPCODEC;
    }
    
    @SuppressWarnings("unchecked")
    public static <O, F> MapCodec<F> forGetter(Function<O, F> getter) {
        return (MapCodec<F>) EMPTY_MAPCODEC;
    }
    
    @SuppressWarnings("unchecked")
    public static <O, F> MapCodec<F> of(MapCodec<F> codec, Function<O, F> getter) {
        return (MapCodec<F>) EMPTY_MAPCODEC;
    }
    
    // MapCodec is an interface with all default methods, so empty impl works
    private static final MapCodec<?> EMPTY_MAPCODEC = new MapCodec<Object>() {};
}
