package io.netty.util.concurrent;

public interface EventExecutor extends EventExecutorGroup {
    <V> Promise<V> newPromise();
    <V> ProgressivePromise<V> newProgressivePromise();
    <V> Future<V> newSucceededFuture(V result);
    <V> Future<V> newFailedFuture(Throwable cause);
    boolean inEventLoop();
    boolean inEventLoop(Thread thread);
}
