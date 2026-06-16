package io.netty.util.concurrent;

public interface ProgressivePromise<V> extends Promise<V> {
    void setProgress(long progress, long total);
    boolean tryProgress(long progress, long total);
}
