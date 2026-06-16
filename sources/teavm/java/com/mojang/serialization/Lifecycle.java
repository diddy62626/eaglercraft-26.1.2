package com.mojang.serialization;

public interface Lifecycle {
    Lifecycle STABLE = new Lifecycle() {};
    Lifecycle EXPERIMENTAL = new Lifecycle() {};
    Lifecycle DEPRECATED = new Lifecycle() {};
}
