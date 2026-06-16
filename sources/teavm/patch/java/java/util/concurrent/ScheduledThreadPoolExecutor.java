package java.util.concurrent;
public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {
    public ScheduledThreadPoolExecutor(int corePoolSize) { super(corePoolSize, corePoolSize, 0L, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>()); }
}