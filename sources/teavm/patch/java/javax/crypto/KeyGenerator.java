package javax.crypto;
public class KeyGenerator {
    public static KeyGenerator getInstance(String algorithm) { return new KeyGenerator(); }
    public void init(int keysize) {}
    public SecretKey generateKey() { return null; }
}