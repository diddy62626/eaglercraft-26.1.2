package com.mojang.serialization;

public final class OptionalDynamic<T> {
    private final DynamicOps<T> ops;
    private final DataResult<Dynamic<T>> result;

    public OptionalDynamic(DynamicOps<T> ops, DataResult<Dynamic<T>> result) {
        this.ops = ops;
        this.result = result;
    }

    public DataResult<Dynamic<T>> get() { return result; }
    public Dynamic<T> orElse(Dynamic<T> defaultValue) {
        return result.resultOrNull() != null ? result.resultOrNull() : defaultValue;
    }
    public DataResult<T> getNumber() { return DataResult.success(null); }
    public DataResult<String> asString() { return DataResult.success(""); }
    public <U> OptionalDynamic<U> cast(DynamicOps<U> ops) { return new OptionalDynamic<>(ops, DataResult.success(null)); }
}
