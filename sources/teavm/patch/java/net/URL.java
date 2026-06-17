package java.net;

public final class URL {
    private final String url;
    public URL(String spec) { this.url = spec; }
    public URL(String protocol, String host, int port, String file) { this.url = protocol + "://" + host + ":" + port + file; }
    public URL(String protocol, String host, String file) { this.url = protocol + "://" + host + file; }
    public String getProtocol() { return ""; }
    public String getHost() { return ""; }
    public int getPort() { return -1; }
    public int getDefaultPort() { return -1; }
    public String getPath() { return ""; }
    public String getFile() { return ""; }
    public String getRef() { return null; }
    public String getQuery() { return null; }
    public String getAuthority() { return null; }
    public String getUserInfo() { return null; }
    public String toString() { return url; }
    public String toExternalForm() { return url; }
    public java.io.InputStream openStream() throws java.io.IOException { return new java.io.ByteArrayInputStream(new byte[0]); }

    public java.net.URLConnection openConnection() throws java.io.IOException {
        return new java.net.URLConnection(this) {};
    }

    public java.net.URLConnection openConnection(java.net.Proxy proxy) throws java.io.IOException {
        return openConnection();
    }

    public boolean sameFile(java.net.URL other) { return false; }
    public java.net.URI toURI() throws java.net.URISyntaxException { return new java.net.URI(url); }
    public boolean equals(Object obj) {
        if (!(obj instanceof URL)) return false;
        return url.equals(((URL) obj).url);
    }
    public int hashCode() { return url == null ? 0 : url.hashCode(); }
}
