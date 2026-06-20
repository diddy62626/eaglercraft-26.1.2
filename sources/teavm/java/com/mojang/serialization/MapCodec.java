package com.mojang.serialization;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * EaglerCraft stub for MapCodec.
 *
 * STANDALONE interface — does NOT extend Codec.
 * This avoids name clashes for recursive() and optionalFieldOf().
 *
 * SimpleMapCodec extends BOTH MapCodec and Codec, but since MapCodec
 * no longer defines methods with the same erasure as Codec, there's
 * no conflict.
 */
public interface MapCodec<T> {

    /**
     * Static factory method that returns a default Codec instance.
     *
     * <p>MC code calls {@code MapCodec.codec()} as a no-arg static method
     * from static initializers like:
     * <pre>
     * public static final MapCodec&lt;Unit&gt; CODEC = MapCodec.codec();
     * </pre>
     *
     * <p>This is referenced from many class static initializers but the
     * real DFU implementation is in the MC JAR. Without this stub,
     * TeaVM emits references to {@code MapCodec.codec()} but never emits
     * the method definition (the actual DFU method gets DCE'd or has a
     * different signature), causing {@code 'MapCodec_codec is not defined'}
     * runtime crashes.
     */
    static <T> Codec<T> codec() {
        return new Codec<T>() {
            @Override public T decode(Object input) { return null; }
            @Override public Object encode(T value) { return null; }
        };
    }

    // NOTE: The instance (default) codec() method that returned
    // Codec.of(this) in real DFU has been REMOVED to avoid Java's
    // name-clash error (static and instance methods cannot share the
    // same erasure in an interface). The unitCodec() methods below
    // now call the static codec() method directly.

    default Encoder<T> encoder() { return (Encoder<T>) this; }
    default Decoder<T> decoder() { return (Decoder<T>) this; }
    default DataResult<T> decode(DynamicOps<?> ops, MapLike<?> input) { return DataResult.success(null); }
    default <S> MapCodec<S> flatXmap(Function<T, DataResult<S>> to, Function<S, DataResult<T>> from) { return (MapCodec<S>) this; }
    default <S> MapCodec<S> xmap(Function<T, S> to, Function<S, T> from) { return (MapCodec<S>) this; }
    default MapCodec<T> orElse(T defaultValue) { return this; }
    // NOTE: orElseGet removed — inherited from Codec via SimpleMapCodec
    default MapCodec<T> recursive(String name, Function<MapCodec<T>, MapCodec<T>> function) { return this; }
    default MapCodec<T> withLifecycle(Lifecycle lifecycle) { return this; }
    default MapCodec<T> stable() { return this; }
    default MapCodec<T> deprecated(int since) { return this; }
    default MapCodec<java.util.Optional<T>> optionalFieldOf(String name) { return (MapCodec<java.util.Optional<T>>) this; }
    default MapCodec<T> unit(T defaultValue) { return this; }
    default MapCodec<T> unit(Supplier<T> defaultValue) { return this; }
    default Codec<T> unitCodec(T defaultValue) { return codec(); }
    default Codec<T> unitCodec(Supplier<T> defaultValue) { return codec(); }
    default MapCodec<T> validate(Function<T, DataResult<T>> validator) { return this; }
    // NOTE: orElseGet(Consumer, Supplier) removed — causes clash with Codec.orElseGet

    default <S> Codec<S> dispatch(Function<T, String> name, Function<String, Codec<? extends T>> codec) { return codec(); }
    default MapCodec<T> orElseGet(java.util.function.Consumer<String> onError, java.util.function.Supplier<T> supplier) { return this; }
}
