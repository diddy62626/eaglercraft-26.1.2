package java.util.concurrent.locks;
public class ReentrantReadWriteLock {
    private final ReadLock readerLock = new ReadLock(this);
    private final WriteLock writerLock = new WriteLock(this);
    public ReadLock readLock() { return readerLock; }
    public WriteLock writeLock() { return writerLock; }
    public static class ReadLock implements Lock { public ReadLock(ReentrantReadWriteLock lock) {} public void lock() {} public void unlock() {} public boolean tryLock() { return true; } public Condition newCondition() { return null; } public void lockInterruptibly() {} public boolean tryLock(long time, TimeUnit unit) { return true; } }
    public static class WriteLock implements Lock { public WriteLock(ReentrantReadWriteLock lock) {} public void lock() {} public void unlock() {} public boolean tryLock() { return true; } public Condition newCondition() { return null; } public void lockInterruptibly() {} public boolean tryLock(long time, TimeUnit unit) { return true; } }
}