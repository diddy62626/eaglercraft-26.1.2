package java.util.concurrent;

/**
 * Browser-compatible stub for CompletableFuture.
 * Only provides methods actually used by Minecraft 26.1.2.
 * Does NOT implement CompletionStage (too many async methods).
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

    public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs) {
        CompletableFuture<Void> result = new CompletableFuture<>(); result.complete(null); return result;
    }

    public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs) {
        CompletableFuture<Object> result = new CompletableFuture<>(); return result;
    }
}
