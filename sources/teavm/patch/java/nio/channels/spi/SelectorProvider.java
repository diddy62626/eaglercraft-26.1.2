package java.nio.channels.spi;

import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public abstract class SelectorProvider {
    protected SelectorProvider() {}

    public static SelectorProvider provider() {
        return new SelectorProvider() {
            @Override public DatagramChannel openDatagramChannel() { return null; }
            @Override public Pipe openPipe() { return null; }
            @Override public ServerSocketChannel openServerSocketChannel() { return null; }
            @Override public SocketChannel openSocketChannel() { return null; }
            @Override public AbstractSelector openSelector() { return null; }
        };
    }

    public abstract DatagramChannel openDatagramChannel();
    public abstract Pipe openPipe();
    public abstract ServerSocketChannel openServerSocketChannel();
    public abstract SocketChannel openSocketChannel();
    public abstract AbstractSelector openSelector();
    public Channel inheritedChannel() { return null; }
    public DatagramChannel openDatagramChannel(java.net.ProtocolFamily family) { return null; }
    public ServerSocketChannel openServerSocketChannel(java.net.ProtocolFamily family) { return null; }
    public SocketChannel openSocketChannel(java.net.ProtocolFamily family) { return null; }
}
