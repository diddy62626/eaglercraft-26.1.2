package javax.net.ssl;

import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.Provider;

/**
 * TeaVM stub for javax.net.ssl.SSLContext.
 */
public class SSLContext {
    private final String protocol;

    protected SSLContext(String protocol) {
        this.protocol = protocol;
    }

    public static SSLContext getInstance(String protocol) throws java.security.NoSuchAlgorithmException {
        return new SSLContext(protocol);
    }

    public static SSLContext getDefault() throws java.security.NoSuchAlgorithmException {
        return new SSLContext("Default");
    }

    public void init(javax.net.ssl.KeyManager[] km, javax.net.ssl.TrustManager[] tm, SecureRandom random)
            throws KeyManagementException {
        // No-op stub
    }

    public SSLSocketFactory getSocketFactory() { return null; }
    public SSLEngine createSSLEngine() { return new SSLEngine(); }
    public SSLEngine createSSLEngine(String peerHost, int peerPort) { return new SSLEngine(); }
    public String getProtocol() { return protocol; }
    public Provider getProvider() { return null; }
}
