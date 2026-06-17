package javax.net.ssl;

import java.io.IOException;
import java.net.URL;

/**
 * TeaVM stub for javax.net.ssl.HttpsURLConnection.
 *
 * Browser: TLS is handled by the browser/fetch API, so all SSL
 * configuration is no-op. We still expose the full Java API so
 * MC and its libraries compile against TeaVM.
 */
public abstract class HttpsURLConnection extends java.net.HttpURLConnection {
    protected HttpsURLConnection(URL url) {
        super(url);
    }

    public abstract String getCipherSuite();
    public abstract java.security.cert.Certificate[] getLocalCertificates();
    public abstract java.security.cert.Certificate[] getServerCertificates();

    // ===== Instance configuration (per-connection) =====

    /**
     * Sets the hostname verifier for this connection. Browser: no-op.
     */
    public void setHostnameVerifier(HostnameVerifier verifier) {
        // no-op in browser
    }

    /**
     * Returns the hostname verifier (always null in browser).
     */
    public HostnameVerifier getHostnameVerifier() {
        return null;
    }

    /**
     * Sets the SSL socket factory for this connection. Browser: no-op.
     */
    public void setSSLSocketFactory(SSLSocketFactory factory) {
        // no-op in browser
    }

    /**
     * Returns the SSL socket factory (always null in browser).
     */
    public SSLSocketFactory getSSLSocketFactory() {
        return null;
    }

    // ===== Static defaults =====

    public static HostnameVerifier getDefaultHostnameVerifier() { return null; }
    public static void setDefaultHostnameVerifier(HostnameVerifier verifier) {}
    public static SSLSocketFactory getDefaultSSLSocketFactory() { return null; }
    public static void setDefaultSSLSocketFactory(SSLSocketFactory factory) {}
}
