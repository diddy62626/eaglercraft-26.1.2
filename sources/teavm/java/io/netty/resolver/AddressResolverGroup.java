package io.netty.resolver;

import java.net.SocketAddress;

/**
 * EaglerCraft stub for io.netty.resolver.AddressResolverGroup.
 *
 * Real class is abstract and parameterized by EventExecutor (from netty-transport).
 * To avoid requiring EventExecutor to be on the classpath at our stub compile
 * time, we use a generic Object placeholder for the executor parameter.
 *
 * @param <T> the address type
 */
public abstract class AddressResolverGroup<T extends SocketAddress> {
    public abstract AddressResolver<T> getResolver(Object executor);
    public void close() {}
}
