package io.netty.resolver;

import java.net.SocketAddress;

public interface AddressResolver<T extends SocketAddress> {
    boolean isResolved(SocketAddress address);
    io.netty.util.concurrent.Future<T> resolve(SocketAddress address);
    T resolve(String inetHost, int inetPort) throws Exception;

    default boolean isSupported(java.net.SocketAddress address) { return true; }
}
