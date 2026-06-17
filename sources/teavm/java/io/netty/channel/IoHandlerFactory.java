package io.netty.channel;

import io.netty.util.concurrent.ThreadAwareExecutor;

public interface IoHandlerFactory {
    IoHandler newHandler();
    default IoHandler newHandler(ThreadAwareExecutor executor) { return newHandler(); }
    default boolean isChangingThreadSupported() { return false; }
}
