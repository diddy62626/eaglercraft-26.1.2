package java.util.concurrent;
public abstract class AbstractExecutorService implements ExecutorService {
    public <T> java.util.concurrent.Future<T> submit(Callable<T> task) { return null; }
    public <T> java.util.concurrent.Future<T> submit(Runnable task, T result) { return null; }
    public java.util.concurrent.Future<?> submit(Runnable task) { return null; }
    public <T> java.util.List<java.util.concurrent.Future<T>> invokeAll(java.util.Collection<? extends Callable<T>> tasks) { return java.util.Collections.emptyList(); }
    public <T> java.util.List<java.util.concurrent.Future<T>> invokeAll(java.util.Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) { return java.util.Collections.emptyList(); }
    public <T> T invokeAny(java.util.Collection<? extends Callable<T>> tasks) { return null; }
    public <T> T invokeAny(java.util.Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) { return null; }
}
