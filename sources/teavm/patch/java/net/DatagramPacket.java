package java.net;
public class DatagramPacket {
    public DatagramPacket(byte[] buf, int length) {}
    public DatagramPacket(byte[] buf, int offset, int length) {}
    public DatagramPacket(byte[] buf, int offset, int length, InetAddress address, int port) {}
    public byte[] getData() { return new byte[0]; }
    public int getLength() { return 0; }
    public InetAddress getAddress() { return new InetAddress(); }
    public int getPort() { return 0; }
}