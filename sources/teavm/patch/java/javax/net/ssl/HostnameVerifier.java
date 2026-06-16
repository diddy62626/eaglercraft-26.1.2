package javax.net.ssl;

/**
 * TeaVM stub for javax.net.ssl.HostnameVerifier.
 *
 * Browser: TLS hostname verification is handled by the browser's
 * fetch/WebSocket APIs. This interface exists for TeaVM compilation only.
 */
@FunctionalInterface
public interface HostnameVerifier {
    boolean verify(String hostname, javax.net.ssl.SSLSession session);
}
