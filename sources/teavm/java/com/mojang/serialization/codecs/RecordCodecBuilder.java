package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;
import java.util.function.Function;

// PATCHED: Returns no-op Codec/MapCodec instances instead of null.
// The original methods return null which causes 'X is not a function'
// when MC tries to chain method calls on the result.
// These no-op implementations return 'this' for all method calls,
// allowing the builder chain to complete without crashing.

public class RecordCodecBuilder {
    
    @SuppressWarnings("unchecked")
    public static <O> Codec<O> create(Function<O, MapCodec<O>> builder) {
        return (Codec<O>) NoOpCodec.INSTANCE;
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
    
    // No-op Codec that returns defaults for all operations
    private static class NoOpCodec implements Codec<Object> {
        static final NoOpCodec INSTANCE = new NoOpCodec();
        
        @Override
        public <T> DataResult<Object> decode(com.mojang.serialization.Dynamic<T> input) {
            return DataResult.success(null);
        }
        
        @Override
        public <T> DataResult<T> encode(Object input, com.mojang.serialization.DynamicOps<T> ops, T prefix) {
            return DataResult.success(prefix);
        }
    }
    
    // No-op MapCodec that returns defaults for all operations
    private static class NoOpMapCodec extends MapCodec<Object> {
        static final NoOpMapCodec INSTANCE = new NoOpMapCodec();
        
        @Override
        public <T> DataResult<Object> decode(com.mojang.serialization.DynamicOps<T> ops, com.mojang.serialization.MapLike<T> input) {
            return DataResult.success(null);
        }
        
        @Override
        public <T> DataResult<T> encode(Object input, com.mojang.serialization.DynamicOps<T> ops, T prefix) {
            return DataResult.success(prefix);
        }
        
        @Override
        public <T> java.util.stream.Stream<T> keys(com.mojang.serialization.DynamicOps<T> ops) {
            return java.util.stream.Stream.empty();
        }
    }
}
