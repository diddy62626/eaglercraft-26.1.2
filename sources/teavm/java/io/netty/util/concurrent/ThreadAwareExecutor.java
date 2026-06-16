package io.netty.util.concurrent;

public interface ThreadAwareExecutor extends java.util.concurrent.Executor {
    boolean inExecutorThread(Thread thread);
    default boolean isExecutorThread(Thread thread) { return inExecutorThread(thread); }
}
