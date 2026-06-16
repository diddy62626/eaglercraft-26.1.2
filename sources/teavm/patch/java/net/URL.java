package java.net;
public final class URL {
    private final String url;
    public URL(String spec) { this.url = spec; }
    public URL(String protocol, String host, int port, String file) { this.url = protocol + "://" + host + ":" + port + file; }
    public String getProtocol() { return ""; }
    public String getHost() { return ""; }
    public int getPort() { return -1; }
    public String getPath() { return ""; }
    public String toString() { return url; }
    public java.io.InputStream openStream() throws java.io.IOException { return new java.io.ByteArrayInputStream(new byte[0]); }
}