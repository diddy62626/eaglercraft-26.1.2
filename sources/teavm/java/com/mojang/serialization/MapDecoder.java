package com.mojang.serialization;

public interface MapDecoder<T> {
    default DataResult<T> decode(DynamicOps<?> ops, MapLike<?> input) { return DataResult.success(null); }
    default <S> MapDecoder<S> map(Function<T, S> fn) { return (MapDecoder<S>) this; }
    public interface Function<T, R> { R apply(T t); }
}
