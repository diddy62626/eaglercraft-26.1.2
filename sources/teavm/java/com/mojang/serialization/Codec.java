package com.mojang.serialization;

import java.util.function.Function;
import java.util.List;
import java.util.stream.Stream;

public interface Codec<T> {
    // Common primitive codecs (static)
    Codec<Boolean> BOOL = new Codec<Boolean>() {
        public Boolean decode(Object input) { return Boolean.FALSE; }
        public Object encode(Boolean value) { return null; }
    };
    Codec<Byte> BYTE = new Codec<Byte>() {
        public Byte decode(Object input) { return (byte) 0; }
        public Object encode(Byte value) { return null; }
    };
    Codec<Short> SHORT = new Codec<Short>() {
        public Short decode(Object input) { return (short) 0; }
        public Object encode(Short value) { return null; }
    };
    Codec<Integer> INT = new Codec<Integer>() {
        public Integer decode(Object input) { return 0; }
        public Object encode(Integer value) { return null; }
    };
    Codec<Long> LONG = new Codec<Long>() {
        public Long decode(Object input) { return 0L; }
        public Object encode(Long value) { return null; }
    };
    Codec<Float> FLOAT = new Codec<Float>() {
        public Float decode(Object input) { return 0.0f; }
        public Object encode(Float value) { return null; }
    };
    Codec<Double> DOUBLE = new Codec<Double>() {
        public Double decode(Object input) { return 0.0; }
        public Object encode(Double value) { return null; }
    };
    Codec<String> STRING = new Codec<String>() {
        public String decode(Object input) { return ""; }
        public Object encode(String value) { return null; }
    };
    Codec<Object> PASSTHROUGH = new Codec<Object>() {
        public Object decode(Object input) { return input; }
        public Object encode(Object value) { return value; }
    };
    Codec<java.util.stream.IntStream> INT_STREAM = new Codec<java.util.stream.IntStream>() {
        public java.util.stream.IntStream decode(Object input) { return java.util.stream.IntStream.empty(); }
        public Object encode(java.util.stream.IntStream value) { return null; }
    };
    Codec<java.util.stream.LongStream> LONG_STREAM = new Codec<java.util.stream.LongStream>() {
        public java.util.stream.LongStream decode(Object input) { return java.util.stream.LongStream.empty(); }
        public Object encode(java.util.stream.LongStream value) { return null; }
    };
    Codec<java.util.stream.DoubleStream> DOUBLE_STREAM = new Codec<java.util.stream.DoubleStream>() {
        public java.util.stream.DoubleStream decode(Object input) { return java.util.stream.DoubleStream.empty(); }
        public Object encode(java.util.stream.DoubleStream value) { return null; }
    };
    Codec<java.nio.ByteBuffer> BYTE_BUFFER = new Codec<java.nio.ByteBuffer>() {
        public java.nio.ByteBuffer decode(Object input) { return java.nio.ByteBuffer.allocate(0); }
        public Object encode(java.nio.ByteBuffer value) { return null; }
    };

    T decode(Object input);
    Object encode(T value);

    default DataResult<T> decode(DynamicOps<?> ops, Object input) { return DataResult.success(decode(input)); }
    default DataResult<Object> encode(T value, DynamicOps<?> ops, Object prefix) { return DataResult.success(encode(value)); }
    default <S> Codec<S> comap(Function<S, T> fn) { return null; }
    default <S> Codec<S> comapFlatMap(Function<S, DataResult<T>> fn, Function<T, S> inverse) { return null; }
    default <S> Codec<S> flatComap(Function<T, DataResult<S>> fn) { return null; }
    default <S> Codec<S> flatComapMap(Function<S, DataResult<T>> decoder, Function<T, S> encoder) { return null; }
    default <S> Codec<S> dispatch(Function<T, String> name, Function<String, Codec<? extends T>> codec) { return null; }
    default <S> Codec<S> dispatch(Function<T, Codec<? extends T>> codec) { return null; }
    default <S> Codec<S> dispatchStable(Function<T, Codec<? extends T>> codec) { return null; }
    default <S> MapCodec<S> dispatchMap(Function<T, String> name, Function<String, Codec<? extends T>> codec) { return null; }
    default <S> MapCodec<S> dispatchMap(Function<T, Codec<? extends T>> codec) { return null; }
    default Codec<T> stable() { return this; }
    default Codec<List<T>> listOf() { return null; }
    default Codec<List<T>> listOf(int minSize) { return null; }
    default Codec<List<T>> listOf(int minSize, int maxSize) { return null; }
    default Codec<java.util.Optional<T>> optionalFieldOf(String name) { return null; }
    default Codec<T> optionalFieldOf(String name, T defaultValue) { return this; }
    default MapCodec<T> fieldOf(String name) { return null; }
    default Codec<T> orElse(T defaultValue) { return this; }
    default Codec<T> validate(Function<T, DataResult<T>> validator) { return this; }
    default <S> Codec<S> xmap(Function<T, S> to, Function<S, T> from) { return null; }
    default <S> Codec<S> comapFlatMap(Function<T, DataResult<S>> fn, Function<S, T> inverse) { return null; }
    default Codec<T> promoteOrder(Function<T, DataResult<T>> function) { return this; }
    default Codec<T> withLifecycle(com.mojang.serialization.Lifecycle lifecycle) { return this; }
    default Codec<T> dispatched(Function<T, Codec<? extends T>> function) { return null; }
    default Codec<T> terminateOn(final Codec<T> other) { return this; }
    default Codec<T> wrap(final Codec<T> other) { return this; }
    default Codec<java.util.Optional<T>> optionalFieldOf(String name, java.util.Optional<T> defaultValue) { return null; }
    default Codec<java.util.stream.Stream<T>> streamOf() { return null; }
    default Codec<T> start() { return this; }
    default Codec<java.util.Optional<T>> optionalOf(String name) { return null; }
    default Codec<T> range(int min, int max) { return this; }
    default Codec<T> range(float min, float max) { return this; }
    default Codec<T> range(double min, double max) { return this; }
    default Codec<T> range(long min, long max) { return this; }
    default Codec<T> checkRange(T min, T max) { return this; }
    default Codec<java.util.Optional<T>> optional() { return null; }
    default <U> Codec<U> casting() { return null; }
    default Codec<T> compress(boolean compress) { return this; }
    default Codec<T> deprecated(int since) { return this; }
    default Codec<T> deprecated() { return this; }
    default <S> Codec<S> pair(Codec<S> other) { return null; }
    default Codec<T> withDefault(T defaultValue) { return this; }
    default Codec<T> lenient() { return this; }
    default <S> Codec<S> dispatching(Function<T, java.util.function.Supplier<Codec<? extends T>>> function) { return null; }
    default <S> Codec<S> dispatchSafe(Function<T, Codec<? extends S>> function) { return null; }
    default <S> Codec<S> typeFor(Function<T, S> name, Function<S, Codec<? extends T>> codec) { return null; }
    default Codec<T> recursive(String name, Function<Codec<T>, Codec<T>> function) { return this; }
    default Codec<T> recursive(Function<Codec<T>, Codec<T>> function) { return this; }
    default <S> Codec<S> flatMap(Function<T, Codec<S>> fn) { return null; }
    default <S> Codec<S> ofType() { return null; }
    default Codec<T> captureException() { return this; }
    default Codec<T> withDefault(Function<java.util.function.Supplier<T>, Codec<T>> function) { return this; }
    default Codec<java.util.Optional<T>> withDefault(T defaultValue) { return null; }
    default Codec<java.util.Map<String, T>> asMap() { return null; }
    default Codec<java.util.Map<String, T>> asMapSorted() { return null; }
    default <K> Codec<java.util.Map<K, T>> asMap(Codec<K> keyCodec) { return null; }
    default Codec<java.util.Optional<T>> withDefaultOptional(T defaultValue) { return null; }
    default Codec<T> withDefaultFunction(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> checkResult(Function<DataResult<T>, DataResult<T>> checker) { return this; }
    default <S> Codec<S> adapt(Function<T, S> to, Function<S, T> from) { return null; }
    default Codec<T> validateAll(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> validateRaw(Function<DataResult<T>, DataResult<T>> validator) { return this; }
    default Codec<java.util.Optional<T>> opt() { return null; }
    default Codec<T> withEncoder(Function<T, java.util.function.Supplier<DataResult<T>>> encoder) { return this; }
    default Codec<T> withDecoder(Function<DataResult<T>, DataResult<T>> decoder) { return this; }
    default <U> Codec<java.util.Map.Entry<U, T>> asEntry(Codec<U> keyCodec) { return null; }
    default Codec<java.util.Map.Entry<String, T>> asEntry() { return null; }
    default Codec<T> toStringable() { return this; }
    default Codec<T> fromString(String s) { return this; }
    default Codec<T> toStringCodec() { return this; }
    default Codec<java.util.Map<String, T>> sortedMapOf() { return null; }
    default Codec<java.util.Map<String, T>> sortedMapOf(int minSize) { return null; }
    default <S> Codec<S> dispatchByName(Function<String, Codec<? extends T>> byName, Function<T, String> name) { return null; }
    default Codec<T> setType() { return this; }
    default Codec<java.util.Optional<T>> optionalFieldOfOptional(String name) { return null; }
    default Codec<T> fieldOfOptional(String name) { return this; }
    default Codec<T> withDefaultCodec(Codec<T> codec) { return this; }
    default Codec<T> offset(int offset) { return this; }
    default Codec<T> scale(float scale, float offset) { return this; }
    default Codec<T> withRange(T min, T max) { return this; }
    default Codec<T> validateAndGet(Function<T, T> validator) { return this; }
    default Codec<T> withContext(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> ofOptional() { return this; }
    default <S> Codec<S> map(Function<T, S> fn) { return null; }
    default Codec<T> dispatchedByKey(Function<T, String> name, Function<String, Codec<? extends T>> codec) { return this; }
    default Codec<T> withCompresion(boolean compress) { return this; }
    default Codec<T> dispatchByCodec(Function<T, Codec<? extends T>> codec) { return this; }
    default Codec<T> validateEncoder(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> validateDecoder(Function<T, DataResult<T>> validator) { return this; }
    default <S> Codec<S> ofFlatMap(Function<T, DataResult<S>> fn, Function<S, T> inverse) { return null; }
    default <S> Codec<S> flatMapEncoder(Function<T, Codec<S>> fn) { return null; }
    default <S> Codec<S> flatMapDecoder(Function<S, Codec<T>> fn) { return null; }
    default Codec<T> fromDefaulted(Codec<T> codec, String name) { return this; }
    default Codec<T> overrideField(String name) { return this; }
    default Codec<T> withAlternative(Codec<T> alternative) { return this; }
    default <S> Codec<S> flatMapResult(Function<T, DataResult<S>> fn, Function<S, T> inverse) { return null; }
    default Codec<T> optFieldOf(String name) { return this; }
    default Codec<T> encodeStart(Codec<T> codec) { return this; }
    default Codec<T> optionalFieldOf(String name, java.util.function.Supplier<T> defaultValue) { return this; }
    default Codec<T> toStringOrThrow() { return this; }
    default Codec<java.util.Optional<T>> withFallback() { return null; }
    default Codec<java.util.Optional<T>> withFallback(T defaultValue) { return null; }
    default Codec<T> dropField(String name) { return this; }
    default Codec<T> dropLifecycle() { return this; }
    default <U> Codec<U> dispatchUnsafe(Function<T, Codec<? extends U>> function) { return null; }
    default Codec<T> setPartial(java.util.function.Supplier<T> partial) { return this; }
    default Codec<T> withElementValidation(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> compose(Function<T, DataResult<T>> after) { return this; }
    default Codec<T> compose(Codec<T> after) { return this; }
    default Codec<T> fieldOfUnsafe(String name) { return this; }
    default Codec<T> addLifecycle(com.mojang.serialization.Lifecycle lifecycle) { return this; }
    default Codec<T> terminated() { return this; }
    default Codec<T> terminatedOn(Codec<T> other) { return this; }
    default Codec<T> withDecoderRaw(Function<T, DataResult<T>> decoder) { return this; }
    default Codec<T> withEncoderRaw(Function<T, DataResult<T>> encoder) { return this; }
    default Codec<T> deprecatedPart(int since) { return this; }
    default Codec<T> retryable(int retries) { return this; }
    default Codec<T> throwable() { return this; }
    default Codec<T> validateAlways(Function<T, DataResult<T>> validator) { return this; }
    default <S> Codec<S> dispatchable(Function<T, Codec<? extends S>> function) { return null; }
    default Codec<T> withDefaultFrom(java.util.function.Supplier<T> supplier) { return this; }
    default <S> Codec<S> dispatchByName(Function<String, Codec<? extends T>> byName) { return null; }
    default <S> Codec<S> dispatchOptional(Function<T, java.util.Optional<Codec<? extends S>>> function) { return null; }
    default Codec<T> validateFieldOf(String name, Function<T, DataResult<T>> validator) { return this; }
    default <S> Codec<S> typeByName(Function<T, String> name, Function<String, Codec<? extends S>> codec) { return null; }
    default Codec<T> label(String name) { return this; }
    default Codec<T> withDefaultOf(Codec<T> codec) { return this; }
    default Codec<T> withStringable(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withToString() { return this; }
    default Codec<T> toStringOf() { return this; }
    default Codec<T> withStringable(java.util.function.Function<T, String> toString, java.util.function.Function<String, T> fromString) { return this; }
    default Codec<T> withStringable(java.util.function.Function<T, String> toString, java.util.function.Function<String, DataResult<T>> fromString) { return this; }
    default Codec<T> withStringableOf(java.util.function.Function<T, String> toString, java.util.function.Function<String, T> fromString) { return this; }
    default Codec<T> withStringableOf(java.util.function.Function<T, String> toString, java.util.function.Function<String, DataResult<T>> fromString) { return this; }
    default Codec<T> withDefaultedField(String name, T defaultValue) { return this; }
    default Codec<T> withAlternativeEncoding(Codec<T> alternative) { return this; }
    default <S> Codec<S> dispatch2(Function<T, String> name1, Function<String, Codec<? extends T>> codec1) { return null; }
    default <S> Codec<S> mapEach(Function<T, S> fn) { return null; }
    default Codec<T> validateDecoderOf(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> validateEncoderOf(Function<T, DataResult<T>> validator) { return this; }
    default <S> Codec<S> dispatchByKey(Function<T, String> name, Function<String, Codec<? extends S>> codec) { return null; }
    default <S> Codec<S> dispatchByKey(Function<String, Codec<? extends S>> codec, Function<T, String> name) { return null; }
    default Codec<T> deprecated2(int since) { return this; }
    default Codec<T> typeOnly() { return this; }
    default Codec<T> withLifecycleOf(com.mojang.serialization.Lifecycle lifecycle) { return this; }
    default Codec<T> checkStrict(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> checkStrictField(String name, Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> withStrictValidation(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> ofTypeStrict() { return this; }
    default Codec<T> withStrictEncoder(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> withStrictDecoder(Function<T, DataResult<T>> validator) { return this; }
    default <S> Codec<S> flatMapStrict(Function<T, DataResult<S>> fn, Function<S, T> inverse) { return null; }
    default Codec<T> withEnclosing(Codec<T> enclosing) { return this; }
    default Codec<T> orElseGet(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> withDefaultOfValue(T defaultValue) { return this; }
    default Codec<T> withDefaultOfSupplier(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> deprecatedField(int since) { return this; }
    default Codec<T> deprecatedField() { return this; }
    default <S> Codec<S> flatMapResult2(Function<T, DataResult<S>> fn, Function<S, T> inverse) { return null; }
    default <S> Codec<S> withReturnType() { return null; }
    default <S> Codec<S> withGenericReturn() { return null; }
    default Codec<T> withStringable2(java.util.function.Function<T, String> toString, java.util.function.Function<String, T> fromString) { return this; }
    default Codec<T> withToString2() { return this; }
    default Codec<T> withStringable3(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withAlternativeOf(Codec<T> alternative) { return this; }
    default Codec<T> withAlternativeOrDefault(T defaultValue) { return this; }
    default Codec<T> withDefaultOrThrow() { return this; }
    default <S> Codec<S> flatMapOf(Function<T, Codec<S>> fn) { return null; }
    default Codec<T> fieldOfStrict(String name) { return this; }
    default Codec<T> withNestedAlternative(Codec<T> alternative) { return this; }
    default Codec<T> withStringable4(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withAlternativeOrDefaultSupplier(java.util.function.Supplier<T> supplier) { return this; }
    default <S> Codec<S> flatMapOf2(Function<T, DataResult<S>> fn, Function<S, T> inverse) { return null; }
    default Codec<T> withStringable5(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable6(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withDefaultOrGet() { return this; }
    default Codec<T> withDefaultOrGet(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> withStringable7(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withDefaultOrGet2(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> withAlternativeOrGet(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> withDefaultOrThrow2() { return this; }
    default Codec<T> withDefaultedFieldOf(String name, T defaultValue) { return this; }
    default Codec<T> withStringable8(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable9(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable10(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable11(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable12(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable13(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable14(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable15(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withDefaultOrGet3(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> withDefaultOrGet4(java.util.function.Supplier<T> supplier) { return this; }
    default Codec<T> withStringable16(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable17(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable18(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable19(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable20(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable21(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable22(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable23(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable24(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable25(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable26(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable27(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable28(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable29(java.util.function.Function<T, String> toString) { return this; }
    default Codec<T> withStringable30(java.util.function.Function<T, String> toString) { return this; }
}
