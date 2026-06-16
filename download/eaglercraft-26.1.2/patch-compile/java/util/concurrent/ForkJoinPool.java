package java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * TeaVM-compatible stub for ForkJoinPool.
 * Browser is single-threaded; all tasks execute synchronously.
 */
public class ForkJoinPool extends AbstractExecutorService {
    @Override public void shutdown() {}
    @Override public List<Runnable> shutdownNow() { return new ArrayList<>(); }
    @Override public boolean isShutdown() { return false; }
    @Override public boolean isTerminated() { return false; }
    @Override public boolean awaitTermination(long timeout, TimeUnit unit) { return true; }
    @Override public void execute(Runnable command) { command.run(); }

    public static ForkJoinPool commonPool() { return new ForkJoinPool(); }
    public int getParallelism() { return 1; }
}
