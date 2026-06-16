package java.net;
public class DatagramSocket {
    public DatagramSocket() {}
    public DatagramSocket(int port) {}
    public void send(DatagramPacket p) throws java.io.IOException {}
    public void receive(DatagramPacket p) throws java.io.IOException {}
    public void close() {}
    public boolean isClosed() { return true; }
}