package java.net;
public class InetSocketAddress extends SocketAddress {
    private final String host;
    private final int port;
    public InetSocketAddress(int port) { this("localhost", port); }
    public InetSocketAddress(String host, int port) { this.host = host; this.port = port; }
    public InetSocketAddress(InetAddress addr, int port) { this.host = addr.getHostAddress(); this.port = port; }
    public String getHostName() { return host; }
    public int getPort() { return port; }
    public InetAddress getAddress() { return new InetAddress(); }
}