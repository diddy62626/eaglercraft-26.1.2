package java.net;
public class ServerSocket {
    public ServerSocket() {}
    public ServerSocket(int port) {}
    public Socket accept() throws java.io.IOException { return new Socket(); }
    public void close() throws java.io.IOException {}
    public boolean isClosed() { return true; }
}