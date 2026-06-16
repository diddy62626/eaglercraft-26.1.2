package com.mojang.serialization;

public interface MapCodec<T> extends Codec<T> {
    default Codec<T> codec() { return this; }
    default Encoder<T> encoder() { return null; }
    default Decoder<T> decoder() { return null; }
    default DataResult<T> decode(DynamicOps<?> ops, MapLike<?> input) { return DataResult.success(null); }
    default <S> MapCodec<S> flatXmap(java.util.function.Function<T, DataResult<S>> to, java.util.function.Function<S, DataResult<T>> from) { return null; }
    default <S> MapCodec<S> xmap(java.util.function.Function<T, S> to, java.util.function.Function<S, T> from) { return null; }
    default MapCodec<T> orElse(T defaultValue) { return this; }
    default MapCodec<T> orElseGet(java.util.function.Supplier<T> supplier) { return this; }
    default MapCodec<T> withLifecycle(Lifecycle lifecycle) { return this; }
    default MapCodec<T> stable() { return this; }
    default MapCodec<T> deprecated(int since) { return this; }
    default MapCodec<java.util.Optional<T>> optionalFieldOf(String name) { return null; }

    default MapCodec<T> unit(T defaultValue) { return this; }
}
