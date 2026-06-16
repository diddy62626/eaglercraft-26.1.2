package java.util.concurrent;
public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {
    public ScheduledThreadPoolExecutor(int corePoolSize) { super(corePoolSize, corePoolSize, 0L, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>()); }
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) { return null; }
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) { return null; }
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) { return null; }
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) { return null; }

    public ScheduledThreadPoolExecutor(int corePoolSize, java.util.concurrent.ThreadFactory threadFactory) {
        super(corePoolSize, Integer.MAX_VALUE, 0L, java.util.concurrent.TimeUnit.NANOSECONDS, new java.util.concurrent.DelayQueue<>(), threadFactory);
    }
    public ScheduledThreadPoolExecutor(int corePoolSize, java.util.concurrent.RejectedExecutionHandler handler) {
        super(corePoolSize, Integer.MAX_VALUE, 0L, java.util.concurrent.TimeUnit.NANOSECONDS, new java.util.concurrent.DelayQueue<>(), handler);
    }
    public ScheduledThreadPoolExecutor(int corePoolSize, java.util.concurrent.ThreadFactory threadFactory, java.util.concurrent.RejectedExecutionHandler handler) {
        super(corePoolSize, Integer.MAX_VALUE, 0L, java.util.concurrent.TimeUnit.NANOSECONDS, new java.util.concurrent.DelayQueue<>(), threadFactory, handler);
    }
    public java.util.concurrent.BlockingQueue<Runnable> getQueue() {
        return getQueue();
    }

}
