package javax.net.ssl;

public class TrustManagerFactory {
    public static TrustManagerFactory getInstance(String algorithm) {
        return new TrustManagerFactory();
    }
    public static TrustManagerFactory getDefault() {
        return new TrustManagerFactory();
    }
    public static String getDefaultAlgorithm() {
        return "PKIX";
    }
    public void init(java.security.KeyStore ks) {}
    public void init(javax.net.ssl.ManagerFactoryParameters spec) {}
    public TrustManager[] getTrustManagers() { return new TrustManager[0]; }
    public java.security.Provider getProvider() { return null; }
    public String getAlgorithm() { return "PKIX"; }
}
