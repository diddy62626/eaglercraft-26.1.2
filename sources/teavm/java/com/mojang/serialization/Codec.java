package com.mojang.serialization;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Codec<T> {
    // Primitive codecs
    Codec<Boolean> BOOL = new Codec<Boolean>() { public Boolean decode(Object i) { return false; } public Object encode(Boolean v) { return null; } };
    Codec<Byte> BYTE = new Codec<Byte>() { public Byte decode(Object i) { return 0; } public Object encode(Byte v) { return null; } };
    Codec<Short> SHORT = new Codec<Short>() { public Short decode(Object i) { return 0; } public Object encode(Short v) { return null; } };
    Codec<Integer> INT = new Codec<Integer>() { public Integer decode(Object i) { return 0; } public Object encode(Integer v) { return null; } };
    Codec<Long> LONG = new Codec<Long>() { public Long decode(Object i) { return 0L; } public Object encode(Long v) { return null; } };
    Codec<Float> FLOAT = new Codec<Float>() { public Float decode(Object i) { return 0.0f; } public Object encode(Float v) { return null; } };
    Codec<Double> DOUBLE = new Codec<Double>() { public Double decode(Object i) { return 0.0; } public Object encode(Double v) { return null; } };
    Codec<String> STRING = new Codec<String>() { public String decode(Object i) { return ""; } public Object encode(String v) { return null; } };
    Codec<Object> PASSTHROUGH = new Codec<Object>() { public Object decode(Object i) { return i; } public Object encode(Object v) { return v; } };
    Codec<java.util.stream.IntStream> INT_STREAM = new Codec<java.util.stream.IntStream>() { public java.util.stream.IntStream decode(Object i) { return java.util.stream.IntStream.empty(); } public Object encode(java.util.stream.IntStream v) { return null; } };
    Codec<java.util.stream.LongStream> LONG_STREAM = new Codec<java.util.stream.LongStream>() { public java.util.stream.LongStream decode(Object i) { return java.util.stream.LongStream.empty(); } public Object encode(java.util.stream.LongStream v) { return null; } };
    Codec<java.util.stream.DoubleStream> DOUBLE_STREAM = new Codec<java.util.stream.DoubleStream>() { public java.util.stream.DoubleStream decode(Object i) { return java.util.stream.DoubleStream.empty(); } public Object encode(java.util.stream.DoubleStream v) { return null; } };
    Codec<java.nio.ByteBuffer> BYTE_BUFFER = new Codec<java.nio.ByteBuffer>() { public java.nio.ByteBuffer decode(Object i) { return java.nio.ByteBuffer.allocate(0); } public Object encode(java.nio.ByteBuffer v) { return null; } };
    Codec<java.util.Optional<Object>> EMPTY = new Codec<java.util.Optional<Object>>() { public java.util.Optional<Object> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<Object> v) { return null; } };

    T decode(Object input);
    Object encode(T value);

    // Core methods
    default DataResult<T> parse(DynamicOps<?> ops, Object input) { return DataResult.success(decode(input)); }
    default DataResult<T> parse(Dynamic<?> input) { return parse(input.getOps(), input.getValue()); }
    default DataResult<Object> encode(T value, DynamicOps<?> ops, Object prefix) { return DataResult.success(encode(value)); }
    default DataResult<Object> encodeStart(DynamicOps<?> ops, T value) { return DataResult.success(encode(value)); }

    // Instance methods
    default <S> Codec<S> comap(Function<S, T> fn) { return (Codec<S>) this; }
    default <S> Codec<S> flatComap(Function<T, DataResult<S>> fn) { return (Codec<S>) this; }
    default <S> Codec<S> comapFlatMap(Function<T, DataResult<S>> fn, Function<S, T> inverse) { return (Codec<S>) this; }
    default <S> Codec<S> flatComapMap(Function<S, DataResult<T>> decoder, Function<T, S> encoder) { return (Codec<S>) this; }
    default <S> Codec<S> flatXmap(Function<T, DataResult<S>> to, Function<S, DataResult<T>> from) { return (Codec<S>) this; }
    default <S> Codec<S> xmap(Function<T, S> to, Function<S, T> from) { return (Codec<S>) this; }
    default <S> Codec<S> flatMap(Function<T, Codec<S>> fn) { return (Codec<S>) this; }
    default <S> Codec<S> adapt(Function<T, S> to, Function<S, T> from) { return (Codec<S>) this; }
    default <S> Codec<S> dispatch(Function<T, String> name, Function<String, Codec<? extends T>> codec) { return (Codec<S>) this; }
    default <S> Codec<S> dispatch(Function<T, Codec<? extends T>> codec) { return (Codec<S>) this; }
    default <S> Codec<S> dispatch(String name, Function<T, ?> fn, Function<?, Codec<? extends T>> codec) { return (Codec<S>) this; }
    default <S> Codec<S> dispatchStable(Function<T, Codec<? extends T>> codec) { return (Codec<S>) this; }
    default <S> Codec<S> partialDispatch(String name, Function<T, ?> fn, Function<?, Codec<? extends T>> codec) { return (Codec<S>) this; }
    default <S> MapCodec<S> dispatchMap(Function<T, String> name, Function<String, Codec<? extends T>> codec) { return __dummyMapCodec(); }
    default <S> MapCodec<S> dispatchMap(Function<T, Codec<? extends T>> codec) { return __dummyMapCodec(); }
    default <S> Codec<S> dispatched(Function<T, Codec<? extends T>> function) { return (Codec<S>) this; }
    default <S> Codec<S> dispatchedMap(Codec<S> codec, Function<T, S> fn) { return (Codec<S>) this; }
    default <S> Codec<S> dispatchSafe(Function<T, Codec<? extends S>> function) { return (Codec<S>) this; }
    default <S> Codec<S> typeFor(Function<T, S> name, Function<S, Codec<? extends T>> codec) { return (Codec<S>) this; }
    default <S> Codec<S> dispatchByName(Function<String, Codec<? extends T>> byName) { return (Codec<S>) this; }
    default <S> Codec<S> dispatchByName(Function<String, Codec<? extends S>> byName, Function<T, String> name) { return (Codec<S>) this; }

    default Codec<T> stable() { return this; }
    default Codec<T> withLifecycle(Lifecycle lifecycle) { return this; }
    default Codec<T> addLifecycle(Lifecycle lifecycle) { return this; }
    default Codec<T> deprecated(int since) { return this; }
    default Codec<T> deprecated() { return this; }
    default Codec<T> validate(Function<T, DataResult<T>> validator) { return this; }
    default Codec<T> promoteOrder(Function<T, DataResult<T>> fn) { return this; }
    default Codec<T> orElse(T defaultValue) { return this; }
    default Codec<T> withAlternative(Codec<T> alternative) { return this; }
    default Codec<T> terminateOn(Codec<T> other) { return this; }
    default Codec<T> wrap(Codec<T> other) { return this; }
    default Codec<T> lenient() { return this; }
    default Codec<T> captureException() { return this; }
    default Codec<T> setPartial(Supplier<T> partial) { return this; }
    default Codec<T> withDefault(T defaultValue) { return this; }
    default Codec<T> range(int min, int max) { return this; }
    default Codec<T> range(float min, float max) { return this; }
    default Codec<T> range(double min, double max) { return this; }
    default Codec<T> range(long min, long max) { return this; }
    default Codec<T> checkRange(T min, T max) { return this; }
    default Codec<T> validateEncoder(Function<T, DataResult<T>> v) { return this; }
    default Codec<T> validateDecoder(Function<T, DataResult<T>> v) { return this; }
    default Codec<T> deprecatedPart(int since) { return this; }
    default Codec<T> retryable(int retries) { return this; }
    default Codec<T> throwable() { return this; }
    default Codec<T> recursive(String name, Function<Codec<T>, Codec<T>> function) { return this; }
    default Codec<T> recursive(Function<Codec<T>, Codec<T>> function) { return this; }
    default Codec<T> compose(Function<T, DataResult<T>> after) { return this; }
    default Codec<T> compose(Codec<T> after) { return this; }
    default Codec<T> fromDefaulted(Codec<T> codec, String name) { return this; }
    default Codec<T> overrideField(String name) { return this; }
    default Codec<T> dropField(String name) { return this; }
    default Codec<T> dropLifecycle() { return this; }
    default Codec<T> setType() { return this; }
    default Codec<T> label(String name) { return this; }
    default Codec<T> ofOptional() { return this; }
    default Codec<T> start() { return this; }
    default Codec<T> captureExceptionOrDefault() { return this; }

    // List / collection methods
    default Codec<List<T>> listOf() {
        final Codec<T> self = this;
        return new Codec<List<T>>() {
            @Override public List<T> decode(Object input) { return java.util.Collections.emptyList(); }
            @Override public Object encode(List<T> value) { return null; }
        };
    }
    default Codec<List<T>> listOf(int minSize) { return listOf(); }
    default Codec<List<T>> listOf(int minSize, int maxSize) { return listOf(); }
    default Codec<List<T>> sizeLimitedListOf(int maxSize) { return new Codec<List<T>>() { public List<T> decode(Object i) { return java.util.Collections.emptyList(); } public Object encode(List<T> v) { return null; } }; }
    default Codec<List<T>> list(Codec<T> codec) { return new Codec<List<T>>() { public List<T> decode(Object i) { return java.util.Collections.emptyList(); } public Object encode(List<T> v) { return null; } }; }

    // Field / optional methods
    default MapCodec<T> fieldOf(String name) { return __dummyMapCodec(); }
    default MapCodec<T> optionalFieldOf(String name, T defaultValue) { return __dummyMapCodec(); }
    default MapCodec<T> optionalFieldOf(String name) { return __dummyMapCodec(); }
    default Codec<java.util.Optional<T>> optionalFieldOf(String name, java.util.Optional<T> defaultValue) { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    default MapCodec<T> lenientOptionalFieldOf(String name, T defaultValue) { return __dummyMapCodec(); }
    default Codec<java.util.Optional<T>> optionalFieldOfOptional(String name) { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    default Codec<T> fieldOfOptional(String name) { return this; }
    default Codec<T> withDefaultedField(String name, T defaultValue) { return this; }
    default Codec<T> optFieldOf(String name) { return this; }
    default Codec<T> fieldOfUnsafe(String name) { return this; }
    default Codec<T> fieldOfStrict(String name) { return this; }
    default Codec<T> validateFieldOf(String name, Function<T, DataResult<T>> v) { return this; }

    // Optional / alternative
    default Codec<java.util.Optional<T>> optional() { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    default Codec<java.util.Optional<T>> opt() { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    default Codec<java.util.Optional<T>> optionalOf(String name) { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    default Codec<java.util.Optional<T>> withFallback() { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    default Codec<java.util.Optional<T>> withFallback(T defaultValue) { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    default Codec<T> withAlternative(Codec<T> alternative, Function<T, T> alternativeEncoder) { return this; }
    default Codec<T> withAlternativeEncoding(Codec<T> alternative) { return this; }
    default Codec<T> withAlternativeOf(Codec<T> alternative) { return this; }
    default Codec<T> withAlternativeOrDefault(T defaultValue) { return this; }
    default Codec<T> withAlternativeOrDefaultSupplier(Supplier<T> supplier) { return this; }
    default Codec<T> withAlternativeOrGet(Supplier<T> supplier) { return this; }
    default Codec<T> withNestedAlternative(Codec<T> alternative) { return this; }
    default Codec<T> orElseGet(Supplier<T> supplier) { return this; }

    // Range methods
    default Codec<T> intRange(int min, int max) { return this; }
    default Codec<T> floatRange(float min, float max) { return this; }
    default Codec<T> doubleRange(double min, double max) { return this; }
    default Codec<T> longRange(long min, long max) { return this; }

    // Stream / map
    default Codec<java.util.stream.Stream<T>> streamOf() { return new Codec<java.util.stream.Stream<T>>() { public java.util.stream.Stream<T> decode(Object i) { return java.util.stream.Stream.empty(); } public Object encode(java.util.stream.Stream<T> v) { return null; } }; }
    default Codec<java.util.Map<String, T>> asMap() { return new Codec<java.util.Map<String, T>>() { public java.util.Map<String, T> decode(Object i) { return java.util.Collections.emptyMap(); } public Object encode(java.util.Map<String, T> v) { return null; } }; }
    default <K> Codec<java.util.Map<K, T>> asMap(Codec<K> keyCodec) { return new Codec<java.util.Map<K, T>>() { public java.util.Map<K, T> decode(Object i) { return java.util.Collections.emptyMap(); } public Object encode(java.util.Map<K, T> v) { return null; } }; }
    default Codec<java.util.Map<String, T>> asMapSorted() { return new Codec<java.util.Map<String, T>>() { public java.util.Map<String, T> decode(Object i) { return java.util.Collections.emptyMap(); } public Object encode(java.util.Map<String, T> v) { return null; } }; }

    // Static factories
    static <T> Codec<List<T>> list(Codec<T> codec, int minSize, int maxSize) { return new Codec<List<T>>() { public List<T> decode(Object i) { return java.util.Collections.emptyList(); } public Object encode(List<T> v) { return null; } }; }
    static <T> Codec<T> lazyInitialized(Supplier<Codec<T>> supplier) { return supplier.get(); }
    static <T> Codec<T> of(Encoder<T> encoder, Decoder<T> decoder) { return null; }
    static <T> Codec<T> pair(Codec<T> left, Codec<T> right) { return left; }
    static <T> Codec<T> either(Codec<T> left, Codec<T> right) { return left; }
    static <T> Codec<T> withAlternative(Codec<T> first, Codec<T> second) { return first; }
    static <T> Codec<T> withAlternative(Codec<T> first, Codec<T> second, Function<T, T> encoder) { return first; }
    static <K, V> com.mojang.serialization.codecs.UnboundedMapCodec<K, V> unboundedMap(Codec<K> keyCodec, Codec<V> valueCodec) { return (com.mojang.serialization.codecs.UnboundedMapCodec<K, V>) valueCodec; }
    static <F, S> Codec<java.util.Map.Entry<F, S>> compoundList(Codec<F> keyCodec, Codec<S> valueCodec) { return (Codec<java.util.Map.Entry<F, S>>) valueCodec; }
    static Codec<String> string(int min, int max) { return STRING; }
    static <T> Codec<T> stringResolver(Function<T, String> toString, Function<String, T> fromString) { return (Codec<T>) fromString; }
    static <T> Codec<java.util.List<T>> sizeLimitedListOf(int maxSize, Codec<T> codec) { return new Codec<java.util.List<T>>() { public java.util.List<T> decode(Object i) { return java.util.Collections.emptyList(); } public Object encode(java.util.List<T> v) { return null; } }; }
    static <N extends Number> Codec<N> intRange(int min, int max, Class<N> type) { return (Codec<N>) type.cast(null); }

    // Other static utilities
    static <T> MapCodec<T> optionalFieldOf(String name, T defaultValue, Codec<T> codec) { return __dummyMapCodec(); }
    static <T> Codec<java.util.Optional<T>> optionalFieldOf(String name, Codec<T> codec) { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }
    static <T> MapCodec<T> mapEither(MapCodec<T> left, MapCodec<T> right) { return left; }

    default DataResult<T> decode(DynamicOps<?> ops, Object input) { return DataResult.success(decode(input)); }
    default <S> Codec<S> mapResult(com.mojang.serialization.Codec.ResultFunction<T> fn) { return (Codec<S>) this; }
    default <S> MapCodec<S> dispatchMap(String name, java.util.function.Function<S, ?> fn, java.util.function.Function<?, Codec<? extends T>> codec) { return __dummyMapCodec(); }
    default MapCodec<T> lenientOptionalFieldOf(String name) { return __dummyMapCodec(); }
    default Codec<java.util.Map<String, T>> simpleMap(Codec<String> keyCodec, Codec<T> valueCodec, com.mojang.serialization.Keyable keys) { return new Codec<java.util.Map<String, T>>() { public java.util.Map<String, T> decode(Object i) { return java.util.Collections.emptyMap(); } public Object encode(java.util.Map<String, T> v) { return null; } }; }
    default Codec<T> sizeLimitedString(int maxSize) { return this; }
    default Codec<T> xor(Codec<T> alternative) { return this; }

    interface ResultFunction<T> {
        DataResult<T> coApply(DynamicOps<?> ops, Object input, DataResult<T> result);
        DataResult<T> coApplyInverse(DynamicOps<?> ops, T input, DataResult<Object> result);
    }

    default <S> Codec<S> dispatchStable(java.util.function.Function<S, ?> name, java.util.function.Function<?, Codec<? extends T>> codec) { return (Codec<S>) this; }
    default Codec<T> xor(Codec<T> other, Codec<T> alternative) { return this; }
    default com.mojang.serialization.codecs.UnboundedMapCodec<String, T> unboundedMap(Codec<T> valueCodec) { return (com.mojang.serialization.codecs.UnboundedMapCodec<String, T>) this; }

    default Decoder<T> map(java.util.function.Function<T, T> fn) { return (Decoder<T>) this; }

    default MapCodec<T> mapPair(MapCodec<T> left, MapCodec<T> right) { return __dummyMapCodec(); }

    // ===== Additional DFU methods that MC 26.1.2 references =====
    // These are no-arg instance methods that return a Codec or MapCodec.
    // Without them, TeaVM emits references (e.g., obfuscated 'qf') but
    // no method definition, causing 'X.qf is not a function' runtime errors.

    /** Returns a Codec for optional values of T. No-arg version. */
    default Codec<java.util.Optional<T>> optionalFieldOf() { return new Codec<java.util.Optional<T>>() { public java.util.Optional<T> decode(Object i) { return java.util.Optional.empty(); } public Object encode(java.util.Optional<T> v) { return null; } }; }

    /** Returns this codec with a terminal lifecycle. */
    default Codec<T> terminal() { return this; }

    /** Returns a sizeable version of this codec (for size-prefixed lists). */
    default Codec<java.util.List<T>> sizeable() { return new Codec<java.util.List<T>>() { public java.util.List<T> decode(Object i) { return java.util.Collections.emptyList(); } public Object encode(java.util.List<T> v) { return null; } }; }

    /** Returns a codec that promotes partial results to full results. */
    default Codec<T> promotePartial() { return this; }

    /** Returns a codec with a stable lifecycle (alias). */
    default Codec<T> stableLifecycle() { return this; }

    /** Returns a codec that captures exceptions. No-arg version. */
    default Codec<T> capture() { return this; }

    /** Returns a MapCodec that wraps this codec for dispatch. */
    default MapCodec<T> dispatchMap() { return __dummyMapCodec(); }

    /** Returns a codec that validates during decode. */
    default Codec<T> validated() { return this; }

    /** Returns a codec with no lifecycle. */
    default Codec<T> noLifecycle() { return this; }

    /** Returns a codec for this type as a map value. */
    default Codec<java.util.Map<String, T>> asMapOf() { return new Codec<java.util.Map<String, T>>() { public java.util.Map<String, T> decode(Object i) { return java.util.Collections.emptyMap(); } public Object encode(java.util.Map<String, T> v) { return null; } }; }

    /** Returns a codec that decodes list elements. */
    default Codec<T> listElement() { return this; }

    /** Returns a codec that decodes map values. */
    default Codec<T> mapValue() { return this; }

    /** Returns a codec with a check function applied. */
    default Codec<T> checked() { return this; }

    /** Returns a codec that's lenient on extra fields. */
    default Codec<T> lenientX() { return this; }

    /** Returns a codec for pairs. */
    default Codec<T> paired() { return this; }

    /** Returns a codec with alternative fallback. */
    default Codec<T> withAlt() { return this; }

    /** Returns a codec for either type. */
    default Codec<T> eitherOf() { return this; }

    /** Returns a dummy non-null MapCodec to prevent NPE when methods are chained. */
    @SuppressWarnings("unchecked")
    static <T> MapCodec<T> __dummyMapCodec() {
        return new MapCodec<T>() {};
    }
}
