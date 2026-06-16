package java.net;
public class HttpURLConnection extends URLConnection {
    public static final int HTTP_OK = 200;
    public static final int HTTP_NOT_FOUND = 404;
    public int getResponseCode() throws java.io.IOException { return HTTP_OK; }
    public String getResponseMessage() throws java.io.IOException { return "OK"; }
    public void setRequestMethod(String method) {}
    public void disconnect() {}
}