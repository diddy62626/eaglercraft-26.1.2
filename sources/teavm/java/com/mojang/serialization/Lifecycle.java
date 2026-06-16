package com.mojang.serialization;

public interface Lifecycle {
    Lifecycle STABLE = new Lifecycle() {};
    Lifecycle EXPERIMENTAL = new Lifecycle() {};
    Lifecycle DEPRECATED = new Lifecycle() {};
    static Lifecycle experimental() { return EXPERIMENTAL; }
    static Lifecycle stable() { return STABLE; }
    static Lifecycle deprecated() { return DEPRECATED; }

    default Lifecycle add(Lifecycle other) { return this; }
}
