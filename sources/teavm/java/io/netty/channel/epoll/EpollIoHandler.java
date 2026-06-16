package io.netty.channel.epoll;

public final class EpollIoHandler implements io.netty.channel.IoHandler {
    public EpollIoHandler() {}
    public void initialize() {}
    public void destroy() {}

    public static io.netty.channel.IoHandlerFactory newFactory() {
        return () -> new EpollIoHandler();
    }
}
