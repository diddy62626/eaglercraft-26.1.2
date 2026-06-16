package io.netty.resolver;

import io.netty.channel.EventExecutor;

public final class DefaultAddressResolverGroup extends AddressResolverGroup<java.net.InetSocketAddress> {
    public static final DefaultAddressResolverGroup INSTANCE = new DefaultAddressResolverGroup();

    @Override
    public AddressResolver<java.net.InetSocketAddress> getResolver(EventExecutor executor) {
        return new AddressResolver<java.net.InetSocketAddress>() {
            @Override
            public boolean isResolved(java.net.SocketAddress address) { return true; }
            @Override
            public java.net.InetSocketAddress resolve(java.net.SocketAddress address) { return (java.net.InetSocketAddress) address; }
            @Override
            public java.net.InetSocketAddress resolve(String inetHost, int inetPort) {
                return new java.net.InetSocketAddress(inetHost, inetPort);
            }
        };
    }
}
