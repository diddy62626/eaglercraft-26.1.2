package com.mojang.serialization;

public interface Decoder<T> {
    default DataResult<T> parse(DynamicOps<?> ops, Object input) { return DataResult.success(null); }
    default <U> T parse(Dynamic<U> input) { return null; }

    default MapDecoder<T> unit(T defaultValue) { return new MapDecoder<T>() {}; }
    default DataResult<T> unit() { return DataResult.success(null); }

    static <T> Decoder<T> error(String message) { return new Decoder<T>() {}; }
}
