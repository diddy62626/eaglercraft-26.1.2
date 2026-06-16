package java.net;
public final class URI {
    private final String uri;
    public URI(String str) { this.uri = str; }
    public static URI create(String str) { return new URI(str); }
    public String getScheme() { return ""; }
    public String getHost() { return ""; }
    public int getPort() { return -1; }
    public String getPath() { return ""; }
    public String getQuery() { return ""; }
    public String getFragment() { return ""; }
    public String toString() { return uri; }
}