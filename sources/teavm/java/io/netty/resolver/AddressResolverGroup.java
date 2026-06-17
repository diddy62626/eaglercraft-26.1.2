package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import java.net.SocketAddress;

public abstract class AddressResolverGroup<T extends SocketAddress> {
    public abstract AddressResolver<T> getResolver(EventExecutor executor);
    public void close() {}
}
