package com.mojang.serialization;

public interface DynamicOps<T> {
    T empty();
    T emptyList();
    T emptyMap();

    default T createBoolean(boolean value) { return empty(); }
    default T createByte(byte value) { return empty(); }
    default T createShort(short value) { return empty(); }
    default T createInt(int value) { return empty(); }
    default T createLong(long value) { return empty(); }
    default T createFloat(float value) { return empty(); }
    default T createDouble(double value) { return empty(); }
    default T createString(String value) { return empty(); }
    default T createNumeric(Number value) { return empty(); }

    default T convertTo(DynamicOps<T> outOps, T input) { return input; }
    default DataResult<java.util.Optional<T>> get(T input, String key) { return DataResult.success(java.util.Optional.empty()); }
    default T set(T input, String key, T value) { return input; }
    default T update(T input, String key, java.util.function.Function<T, T> fn) { return input; }
    default DataResult<Number> getNumberValue(T input) { return DataResult.success(0); }
    default DataResult<String> getStringValue(T input) { return DataResult.success(""); }
    default DataResult<Boolean> getBooleanValue(T input) { return DataResult.success(false); }
    default DataResult<java.util.stream.Stream<T>> getStream(T input) { return DataResult.success(java.util.stream.Stream.empty()); }
    default DataResult<java.util.stream.Stream<java.util.Map.Entry<T, T>>> getMapValues(T input) {
        return DataResult.success(java.util.stream.Stream.empty());
    }
    default DataResult<MapLike<T>> getMap(T input) { return DataResult.success(null); }
    default T mergeToMap(T input, T key, T value) { return input; }
    default T mergeToList(T input, T value) { return input; }
    default T remove(T input, String key) { return input; }
    default boolean compressMaps() { return false; }
    default com.mojang.serialization.ListBuilder<T> listBuilder() { return null; }
    default com.mojang.serialization.RecordBuilder<T> mapBuilder() { return null; }

    default T createByteList(java.nio.ByteBuffer buf) { return empty(); }
    default T createIntList(java.util.stream.IntStream stream) { return empty(); }
    default T createLongList(java.util.stream.LongStream stream) { return empty(); }
    default T createList(java.util.stream.Stream<T> stream) { return empty(); }
    default T createMap(java.util.Map<T, T> map) { return empty(); }
    default DataResult<T> mergeToMap(T input, MapLike<T> map) { return DataResult.success(input); }
    default DataResult<T> mergeToPrimitive(T input, T value) { return DataResult.success(input); }
}
