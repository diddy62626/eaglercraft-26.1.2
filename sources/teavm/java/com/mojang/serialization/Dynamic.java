package com.mojang.serialization;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Dynamic<T> {
    private final DynamicOps<T> ops;
    private final T value;

    public Dynamic(DynamicOps<T> ops) {
        this(ops, ops == null ? null : ops.empty());
    }

    public Dynamic(DynamicOps<T> ops, T value) {
        this.ops = ops;
        this.value = value;
    }

    public DynamicOps<T> getOps() { return ops; }
    public T getValue() { return value; }

    public <U> Dynamic<U> convert(DynamicOps<U> toOps) { return new Dynamic<>(toOps, null); }
    public Dynamic<T> map(java.util.function.Function<T, T> fn) { return new Dynamic<>(ops, fn.apply(value)); }
    public OptionalDynamic<T> get(String key) { return new OptionalDynamic<>(ops, DataResult.success(this)); }
    public DataResult<Dynamic<T>> getResult(String key) { return DataResult.success(this); }
    public DataResult<Number> asNumber() { return DataResult.success(0); }
    public DataResult<String> asString() { return DataResult.success(""); }
    public String asString(String defaultValue) { return defaultValue; }
    public DataResult<java.util.Optional<Dynamic<T>>> getElement(String key) { return DataResult.success(java.util.Optional.empty()); }
    public Dynamic<T> emptyList() { return new Dynamic<>(ops, ops.emptyList()); }
    public Dynamic<T> emptyMap() { return new Dynamic<>(ops, ops.emptyMap()); }

    public Dynamic<T> createBoolean(boolean value) { return new Dynamic<>(ops, ops.createBoolean(value)); }
    public Dynamic<T> createByte(byte value) { return new Dynamic<>(ops, ops.createByte(value)); }
    public Dynamic<T> createShort(short value) { return new Dynamic<>(ops, ops.createShort(value)); }
    public Dynamic<T> createInt(int value) { return new Dynamic<>(ops, ops.createInt(value)); }
    public Dynamic<T> createLong(long value) { return new Dynamic<>(ops, ops.createLong(value)); }
    public Dynamic<T> createFloat(float value) { return new Dynamic<>(ops, ops.createFloat(value)); }
    public Dynamic<T> createDouble(double value) { return new Dynamic<>(ops, ops.createDouble(value)); }
    public Dynamic<T> createString(String value) { return new Dynamic<>(ops, ops.createString(value)); }
    public Dynamic<T> createIntList(IntStream stream) { return this; }
    public Dynamic<T> createList(Stream<Dynamic<T>> stream) { return this; }
    public Dynamic<T> createMap(Map<Dynamic<T>, Dynamic<T>> map) { return this; }

    public Dynamic<T> set(String key, Dynamic<T> value) { return this; }
    public Dynamic<T> update(String key, java.util.function.Function<Dynamic<T>, Dynamic<T>> fn) { return this; }
    public Dynamic<T> remove(String key) { return this; }
    public Dynamic<T> renameAndFixField(String oldName, String newName, java.util.function.UnaryOperator<Dynamic<T>> fixer) { return this; }
    @SuppressWarnings("rawtypes")
    public DataResult<java.util.stream.Stream> getMapValues() { return DataResult.success(java.util.stream.Stream.empty()); }

    public interface Function<T, R> { R apply(T t); }

    public static <T> Dynamic<T> copyField(Dynamic<T> src, String srcKey, Dynamic<T> dst, String dstKey) { return dst; }
    public Dynamic<T> renameField(String oldKey, String newKey) { return this; }
    public Dynamic<T> replaceField(String key, String newKey, java.util.Optional<Dynamic<T>> value) { return this; }
    public Dynamic<T> setFieldIfPresent(String key, java.util.Optional<Dynamic<T>> value) { return this; }
    public Dynamic<T> updateMapValues(java.util.function.Function<Dynamic<T>, Dynamic<T>> fn) { return this; }

    public float asFloat(float defaultValue) { return defaultValue; }
    public <U> java.util.List<U> asList(java.util.function.Function<Dynamic<T>, U> decoder) { return new java.util.ArrayList<>(); }
    public DataResult<MapLike<Dynamic<T>>> asMapOpt() { return DataResult.success(null); }
    public java.util.stream.Stream<Dynamic<T>> asStream() { return java.util.stream.Stream.empty(); }
    public Dynamic<T> createLongList(java.util.stream.LongStream stream) { return this; }

    public DataResult<Boolean> asBoolean() { return DataResult.success(false); }
    public java.util.stream.LongStream asLongStream() { return java.util.stream.LongStream.empty(); }

    public short asShort(short defaultValue) { return defaultValue; }
    public byte asByte(byte defaultValue) { return defaultValue; }
}
