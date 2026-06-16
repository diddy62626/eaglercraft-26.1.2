package java.util.concurrent;

/**
 * Stub ForkJoinWorkerThread. Browser is single-threaded so this just
 * wraps a Thread. Provides the constructor (ForkJoinPool) that
 * ForkJoinWorkerThreadFactory implementations expect to call.
 */
public class ForkJoinWorkerThread extends Thread {
    private final ForkJoinPool pool;

    public ForkJoinWorkerThread(ForkJoinPool pool) {
        super("ForkJoinPool-Worker");
        this.pool = pool;
        setDaemon(true);
    }

    public ForkJoinPool getPool() { return pool; }
    public int getPoolIndex() { return 0; }
}
