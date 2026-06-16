package com.mojang.serialization;

public interface ListBuilder<T> {
    ListBuilder<T> add(T value);
    ListBuilder<T> add(DataResult<T> value);
    DataResult<T> build(T prefix);
    default DataResult<T> build() { return build(empty()); }
    T empty();
}
