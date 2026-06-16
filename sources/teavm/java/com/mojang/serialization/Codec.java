package com.mojang.serialization;

import java.util.function.Function;

public interface Codec<T> {
    T decode(Object input);
    Object encode(T value);
    default <S> Codec<S> comap(Function<T, S> fn) { return null; }
    default <S> Codec<S> flatComap(Function<T, S> fn) { return null; }
}
