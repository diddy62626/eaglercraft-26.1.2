package javax.net.ssl;

import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;

/**
 * TeaVM stub for javax.net.ssl.SSLSocketFactory.
 */
public abstract class SSLSocketFactory {
    public abstract Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException;
    public abstract String[] getDefaultCipherSuites();
    public abstract String[] getSupportedCipherSuites();
    public Socket createSocket() throws IOException { return new Socket(); }
    public Socket createSocket(String host, int port) throws IOException { return new Socket(host, port); }
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return new Socket(host, port, localHost, localPort);
    }
    public Socket createSocket(InetAddress host, int port) throws IOException { return new Socket(host, port); }
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return new Socket(address, port, localAddress, localPort);
    }
    public static SSLSocketFactory getDefault() { return null; }
}
