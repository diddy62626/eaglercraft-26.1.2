package java.security;
public class KeyFactory {
    public static KeyFactory getInstance(String algorithm) { return new KeyFactory(); }
    public PublicKey generatePublic(java.security.spec.X509EncodedKeySpec keySpec) { return null; }
    public PrivateKey generatePrivate(java.security.spec.PKCS8EncodedKeySpec keySpec) { return null; }
}