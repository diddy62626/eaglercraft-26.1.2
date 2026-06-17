package java.net;
public class Socket {
    public Socket() {}
    public Socket(String host, int port) {}
    public Socket(InetAddress address, int port) {}
    public void connect(SocketAddress endpoint) throws java.io.IOException {}
    public void connect(SocketAddress endpoint, int timeout) throws java.io.IOException {}
    public void close() throws java.io.IOException {}
    public java.io.InputStream getInputStream() throws java.io.IOException { return new java.io.ByteArrayInputStream(new byte[0]); }
    public java.io.OutputStream getOutputStream() throws java.io.IOException { return new java.io.ByteArrayOutputStream(); }
    public boolean isConnected() { return false; }
    public boolean isClosed() { return true; }
    public void setSoTimeout(int timeout) {}
    public void setTcpNoDelay(boolean on) {}
}