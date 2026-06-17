package javax.net.ssl;

public class KeyManagerFactory {
    public static KeyManagerFactory getInstance(String algorithm) {
        return new KeyManagerFactory();
    }
    public static KeyManagerFactory getDefault() {
        return new KeyManagerFactory();
    }
    public static String getDefaultAlgorithm() {
        return "SunX509";
    }
    public void init(java.security.KeyStore ks, char[] password) {}
    public void init(javax.net.ssl.KeyManager[] km) {}
    public KeyManager[] getKeyManagers() { return new KeyManager[0]; }
    public java.security.Provider getProvider() { return null; }
    public String getAlgorithm() { return "SunX509"; }
}
