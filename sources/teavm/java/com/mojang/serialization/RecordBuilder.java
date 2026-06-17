package com.mojang.serialization;

public interface RecordBuilder<T> {
    RecordBuilder<T> add(String name, T value);
    RecordBuilder<T> add(String name, DataResult<T> value);
    RecordBuilder<T> add(com.mojang.serialization.DataResult<java.util.Map.Entry<T, T>> entry);
    DataResult<T> build(T prefix);
    default DataResult<T> build() { return build(empty()); }
    T empty();
}
