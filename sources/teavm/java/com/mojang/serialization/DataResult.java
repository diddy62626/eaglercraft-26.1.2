package com.mojang.serialization;

public final class DataResult<T> {
    private final T value;
    private final String error;

    private DataResult(T value, String error) { this.value = value; this.error = error; }

    public static <T> DataResult<T> success(T value) { return new DataResult<>(value, null); }
    public static <T> DataResult<T> error(String message) { return new DataResult<>(null, message); }

    public T result() { return value; }
    public String error() { return error; }
    public boolean isSuccess() { return error == null; }

    public <S> DataResult<S> map(Function<T, S> fn) {
        return isSuccess() ? success(fn.apply(value)) : error(error);
    }
    public <S> DataResult<S> flatMap(Function<T, DataResult<S>> fn) {
        return isSuccess() ? fn.apply(value) : error(error);
    }

    public interface Function<T, R> { R apply(T t); }
}
