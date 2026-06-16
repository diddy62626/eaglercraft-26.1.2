package io.netty.util.concurrent;

public interface Promise<V> extends Future<V> {
    Promise<V> setSuccess(V result);
    boolean trySuccess(V result);
    Promise<V> setFailure(Throwable cause);
    boolean tryFailure(Throwable cause);
    boolean setUncancellable();
    Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);
}
