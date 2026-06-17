package com.mojang.serialization;

public interface Encoder<T> {
    default DataResult<Object> encode(T value, DynamicOps<?> ops, Object prefix) { return DataResult.success(null); }

    static <T> Encoder<T> error(String message) {
        return new Encoder<T>() {};
    }

    default DataResult<Object> encodeStart(DynamicOps<?> ops, T value) { return DataResult.success(encode(value, ops, ops.empty())); }
}
