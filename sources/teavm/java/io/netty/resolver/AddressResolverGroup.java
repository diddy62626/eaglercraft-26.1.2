package io.netty.resolver;

import io.netty.channel.EventExecutor;

public abstract class AddressResolverGroup<T extends java.net.SocketAddress> {
    public abstract AddressResolver<T> getResolver(EventExecutor executor);
    public void close() {}
}
