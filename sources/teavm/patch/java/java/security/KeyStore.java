package java.security;
public class KeyStore {
    public static KeyStore getInstance(String type) { return new KeyStore(); }
    public void load(java.io.InputStream stream, char[] password) {}
    public void store(java.io.OutputStream stream, char[] password) {}
}