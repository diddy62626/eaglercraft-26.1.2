package java.security;
public abstract class KeyPairGenerator {
    public static KeyPairGenerator getInstance(String algorithm) { return new KeyPairGenerator() { public KeyPair generateKeyPair() { return new KeyPair(null, null); } }; }
    public abstract KeyPair generateKeyPair();
}