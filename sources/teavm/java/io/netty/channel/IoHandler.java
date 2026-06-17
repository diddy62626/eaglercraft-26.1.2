package io.netty.channel;

public interface IoHandler {
    default void initialize() {}
    default void destroy() {}
    default void prepareToDestroy() {}
    default void wakeup() {}
    default int run(IoHandlerContext context) { return 0; }
}
