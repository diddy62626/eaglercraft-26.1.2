package javax.net.ssl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * TeaVM stub for javax.net.ssl.HttpsURLConnection.
 */
public abstract class HttpsURLConnection extends HttpURLConnection {
    protected HttpsURLConnection(URL url) {
        super(url);
    }

    public abstract String getCipherSuite();
    public abstract java.security.cert.Certificate[] getLocalCertificates();
    public abstract java.security.cert.Certificate[] getServerCertificates();

    public static String getDefaultHostnameVerifier() { return "default"; }
    public static void setDefaultHostnameVerifier(Object verifier) {}
    public static javax.net.ssl.SSLSocketFactory getDefaultSSLSocketFactory() { return null; }
    public static void setDefaultSSLSocketFactory(javax.net.ssl.SSLSocketFactory factory) {}
}
