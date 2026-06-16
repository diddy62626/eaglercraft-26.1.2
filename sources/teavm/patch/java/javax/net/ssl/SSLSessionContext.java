package javax.net.ssl;

/**
 * TeaVM stub for javax.net.ssl.SSLSessionContext.
 */
public interface SSLSessionContext {
    java.util.Enumeration<byte[]> getIds();
    SSLSession getSession(byte[] sessionId);
    int getSessionCacheSize();
    void setSessionCacheSize(int size);
    int getSessionTimeout();
    void setSessionTimeout(int seconds);
}
