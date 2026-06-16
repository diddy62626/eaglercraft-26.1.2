package io.netty.resolver;

import java.net.SocketAddress;

public interface AddressResolver<T extends SocketAddress> {
    boolean isResolved(SocketAddress address);
    T resolve(SocketAddress address) throws Exception;
    T resolve(String inetHost, int inetPort) throws Exception;

    default boolean isSupported(java.net.SocketAddress address) { return true; }

    default io.netty.util.concurrent.Future<T> resolve(java.net.SocketAddress address) { return null; }
}
