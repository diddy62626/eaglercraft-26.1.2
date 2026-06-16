package io.netty.resolver;

/**
 * EaglerCraft stub for io.netty.resolver.DefaultAddressResolverGroup.
 *
 * Returns a basic AddressResolver that wraps the address without doing
 * any DNS lookup (browser handles DNS).
 */
public final class DefaultAddressResolverGroup extends AddressResolverGroup<java.net.InetSocketAddress> {
    public static final DefaultAddressResolverGroup INSTANCE = new DefaultAddressResolverGroup();

    @Override
    public AddressResolver<java.net.InetSocketAddress> getResolver(Object executor) {
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
