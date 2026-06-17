package java.util.concurrent.locks;

import java.util.Date;

/**
 * TeaVM-compatible stub for Condition interface.
 */
public interface Condition {
    void await() throws InterruptedException;
    void awaitUninterruptibly();
    long awaitNanos(long nanosTimeout) throws InterruptedException;
    boolean await(long time, java.util.concurrent.TimeUnit unit) throws InterruptedException;
    boolean awaitUntil(Date deadline) throws InterruptedException;
    void signal();
    void signalAll();
}
