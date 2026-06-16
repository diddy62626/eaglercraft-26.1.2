package com.mojang.serialization;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class OptionalDynamic<T> {
    private final DynamicOps<T> ops;
    private final DataResult<Dynamic<T>> result;

    public OptionalDynamic(DynamicOps<T> ops, DataResult<Dynamic<T>> result) {
        this.ops = ops;
        this.result = result;
    }

    public DataResult<Dynamic<T>> get() { return result; }
    public Dynamic<T> orElse(Dynamic<T> defaultValue) {
        return result.resultOrNull() != null ? result.resultOrNull() : defaultValue;
    }
    public Dynamic<T> orElseEmptyMap() { return new Dynamic<>(ops, ops.emptyMap()); }
    public Dynamic<T> orElseEmptyList() { return new Dynamic<>(ops, ops.emptyList()); }
    public DataResult<Number> asNumber() { return DataResult.success(0); }
    public DataResult<String> asString() { return DataResult.success(""); }
    public String asString(String defaultValue) { return defaultValue; }
    public byte asByte(byte defaultValue) { return defaultValue; }
    public short asShort(short defaultValue) { return defaultValue; }
    public int asInt(int defaultValue) { return defaultValue; }
    public long asLong(long defaultValue) { return defaultValue; }
    public float asFloat(float defaultValue) { return defaultValue; }
    public double asDouble(double defaultValue) { return defaultValue; }
    public boolean asBoolean(boolean defaultValue) { return defaultValue; }
    public DataResult<Stream<Dynamic<T>>> asStreamOpt() { return DataResult.success(Stream.empty()); }
    public DataResult<java.util.stream.IntStream> asIntStreamOpt() { return DataResult.success(java.util.stream.IntStream.empty()); }
    public OptionalDynamic<T> get(String key) { return new OptionalDynamic<>(ops, DataResult.success(null)); }
    public <U> DataResult<U> map(Function<Dynamic<T>, U> fn) { return DataResult.success(null); }
    public <U> DataResult<U> flatMap(Function<Dynamic<T>, DataResult<U>> fn) { return DataResult.success(null); }
    public Optional<Dynamic<T>> result() { return result.result(); }
    public <U> OptionalDynamic<U> cast(DynamicOps<U> ops) { return new OptionalDynamic<>(ops, DataResult.success(null)); }

    public DataResult<java.nio.ByteBuffer> asByteBufferOpt() { return DataResult.success(java.nio.ByteBuffer.allocate(0)); }
    public java.util.stream.IntStream asIntStream() { return java.util.stream.IntStream.empty(); }
    public java.util.stream.LongStream asLongStream() { return java.util.stream.LongStream.empty(); }
    public <K, V> java.util.Map<K, V> asMap(Function<Dynamic<T>, K> keyDecoder, Function<Dynamic<T>, V> valueDecoder) { return new java.util.HashMap<>(); }
    public java.util.stream.Stream<Dynamic<T>> asStream() { return java.util.stream.Stream.empty(); }
    public Dynamic<T> createList(java.util.stream.Stream<Dynamic<T>> stream) { return new Dynamic<>(ops, ops.emptyList()); }
    public Dynamic<T> createString(String value) { return new Dynamic<>(ops, ops.createString(value)); }
    public <U> DataResult<U> read(Decoder<U> decoder) { return DataResult.success(null); }

    public <U> java.util.List<U> asList(java.util.function.Function<Dynamic<T>, U> decoder) { return new java.util.ArrayList<>(); }
    public Dynamic<T> createInt(int value) { return new Dynamic<>(ops, ops.createInt(value)); }
    public Dynamic<T> createMap(java.util.Map<Dynamic<T>, Dynamic<T>> map) { return new Dynamic<>(ops, ops.emptyMap()); }
    public Dynamic<T> emptyMap() { return new Dynamic<>(ops, ops.emptyMap()); }
}
