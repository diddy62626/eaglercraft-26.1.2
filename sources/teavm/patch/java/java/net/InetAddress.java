package java.net;
public class InetAddress {
    public static InetAddress getByName(String host) { return new InetAddress(); }
    public static InetAddress getLocalHost() { return new InetAddress(); }
    public static InetAddress[] getAllByName(String host) { return new InetAddress[0]; }
    public String getHostAddress() { return "127.0.0.1"; }
    public String getHostName() { return "localhost"; }
    public boolean isLoopbackAddress() { return true; }
}