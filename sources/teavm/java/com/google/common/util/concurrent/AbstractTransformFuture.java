package com.google.common.util.concurrent;

/**
 * EaglerCraft shadow stub for Guava's AbstractTransformFuture.
 * Replaces VarHandle usage with synchronized blocks.
 */
abstract class AbstractTransformFuture<I, O> extends AbstractFuture<O> {

    private volatile ListenableFuture<? extends I> inputFuture;
    private volatile boolean inputDone = false;

    public boolean setFuture(ListenableFuture<? extends I> future) {
        if (future == null) return false;
        synchronized (this) {
            if (inputDone) return false;
            this.inputFuture = future;
        }
        future.addListener(() -> {
            synchronized (this) {
                inputDone = true;
            }
            try {
                I result = future.get();
                O transformed = transform(result);
                set(transformed);
            } catch (Exception e) {
                setException(e);
            }
        }, Runnable::run);
        return true;
    }

    abstract O transform(I input) throws Exception;

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> inputFuture,
            com.google.common.base.Function<I, O> function,
            java.util.concurrent.Executor executor) {
        return new AbstractTransformFuture<I, O>() {
            @Override
            O transform(I input) {
                return function.apply(input);
            }
        };
    }
}
