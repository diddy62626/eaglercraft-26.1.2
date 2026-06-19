package com.google.common.util.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * EaglerCraft shadow stub for Guava's AbstractFutureState.
 *
 * The real Guava class uses java.lang.invoke.VarHandle for lock-free
 * atomic operations. TeaVM's classlib VarHandle doesn't support the
 * specific method signatures Guava uses (compareAndSet with Guava-
 * specific types). This stub replaces VarHandle with a simple
 * ReentrantLock, providing the same thread-safety guarantees without
 * requiring VarHandle support.
 *
 * This class is placed in the teavm source set so it's compiled and
 * put on the classpath BEFORE the real Guava JAR. TeaVM sees this
 * stub first and uses it instead of the real AbstractFutureState.
 */
abstract class AbstractFutureState {

    /** Lock protecting all mutable state in this future. */
    private final ReentrantLock lock = new ReentrantLock();

    /** The current listeners (replaces the VarHandle-backed listeners field). */
    private volatile Listener listeners;

    /** The current waiter (replaces the VarHandle-backed waiters field). */
    private volatile Waiter waiters;

    /** The current value (replaces the VarHandle-backed value field). */
    private volatile Object value;

    // ========== Listener management ==========

    /**
     * Atomically updates the listeners list.
     * Uses synchronized instead of VarHandle.compareAndSet.
     */
    final void casListeners(Listener oldListener, Listener newListener) {
        lock.lock();
        try {
            if (listeners == oldListener) {
                listeners = newListener;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets and sets the listeners list atomically.
     * Uses synchronized instead of VarHandle.getAndSet.
     */
    final Listener getAndSetListeners(Listener newListener) {
        lock.lock();
        try {
            Listener old = listeners;
            listeners = newListener;
            return old;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Clears the listeners list.
     */
    final Listener clearListeners() {
        return getAndSetListeners(null);
    }

    // ========== Waiter management ==========

    /**
     * Atomically updates the waiters list.
     * Uses synchronized instead of VarHandle.compareAndSet.
     */
    final void casWaiters(Waiter oldWaiter, Waiter newWaiter) {
        lock.lock();
        try {
            if (waiters == oldWaiter) {
                waiters = newWaiter;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets and sets the waiters list atomically.
     */
    final Waiter getAndSetWaiters(Waiter newWaiter) {
        lock.lock();
        try {
            Waiter old = waiters;
            waiters = newWaiter;
            return old;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the waiter's next pointer with release semantics.
     * Uses synchronized instead of VarHandle.setRelease.
     */
    final void setWaiterNext(Waiter waiter, Waiter next) {
        lock.lock();
        try {
            if (waiter != null) {
                waiter.next = next;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the waiter's thread with release semantics.
     */
    final void setWaiterThread(Waiter waiter, Thread thread) {
        lock.lock();
        try {
            if (waiter != null) {
                waiter.thread = thread;
            }
        } finally {
            lock.unlock();
        }
    }

    // ========== Value management ==========

    /**
     * Atomically updates the value.
     */
    final void casValue(Object oldValue, Object newValue) {
        lock.lock();
        try {
            if (value == oldValue) {
                value = newValue;
            }
        } finally {
            lock.unlock();
        }
    }

    final Object getValue() {
        return value;
    }

    final void setValue(Object value) {
        lock.lock();
        try {
            this.value = value;
        } finally {
            lock.unlock();
        }
    }

    // ========== Inner classes ==========

    /**
     * A node in the linked list of listeners waiting for the future to complete.
     */
    static final class Listener {
        volatile Listener next;
        final Runnable task;
        final java.util.concurrent.Executor executor;

        Listener(Runnable task, java.util.concurrent.Executor executor) {
            this.task = task;
            this.executor = executor;
        }
    }

    /**
     * A node in the linked list of threads waiting for the future to complete.
     */
    static final class Waiter {
        volatile Waiter next;
        volatile Thread thread;

        Waiter() {
            this.thread = Thread.currentThread();
        }
    }

    // ========== Abstract methods ==========

    abstract Object getValueInternal();
    abstract void complete(Object value);
    abstract boolean isDone();
}
