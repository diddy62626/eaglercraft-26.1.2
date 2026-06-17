package javax.net.ssl;

/**
 * TeaVM stub for javax.net.ssl.SSLSession.
 *
 * Browser: TLS sessions are managed by the browser. Stub for compilation only.
 */
public interface SSLSession {
    byte[] getId();
    String getCipherSuite();
    String getPeerHost();
    long getCreationTime();
    long getLastAccessedTime();
    void invalidate();
    boolean isValid();
    void putValue(String name, Object value);
    Object getValue(String name);
    void removeValue(String name);
    String[] getValueNames();
    java.security.cert.Certificate[] getPeerCertificates() throws javax.net.ssl.SSLPeerUnverifiedException;
    java.security.cert.Certificate[] getLocalCertificates();
    javax.security.auth.x500.X500Principal getPeerPrincipal() throws javax.net.ssl.SSLPeerUnverifiedException;
    javax.security.auth.x500.X500Principal getLocalPrincipal();
    SSLSessionContext getSessionContext();
    int getPacketBufferSize();
    int getApplicationBufferSize();
}
