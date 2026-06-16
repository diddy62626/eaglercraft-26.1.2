package javax.net.ssl;

import java.io.IOException;
import java.net.Socket;

/**
 * TeaVM stub for javax.net.ssl.SSLSocketFactory.
 */
public abstract class SSLSocketFactory {
    public abstract Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException;
    public abstract String[] getDefaultCipherSuites();
    public abstract String[] getSupportedCipherSuites();

    public Socket createSocket() throws IOException { return new Socket(); }
    public Socket createSocket(String host, int port) throws IOException { return new Socket(); }
    public Socket createSocket(String host, int port, java.net.InetAddress localHost, int localPort) throws IOException {
        return new Socket();
    }
    public Socket createSocket(java.net.InetAddress host, int port) throws IOException { return new Socket(); }
    public Socket createSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddress, int localPort) throws IOException {
        return new Socket();
    }
    public static SSLSocketFactory getDefault() { return null; }
}
