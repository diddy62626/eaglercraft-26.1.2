package java.util.concurrent.locks;

/**
 * TeaVM stub for ReadWriteLock.
 * Browser is single-threaded; locks are no-ops.
 */
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
