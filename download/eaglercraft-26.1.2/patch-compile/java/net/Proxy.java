package java.net;

public class Proxy {
    public static final Proxy NO_PROXY = new Proxy(Type.DIRECT, null);
    public enum Type { DIRECT, HTTP, SOCKS }
    private final Type type;
    private final SocketAddress address;
    public Proxy(Type type, SocketAddress address) { this.type = type; this.address = address; }
    public Type type() { return type; }
    public SocketAddress address() { return address; }
    public String toString() { return type == Type.DIRECT ? "DIRECT" : type + " @ " + address; }
}
