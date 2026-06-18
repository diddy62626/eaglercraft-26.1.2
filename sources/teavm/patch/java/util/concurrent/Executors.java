package java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TeaVM-compatible stub for Executors.
 * Browser is single-threaded; all tasks execute synchronously.
 */
public class Executors {
    private Executors() {}

    public static ExecutorService newFixedThreadPool(int n) { return newFixedThreadPool(n, null); }
    public static ExecutorService newFixedThreadPool(int n, ThreadFactory threadFactory) {
        return new AbstractExecutorService() {
            @Override public void shutdown() {}
            @Override public List<Runnable> shutdownNow() { return new ArrayList<>(); }
            @Override public boolean isShutdown() { return false; }
            @Override public boolean isTerminated() { return false; }
            @Override public boolean awaitTermination(long timeout, TimeUnit unit) { return true; }
            @Override public void execute(Runnable command) { command.run(); }
        };
    }
    public static ExecutorService newCachedThreadPool() { return newFixedThreadPool(1); }
    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) { return newFixedThreadPool(1, threadFactory); }
    public static ExecutorService newSingleThreadExecutor() { return newFixedThreadPool(1); }
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) { return newFixedThreadPool(1, threadFactory); }
    public static ExecutorService newWorkStealingPool() { return new ForkJoinPool(); }
    public static ExecutorService newWorkStealingPool(int parallelism) { return new ForkJoinPool(parallelism); }
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) { return newScheduledThreadPool(); }
    public static ScheduledExecutorService newScheduledThreadPool() {
        return new ScheduledExecutorService() {
            @Override public void shutdown() {}
            @Override public List<Runnable> shutdownNow() { return new ArrayList<>(); }
            @Override public boolean isShutdown() { return false; }
            @Override public boolean isTerminated() { return false; }
            @Override public boolean awaitTermination(long timeout, TimeUnit unit) { return true; }
            @Override public void execute(Runnable command) { command.run(); }
            @Override public <T> Future<T> submit(Callable<T> task) { return new StubFuture<>(); }
            @Override public Future<?> submit(Runnable task) { task.run(); return new StubFuture<>(); }
            @Override public <T> Future<T> submit(Runnable task, T result) { task.run(); return new StubFuture<>(); }
            @Override public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) { command.run(); return new StubScheduledFuture<>(); }
            @Override public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) { return new StubScheduledFuture<>(); }
            @Override public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) { return new StubScheduledFuture<>(); }
            @Override public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) { return new StubScheduledFuture<>(); }
            @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
                List<Future<T>> results = new ArrayList<>();
                for (Callable<T> task : tasks) { results.add(submit(task)); }
                return results;
            }
            @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
                for (Callable<T> task : tasks) {
                    try { return task.call(); } catch (Exception e) { continue; }
                }
                throw new RuntimeException("No task completed successfully");
            }
        };
    }

    public static ThreadFactory defaultThreadFactory() {
        return new ThreadFactory() {
            @Override public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        };
    }
}
