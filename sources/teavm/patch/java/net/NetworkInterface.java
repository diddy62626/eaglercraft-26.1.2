package java.net;

import java.util.Enumeration;
import java.util.Collections;

/**
 * TeaVM stub for java.net.NetworkInterface.
 * Browser: no direct network interface access; returns empty/stub values.
 */
public class NetworkInterface {
    private final String name;

    private NetworkInterface(String name) { this.name = name; }

    public static NetworkInterface getByName(String name) throws java.net.SocketException {
        return null;
    }

    public static NetworkInterface getByInetAddress(InetAddress addr) throws java.net.SocketException {
        return null;
    }

    public static Enumeration<NetworkInterface> getNetworkInterfaces() throws java.net.SocketException {
        return Collections.emptyEnumeration();
    }

    public String getName() { return name; }
    public String getDisplayName() { return name; }
    public Enumeration<InetAddress> getInetAddresses() {
        return Collections.emptyEnumeration();
    }
    public boolean isUp() throws java.net.SocketException { return false; }
    public boolean isLoopback() throws java.net.SocketException { return false; }
    public boolean isPointToPoint() throws java.net.SocketException { return false; }
    public boolean isVirtual() { return false; }
    public boolean supportsMulticast() throws java.net.SocketException { return false; }
    public byte[] getHardwareAddress() throws java.net.SocketException { return new byte[0]; }
    public int getMTU() throws java.net.SocketException { return 0; }
    public int getIndex() { return 0; }
}
