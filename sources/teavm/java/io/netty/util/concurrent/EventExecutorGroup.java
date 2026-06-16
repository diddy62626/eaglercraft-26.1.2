package io.netty.util.concurrent;

public interface EventExecutorGroup extends java.util.concurrent.ScheduledExecutorService {
    boolean isShuttingDown();
    Future<?> shutdownGracefully();
    Future<?> shutdownGracefully(long quietPeriod, long timeout, java.util.concurrent.TimeUnit unit);
    Future<?> terminationFuture();
    EventExecutor next();
    java.util.Iterator<EventExecutor> iterator();
}
