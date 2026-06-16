package java.net;
public class Proxy {
    public enum Type { DIRECT, HTTP, SOCKS }
    public static final Proxy NO_PROXY = new Proxy(Type.DIRECT, null);
    private final Type type;
    private final SocketAddress addr;
    public Proxy(Type type, SocketAddress addr) { this.type = type; this.addr = addr; }
    public Type type() { return type; }
    public SocketAddress address() { return addr; }
}