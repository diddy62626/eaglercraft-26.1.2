package com.mojang.serialization;

public final class Dynamic<T> {
    private final DynamicOps<T> ops;
    private final T value;

    public Dynamic(DynamicOps<T> ops, T value) {
        this.ops = ops;
        this.value = value;
    }

    public DynamicOps<T> getOps() { return ops; }
    public T getValue() { return value; }

    public <U> Dynamic<U> convert(DynamicOps<U> toOps) { return new Dynamic<>(toOps, null); }
    public Dynamic<T> map(Function<T, T> fn) { return new Dynamic<>(ops, fn.apply(value)); }
    public DataResult<Dynamic<T>> get(String key) { return DataResult.success(this); }
    public DataResult<Number> asNumber() { return DataResult.success(0); }
    public DataResult<String> asString() { return DataResult.success(""); }
    public DataResult<java.util.List<Dynamic<T>>> asStream() { return DataResult.success(new java.util.ArrayList<>()); }
    public DataResult<java.util.Optional<Dynamic<T>>> getElement(String key) { return DataResult.success(java.util.Optional.empty()); }
    public Dynamic<T> emptyList() { return new Dynamic<>(ops, ops.emptyList()); }
    public Dynamic<T> emptyMap() { return new Dynamic<>(ops, ops.emptyMap()); }

    public interface Function<T, R> { R apply(T t); }
}
