package java.util.concurrent;
public class ThreadPoolExecutor extends AbstractExecutorService {
    public ThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {}
    public void execute(Runnable command) { command.run(); }
    public void shutdown() {}
    public java.util.List<Runnable> shutdownNow() { return java.util.Collections.emptyList(); }
    public boolean isShutdown() { return false; }
    public boolean isTerminated() { return false; }
    public boolean awaitTermination(long timeout, TimeUnit unit) { return true; }
}
