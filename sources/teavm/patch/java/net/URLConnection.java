package java.net;
public class URLConnection {
    public void setConnectTimeout(int timeout) {}
    public void setReadTimeout(int timeout) {}
    public void setRequestProperty(String key, String value) {}
    public void connect() throws java.io.IOException {}
    public java.io.InputStream getInputStream() throws java.io.IOException { return new java.io.ByteArrayInputStream(new byte[0]); }
}