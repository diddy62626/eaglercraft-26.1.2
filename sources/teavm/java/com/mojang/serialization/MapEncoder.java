package com.mojang.serialization;

public interface MapEncoder<T> {
    default DataResult<T> encode(T value, DynamicOps<?> ops, Object prefix) { return DataResult.success(null); }
}
