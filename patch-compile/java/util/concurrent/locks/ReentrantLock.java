package java.util.concurrent.locks;

/**
 * TeaVM-compatible stub for ReentrantLock.
 * Browser is single-threaded; no real locking needed.
 */
public class ReentrantLock implements Lock {
    private boolean locked = false;

    public ReentrantLock() {}
    public ReentrantLock(boolean fair) {}

    @Override public void lock() { locked = true; }
    @Override public void lockInterruptibly() { locked = true; }
    @Override public boolean tryLock() { locked = true; return true; }
    @Override public boolean tryLock(long time, java.util.concurrent.TimeUnit unit) { locked = true; return true; }
    @Override public void unlock() { locked = false; }
    @Override public java.util.concurrent.locks.Condition newCondition() { return new StubCondition(); }

    public boolean isHeldByCurrentThread() { return locked; }
    public boolean isLocked() { return locked; }
    public int getHoldCount() { return locked ? 1 : 0; }
}

class StubCondition implements java.util.concurrent.locks.Condition {
    @Override public void await() {}
    @Override public void awaitUninterruptibly() {}
    @Override public long awaitNanos(long nanosTimeout) { return 0; }
    @Override public boolean await(long time, java.util.concurrent.TimeUnit unit) { return true; }
    @Override public boolean awaitUntil(java.util.Date deadline) { return true; }
    @Override public void signal() {}
    @Override public void signalAll() {}
}
