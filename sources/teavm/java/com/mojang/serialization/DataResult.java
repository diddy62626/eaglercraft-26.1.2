package com.mojang.serialization;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DataResult<T> {
    private final T value;
    private final String errorMessage;
    private final T partial;

    private DataResult(T value, T partial, String error) {
        this.value = value;
        this.partial = partial;
        this.errorMessage = error;
    }

    public static <T> DataResult<T> success(T value) { return new DataResult<>(value, null, null); }
    public static <T> DataResult<T> success(T value, Lifecycle lifecycle) { return success(value); }
    public static <T> DataResult<T> error(String message) { return new DataResult<>(null, null, message); }
    public static <T> DataResult<T> error(Supplier<String> message) { return new DataResult<>(null, null, message.get()); }
    public static <T> DataResult<T> error(Supplier<String> message, T partial) { return new DataResult<>(null, partial, message.get()); }
    public static <T> DataResult<T> error(Supplier<String> message, T partial, Lifecycle lifecycle) { return new DataResult<>(null, partial, message.get()); }
    public static <T> DataResult<T> partial(T partial, String message) { return new DataResult<>(null, partial, message); }
    public static <T> DataResult<T> partial(T partial, Supplier<String> message) { return new DataResult<>(null, partial, message.get()); }

    public Optional<T> result() { return Optional.ofNullable(value); }
    public T resultOrNull() { return value; }
    public Optional<String> error() { return Optional.ofNullable(errorMessage); }
    public String errorOrNull() { return errorMessage; }

    public T getOrThrow() throws RuntimeException {
        if (errorMessage != null) throw new RuntimeException(errorMessage);
        return value;
    }
    public T getOrThrow(Function<String, ? extends RuntimeException> exceptionFactory) throws RuntimeException {
        if (errorMessage != null) throw exceptionFactory.apply(errorMessage);
        return value;
    }
    public Optional<T> resultOrPartial(Consumer<String> onError) {
        if (errorMessage != null) onError.accept(errorMessage);
        return Optional.ofNullable(value != null ? value : partial);
    }
    public boolean isError() { return errorMessage != null; }
    public boolean isSuccess() { return errorMessage == null; }

    public <S> DataResult<S> map(Function<T, S> fn) {
        return errorMessage != null ? error(errorMessage) : success(fn.apply(value));
    }
    public <S> DataResult<S> flatMap(Function<T, DataResult<S>> fn) {
        return errorMessage != null ? error(errorMessage) : fn.apply(value);
    }
    public <S> S mapOrElse(Function<T, S> onSuccess, Function<String, S> onError) {
        return errorMessage != null ? onError.apply(errorMessage) : onSuccess.apply(value);
    }
    public DataResult<T> ifError(Consumer<String> onError) {
        if (errorMessage != null) onError.accept(errorMessage);
        return this;
    }
    public DataResult<T> ifSuccess(Consumer<T> onSuccess) {
        if (errorMessage == null) onSuccess.accept(value);
        return this;
    }
    public DataResult<T> ifErrorOrElse(Consumer<String> onError, Runnable onSuccess) {
        if (errorMessage != null) onError.accept(errorMessage);
        else onSuccess.run();
        return this;
    }
    public DataResult<T> promote() { return this; }
    public DataResult<T> promoteOnlyOnError() { return this; }
    public DataResult<T> setPartial(T partial) {
        return new DataResult<>(value, partial, errorMessage);
    }
    public DataResult<T> withLifecycle(Lifecycle lifecycle) { return this; }
    public Lifecycle lifecycle() { return Lifecycle.STABLE; }
    public DataResult<T> apply2(BiFunction<T, T, T> fn, DataResult<T> other) { return this; }
    public DataResult<T> apply2stable(BiFunction<T, T, T> fn, DataResult<T> other) { return this; }
    public DataResult<T> apply3(TriFunction<T, T, T, T> fn, DataResult<T> second, DataResult<T> third) { return this; }
    public DataResult<T> ap(DataResult<Function<T, T>> fnResult) { return this; }
    public DataResult<T> ap2(DataResult<java.util.function.BiFunction<T, T, T>> fnResult, DataResult<T> second) { return this; }

    public interface BiFunction<T, U, R> { R apply(T t, U u); }
    public interface TriFunction<T, U, V, R> { R apply(T t, U u, V v); }

    public DataResult<T> setLifecycle(Lifecycle lifecycle) { return this; }
    public T getPartialOrThrow(java.util.function.Function<String, ? extends RuntimeException> exceptionFactory) {
        if (errorMessage != null) throw exceptionFactory.apply(errorMessage);
        return partial != null ? partial : value;
    }

    public DataResult<T> apply2stable(java.util.function.BiFunction<T, T, T> fn, DataResult<T> other) { return this; }
}
