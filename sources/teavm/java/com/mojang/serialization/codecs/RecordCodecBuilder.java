package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import java.util.stream.Stream;

// PATCHED: Returns no-op Codec/MapCodec instances instead of null.

public class RecordCodecBuilder {
    
    @SuppressWarnings("unchecked")
    public static <O> Codec<O> create(Function<O, MapCodec<O>> builder) {
        return (Codec<O>) Codec.PASSTHROUGH;
    }
    
    @SuppressWarnings("unchecked")
    public static <O> MapCodec<O> mapCodec(Function<O, MapCodec<O>> builder) {
        return (MapCodec<O>) NoOpMapCodec.INSTANCE;
    }
    
    @SuppressWarnings("unchecked")
    public static <O, F> MapCodec<F> forGetter(Function<O, F> getter) {
        return (MapCodec<F>) NoOpMapCodec.INSTANCE;
    }
    
    @SuppressWarnings("unchecked")
    public static <O, F> MapCodec<F> of(MapCodec<F> codec, Function<O, F> getter) {
        return (MapCodec<F>) NoOpMapCodec.INSTANCE;
    }
    
    // Simple no-op MapCodec implementation
    private static class NoOpMapCodec<T> extends MapCodec<T> {
        static final NoOpMapCodec<Object> INSTANCE = new NoOpMapCodec<>();
        
        @Override
        public <T2> DataResult<T> decode(DynamicOps<T2> ops, com.mojang.serialization.MapLike<T2> input) {
            return DataResult.success(null);
        }
        
        @Override
        public <T2> DataResult<T2> encode(T input, DynamicOps<T2> ops, T2 prefix) {
            return DataResult.success(prefix);
        }
        
        @Override
        public <T2> Stream<T2> keys(DynamicOps<T2> ops) {
            return Stream.empty();
        }
    }
}
