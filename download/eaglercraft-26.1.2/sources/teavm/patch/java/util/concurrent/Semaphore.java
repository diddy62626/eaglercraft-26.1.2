package java.util.concurrent;

/**
 * TeaVM stub for java.util.concurrent.Semaphore.
 * Browser is single-threaded; acquire()/release() just track a counter.
 */
public class Semaphore implements java.io.Serializable {

    private int permits;

    public Semaphore(int permits) {
        this.permits = permits;
    }

    public Semaphore(int permits, boolean fair) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        if (permits > 0) {
            permits--;
        }
        // In single-threaded browser, if no permits available we just proceed
    }

    public void acquireUninterruptibly() {
        if (permits > 0) {
            permits--;
        }
    }

    public boolean tryAcquire() {
        if (permits > 0) {
            permits--;
            return true;
        }
        return false;
    }

    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        return tryAcquire();
    }

    public void release() {
        permits++;
    }

    public void acquire(int permits) throws InterruptedException {
        this.permits -= permits;
        if (this.permits < 0) this.permits = 0;
    }

    public void acquireUninterruptibly(int permits) {
        this.permits -= permits;
        if (this.permits < 0) this.permits = 0;
    }

    public boolean tryAcquire(int permits) {
        if (this.permits >= permits) {
            this.permits -= permits;
            return true;
        }
        return false;
    }

    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
        return tryAcquire(permits);
    }

    public void release(int permits) {
        this.permits += permits;
    }

    public int availablePermits() {
        return permits;
    }

    public int drainPermits() {
        int old = permits;
        permits = 0;
        return old;
    }

    protected void reducePermits(int reduction) {
        permits -= reduction;
    }

    public boolean isFair() {
        return false;
    }

    public final boolean hasQueuedThreads() {
        return false;
    }

    public final int getQueueLength() {
        return 0;
    }
}
