package com.mojang.serialization;

public interface Keyable {
    <T> java.util.stream.Stream<T> keys(DynamicOps<T> ops);
}
