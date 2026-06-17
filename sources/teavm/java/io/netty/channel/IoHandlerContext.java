package io.netty.channel;

public interface IoHandlerContext {
    boolean canBlock();
    void execute(Runnable task);
    void executeAfterEventLoop(Runnable task);
    void execute(Runnable task, long delayNanos);
    long delayNanos(Runnable task);
    default long delayNanos(long delayNanos) { return delayNanos; }
    default void reportActiveIoTime(long nanoTime) {}
    default boolean shouldReportActiveIoTime() { return false; }
}
