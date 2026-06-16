package io.netty.channel.epoll;

import io.netty.channel.socket.SocketChannel;

public class EpollSocketChannel implements SocketChannel {
    // Stub - native transport not available in browser
    public EpollSocketChannel() {}
    @Override public boolean isOpen() { return false; }
}
