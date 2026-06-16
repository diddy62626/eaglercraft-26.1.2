package io.netty.channel.kqueue;

public final class KQueueIoHandler implements io.netty.channel.IoHandler {
    public KQueueIoHandler() {}
    public void initialize() {}
    public void destroy() {}

    public static io.netty.channel.IoHandlerFactory newFactory() {
        return () -> new KQueueIoHandler();
    }
}
