package io.netty.channel.epoll;

/**
 * EaglerCraft stub for EpollSocketChannel.
 * Browser: native epoll transport not available - this is just a marker class.
 * Not implementing SocketChannel since we'd need to override too many abstract methods.
 */
public class EpollSocketChannel {
    public EpollSocketChannel() {}
    public boolean isOpen() { return false; }
    public void close() {}
}
