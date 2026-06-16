package com.mojang.serialization;

public interface Encoder<T> {
    default DataResult<Object> encode(T value, DynamicOps<?> ops, Object prefix) { return DataResult.success(null); }
    default <U> U encodeStart(DynamicOps<U> ops, T value) { return null; }
}
