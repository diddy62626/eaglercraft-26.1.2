package java.net;

/**
 * TeaVM stub for java.net.URLConnection.
 */
public class URLConnection {
    protected URL url;

    public URLConnection(URL url) {
        this.url = url;
    }

    public URL getURL() {
        return url;
    }

    public void setConnectTimeout(int timeout) {}
    public int getConnectTimeout() { return 0; }
    public void setReadTimeout(int timeout) {}
    public int getReadTimeout() { return 0; }
    public void setRequestProperty(String key, String value) {}
    public String getRequestProperty(String key) { return null; }
    public void connect() throws java.io.IOException {}
    public java.io.InputStream getInputStream() throws java.io.IOException { return new java.io.ByteArrayInputStream(new byte[0]); }
    public java.io.OutputStream getOutputStream() throws java.io.IOException { return new java.io.ByteArrayOutputStream(); }
    public String getContentType() { return null; }
    public int getContentLength() { return -1; }
    public long getContentLengthLong() { return -1L; }
    public String getContentEncoding() { return null; }
    public long getExpiration() { return 0L; }
    public long getDate() { return 0L; }
    public long getLastModified() { return 0L; }
    public void setDoInput(boolean doinput) {}
    public boolean getDoInput() { return true; }
    public void setDoOutput(boolean dooutput) {}
    public boolean getDoOutput() { return false; }
    public void setAllowUserInteraction(boolean allowuserinteraction) {}
    public boolean getAllowUserInteraction() { return false; }
    public void setUseCaches(boolean usecaches) {}
    public boolean getUseCaches() { return true; }
    public void setIfModifiedSince(long ifmodifiedsince) {}
    public long getIfModifiedSince() { return 0L; }
    public java.util.Map<String, java.util.List<String>> getHeaderFields() { return new java.util.HashMap<>(); }
    public String getHeaderField(String name) { return null; }
    public int getHeaderFieldInt(String name, int Default) { return Default; }
    public long getHeaderFieldLong(String name, long Default) { return Default; }
    public java.util.Map<String, java.util.List<String>> getRequestProperties() { return new java.util.HashMap<>(); }
}
