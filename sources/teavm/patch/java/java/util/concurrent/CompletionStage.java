package java.util.concurrent;
public interface CompletionStage<T> {
    CompletionStage<?> thenAccept(java.util.function.Consumer<? super T> action);
    CompletionStage<?> thenRun(Runnable action);
    <U> CompletionStage<U> thenApply(java.util.function.Function<? super T,? extends U> fn);
    <U> CompletionStage<U> thenCompose(java.util.function.Function<? super T,? extends CompletionStage<U>> fn);
    CompletionStage<T> exceptionally(java.util.function.Function<Throwable,? extends T> fn);
}