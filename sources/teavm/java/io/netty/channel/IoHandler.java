package io.netty.channel;

public interface IoHandler {
    default void initialize() {}
    default void destroy() {}
    default void prepareToDestroy() {}
    default void wakeup() {}
}
