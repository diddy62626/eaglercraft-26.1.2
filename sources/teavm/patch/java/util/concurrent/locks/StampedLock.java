package java.util.concurrent.locks;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class StampedLock implements Serializable {
    private static final long serialVersionUID = -6001602636862214147L;

    public StampedLock() {}

    public long writeLock() { return 0L; }
    public long tryWriteLock() { return 0L; }
    public long tryWriteLock(long time, TimeUnit unit) { return 0L; }
    public void unlockWrite(long stamp) {}
    public long readLock() { return 0L; }
    public long tryReadLock() { return 0L; }
    public long tryReadLock(long time, TimeUnit unit) { return 0L; }
    public void unlockRead(long stamp) {}
    public void unlock(long stamp) {}
    public boolean tryConvertToWriteLock(long stamp) { return false; }
    public boolean tryConvertToReadLock(long stamp) { return false; }
    public boolean tryConvertToOptimisticRead(long stamp) { return false; }
    public boolean tryUnlockWrite() { return false; }
    public boolean tryUnlockRead() { return false; }
    public boolean isWriteLocked() { return false; }
    public boolean isReadLocked() { return false; }
    public boolean validate(long stamp) { return true; }
    public Lock asReadLock() { return new ReentrantLock(); }
    public Lock asWriteLock() { return new ReentrantLock(); }
    public Lock asReadWriteLock() { return new ReentrantReadWriteLock().readLock(); }
}
