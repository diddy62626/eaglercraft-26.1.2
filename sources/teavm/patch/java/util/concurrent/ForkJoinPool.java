package java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * TeaVM-compatible stub for ForkJoinPool.
 * Browser is single-threaded; all tasks execute synchronously.
 *
 * Provides the 4-arg constructor (parallelism, workerThreadFactory,
 * uncaughtExceptionHandler, asyncMode) that MC's Util.makeExecutor uses.
 */
public class ForkJoinPool extends AbstractExecutorService {

    /** Stub interface for ForkJoinWorkerThreadFactory (matches MC's Util usage). */
    public interface ForkJoinWorkerThreadFactory {
        ForkJoinWorkerThread newThread(ForkJoinPool pool);
    }

    private final int parallelism;
    private final ForkJoinWorkerThreadFactory factory;
    private final Thread.UncaughtExceptionHandler ueh;
    private final boolean asyncMode;

    public ForkJoinPool() {
        this(Runtime.getRuntime().availableProcessors(),
             null, null, false);
    }

    public ForkJoinPool(int parallelism) {
        this(parallelism, null, null, false);
    }

    public ForkJoinPool(int parallelism,
                        ForkJoinWorkerThreadFactory factory,
                        Thread.UncaughtExceptionHandler handler,
                        boolean asyncMode) {
        this.parallelism = parallelism > 0 ? parallelism : 1;
        this.factory = factory != null ? factory : defaultForkJoinWorkerThreadFactory();
        this.ueh = handler;
        this.asyncMode = asyncMode;
    }

    private static ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory() {
        return pool -> new ForkJoinWorkerThread(pool);
    }

    @Override public void shutdown() {}
    @Override public List<Runnable> shutdownNow() { return new ArrayList<>(); }
    @Override public boolean isShutdown() { return false; }
    @Override public boolean isTerminated() { return false; }
    @Override public boolean awaitTermination(long timeout, TimeUnit unit) { return true; }
    @Override public void execute(Runnable command) {
        if (command != null) {
            try { command.run(); }
            catch (RuntimeException e) { if (ueh != null) ueh.uncaughtException(Thread.currentThread(), e); else throw e; }
        }
    }

    public static ForkJoinPool commonPool() {
        return CommonPoolHolder.COMMON;
    }

    private static class CommonPoolHolder {
        static final ForkJoinPool COMMON = new ForkJoinPool(1);
    }

    public int getParallelism() { return parallelism; }
    public int getPoolSize() { return parallelism; }
    public long getStealCount() { return 0; }
    public long getQueuedTaskCount() { return 0; }
    public boolean getAsyncMode() { return asyncMode; }
}
