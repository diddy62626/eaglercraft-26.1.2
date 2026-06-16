package java.util.concurrent.locks;

import java.util.concurrent.TimeUnit;

/**
 * TeaVM stub for ReentrantReadWriteLock.
 * Browser is single-threaded; lock()/unlock() are no-ops.
 */
public class ReentrantReadWriteLock implements ReadWriteLock, java.io.Serializable {
    private static final long serialVersionUID = -6992448646407690164L;

    private final Lock readLock = new NoopLock();
    private final Lock writeLock = new NoopLock();

    public ReentrantReadWriteLock() {}
    public ReentrantReadWriteLock(boolean fair) {}

    @Override
    public Lock readLock() { return readLock; }

    @Override
    public Lock writeLock() { return writeLock; }

    public boolean isFair() { return false; }
    public boolean isWriteLocked() { return false; }
    public boolean isWriteLockedByCurrentThread() { return false; }
    public int getWriteHoldCount() { return 0; }
    public int getReadHoldCount() { return 0; }
    public int getReadLockCount() { return 0; }
    public boolean hasQueuedThreads() { return false; }
    public boolean hasQueuedThread(Thread thread) { return false; }
    public int getQueueLength() { return 0; }
    public java.util.Collection<Thread> getQueuedThreads() { return new java.util.ArrayList<>(); }
    public boolean hasWaiters(Condition condition) { return false; }
    public int getWaitQueueLength(Condition condition) { return 0; }

    private static class NoopLock implements Lock {
        @Override public void lock() {}
        @Override public void lockInterruptibly() {}
        @Override public boolean tryLock() { return true; }
        @Override public boolean tryLock(long time, TimeUnit unit) { return true; }
        @Override public void unlock() {}
        @Override public Condition newCondition() { return new NoopCondition(); }
    }

    private static class NoopCondition implements Condition {
        @Override public void await() {}
        @Override public void awaitUninterruptibly() {}
        @Override public long awaitNanos(long nanosTimeout) { return 0; }
        @Override public boolean await(long time, TimeUnit unit) { return true; }
        @Override public boolean awaitUntil(java.util.Date deadline) { return true; }
        @Override public void signal() {}
        @Override public void signalAll() {}
    }
}
