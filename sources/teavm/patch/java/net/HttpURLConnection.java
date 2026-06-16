package java.net;

public class HttpURLConnection extends URLConnection {
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_ERROR = 500;
    public static final int HTTP_UNAVAILABLE = 503;

    protected String method = "GET";
    protected int responseCode = -1;
    protected String responseMessage = null;
    protected boolean instanceFollowRedirects = true;
    protected boolean chunkedStreaming = -1 > 0;

    protected HttpURLConnection(URL url) {
        super(url);
    }

    public int getResponseCode() throws java.io.IOException { return HTTP_OK; }
    public String getResponseMessage() throws java.io.IOException { return "OK"; }
    public void setRequestMethod(String method) { this.method = method; }
    public String getRequestMethod() { return method; }
    public void disconnect() {}
    public boolean usingProxy() { return false; }
    public java.io.InputStream getErrorStream() { return new java.io.ByteArrayInputStream(new byte[0]); }
    public void setInstanceFollowRedirects(boolean follow) { this.instanceFollowRedirects = follow; }
    public boolean getInstanceFollowRedirects() { return instanceFollowRedirects; }
    public void setChunkedStreamingMode(int chunklen) {}
    public void setFixedLengthStreamingMode(int contentLength) {}
    public void setFixedLengthStreamingMode(long contentLength) {}
    public String getHeaderFieldKey(int n) { return null; }
    public String getHeaderField(int n) { return null; }
    public long getHeaderFieldDate(String name, long Default) { return Default; }
}
