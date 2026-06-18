package java.util.concurrent;

import java.util.function.Supplier;

/**
 * Enhanced CompletableFuture stub with supplyAsync support.
 */
public class CompletableFuture<T> implements Future<T> {

    private volatile Object resultValue;
    private volatile boolean completed = false;

    private static class AltResult {
        final Throwable ex;
        AltResult(Throwable ex) { this.ex = ex; }
    }

    public CompletableFuture() {}

    @SuppressWarnings("unchecked")
    private T value() {
        if (resultValue instanceof AltResult) return null;
        return (T) resultValue;
    }

    private Throwable exception() {
        if (resultValue instanceof AltResult) return ((AltResult) resultValue).ex;
        return null;
    }

    public boolean complete(T value) {
        if (completed) return false;
        this.resultValue = value; this.completed = true; return true;
    }

    public boolean completeExceptionally(Throwable ex) {
        if (completed) return false;
        this.resultValue = new AltResult(ex); this.completed = true; return true;
    }

    @Override public boolean cancel(boolean mayInterruptIfRunning) { return false; }
    @Override public boolean isCancelled() { return false; }
    @Override public boolean isDone() { return completed; }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        Throwable ex = exception();
        if (ex != null) throw new ExecutionException(ex);
        return value();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return get();
    }

    public T join() {
        Throwable ex = exception();
        if (ex != null) {
            if (ex instanceof RuntimeException) throw (RuntimeException) ex;
            throw new CompletionException(ex);
        }
        return value();
    }

    public T getNow(T valueIfAbsent) { return completed ? value() : valueIfAbsent; }

    public static <U> CompletableFuture<U> completedFuture(U value) {
        CompletableFuture<U> f = new CompletableFuture<>(); f.complete(value); return f;
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        CompletableFuture<U> f = new CompletableFuture<>();
        try { f.complete(supplier.get()); } catch (Throwable t) { f.completeExceptionally(t); }
        return f;
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        CompletableFuture<U> f = new CompletableFuture<>();
        try {
            executor.execute(() -> {
                try { f.complete(supplier.get()); } catch (Throwable t) { f.completeExceptionally(t); }
            });
        } catch (Throwable t) { f.completeExceptionally(t); }
        return f;
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        CompletableFuture<Void> f = new CompletableFuture<>();
        try { runnable.run(); f.complete(null); } catch (Throwable t) { f.completeExceptionally(t); }
        return f;
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        CompletableFuture<Void> f = new CompletableFuture<>();
        try {
            executor.execute(() -> {
                try { runnable.run(); f.complete(null); } catch (Throwable t) { f.completeExceptionally(t); }
            });
        } catch (Throwable t) { f.completeExceptionally(t); }
        return f;
    }

    public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs) {
        CompletableFuture<Void> result = new CompletableFuture<>(); result.complete(null); return result;
    }

    public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs) {
        CompletableFuture<Object> result = new CompletableFuture<>(); return result;
    }

    public <U> CompletableFuture<U> thenApply(java.util.function.Function<? super T, ? extends U> fn) {
        CompletableFuture<U> f = new CompletableFuture<>();
        if (completed) {
            try { f.complete(fn.apply(value())); } catch (Throwable t) { f.completeExceptionally(t); }
        }
        return f;
    }

    public CompletableFuture<Void> thenAccept(java.util.function.Consumer<? super T> action) {
        CompletableFuture<Void> f = new CompletableFuture<>();
        if (completed) {
            try { action.accept(value()); f.complete(null); } catch (Throwable t) { f.completeExceptionally(t); }
        }
        return f;
    }

    public CompletableFuture<Void> thenRun(Runnable action) {
        CompletableFuture<Void> f = new CompletableFuture<>();
        if (completed) {
            try { action.run(); f.complete(null); } catch (Throwable t) { f.completeExceptionally(t); }
        }
        return f;
    }

    public <U> CompletableFuture<U> thenCompose(java.util.function.Function<? super T, ? extends CompletableFuture<U>> fn) {
        CompletableFuture<U> f = new CompletableFuture<>();
        if (completed) {
            try { CompletableFuture<U> result = fn.apply(value()); if (result != null) return result; } catch (Throwable t) { f.completeExceptionally(t); }
        }
        return f;
    }

    public <U> CompletableFuture<U> exceptionally(java.util.function.Function<Throwable, ? extends U> fn) {
        CompletableFuture<U> f = new CompletableFuture<>();
        Throwable ex = exception();
        if (ex != null) {
            try { f.complete(fn.apply(ex)); } catch (Throwable t) { f.completeExceptionally(t); }
        } else if (completed) {
            @SuppressWarnings("unchecked")
            U val = (U) value();
            f.complete(val);
        }
        return f;
    }

    public CompletableFuture<T> whenComplete(java.util.function.BiConsumer<? super T, ? super Throwable> action) {
        if (completed) { action.accept(value(), exception()); }
        return this;
    }

    public <U> CompletableFuture<U> handle(java.util.function.BiFunction<? super T, Throwable, ? extends U> fn) {
        CompletableFuture<U> f = new CompletableFuture<>();
        if (completed) {
            try { f.complete(fn.apply(value(), exception())); } catch (Throwable t) { f.completeExceptionally(t); }
        }
        return f;
    }

    public boolean isCompletedExceptionally() {
        return completed && resultValue instanceof AltResult;
    }

    public CompletableFuture<T> completeOnTimeout(T value, long timeout, TimeUnit unit) { return this; }
}

class StubFuture<V> implements Future<V> {
    @Override public boolean cancel(boolean mayInterruptIfRunning) { return false; }
    @Override public boolean isCancelled() { return false; }
    @Override public boolean isDone() { return true; }
    @Override public V get() { return null; }
    @Override public V get(long timeout, TimeUnit unit) { return null; }
}

class StubScheduledFuture<V> implements ScheduledFuture<V> {
    @Override public long getDelay(TimeUnit unit) { return 0; }
    @Override public int compareTo(ScheduledFuture<V> o) { return 0; }
    @Override public boolean cancel(boolean mayInterruptIfRunning) { return false; }
    @Override public boolean isCancelled() { return false; }
    @Override public boolean isDone() { return true; }
    @Override public V get() { return null; }
    @Override public V get(long timeout, TimeUnit unit) { return null; }
}
