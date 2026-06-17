package com.mojang.serialization;

public interface MapLike<T> {
    T get(String key);
    T get(T key);
    java.util.stream.Stream<java.util.Map.Entry<T, T>> entries();
}
