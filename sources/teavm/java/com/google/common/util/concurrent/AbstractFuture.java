package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

/**
 * EaglerCraft shadow stub for Guava's AbstractFuture.
 *
 * The real Guava class extends AbstractFutureState and uses VarHandle
 * for atomic operations. This stub provides the same API using
 * synchronized blocks instead.
 *
 * This class is placed on the classpath BEFORE the real Guava JAR.
 */
abstract class AbstractFuture<V> extends AbstractFutureState implements ListenableFuture<V> {

    /** The result of the future, or null if not yet complete. */
    private volatile Object result;
    private volatile boolean cancelled = false;
    private volatile boolean done = false;

    // ========== Completion ==========

    public boolean set(V value) {
        synchronized (this) {
            if (done) return false;
            this.result = value;
            this.done = true;
            notifyAll();
        }
        executeListeners();
        return true;
    }

    public boolean setException(Throwable throwable) {
        synchronized (this) {
            if (done) return false;
            this.result = throwable;
            this.done = true;
            notifyAll();
        }
        executeListeners();
        return true;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (done) return false;
            this.cancelled = true;
            this.done = true;
            notifyAll();
        }
        executeListeners();
        return true;
    }

    // ========== Querying ==========

    public boolean isDone() {
        return done;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @SuppressWarnings("unchecked")
    public V get() throws InterruptedException, java.util.concurrent.ExecutionException {
        synchronized (this) {
            while (!done) {
                wait();
            }
        }
        if (result instanceof Throwable) {
            throw new java.util.concurrent.ExecutionException((Throwable) result);
        }
        return (V) result;
    }

    @SuppressWarnings("unchecked")
    public V get(long timeout, java.util.concurrent.TimeUnit unit)
            throws InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
        long nanos = unit.toNanos(timeout);
        long deadline = System.nanoTime() + nanos;
        synchronized (this) {
            while (!done) {
                long remaining = deadline - System.nanoTime();
                if (remaining <= 0) {
                    throw new java.util.concurrent.TimeoutException();
                }
                wait(remaining / 1_000_000, (int) (remaining % 1_000_000));
            }
        }
        if (result instanceof Throwable) {
            throw new java.util.concurrent.ExecutionException((Throwable) result);
        }
        return (V) result;
    }

    // ========== Listeners ==========

    public void addListener(Runnable listener, Executor executor) {
        if (listener == null || executor == null) return;
        synchronized (this) {
            if (done) {
                executeListener(listener, executor);
                return;
            }
            Listener newListener = new Listener(listener, executor);
            newListener.next = clearListeners();
            getAndSetListeners(newListener);
        }
    }

    private void executeListeners() {
        Listener head = clearListeners();
        while (head != null) {
            Listener next = head.next;
            executeListener(head.task, head.executor);
            head = next;
        }
    }

    private static void executeListener(Runnable listener, Executor executor) {
        try {
            executor.execute(listener);
        } catch (RuntimeException e) {
            // Swallow executor exceptions
        }
    }

    // ========== AbstractFutureState implementation ==========

    @Override
    Object getValueInternal() {
        return result;
    }

    @Override
    void complete(Object value) {
        if (value instanceof Throwable) {
            setException((Throwable) value);
        } else {
            @SuppressWarnings("unchecked")
            V v = (V) value;
            set(v);
        }
    }

}
