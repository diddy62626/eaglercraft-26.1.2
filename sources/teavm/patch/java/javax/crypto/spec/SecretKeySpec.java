package javax.crypto.spec;
public class SecretKeySpec implements javax.crypto.SecretKey {
    private final byte[] key;
    public SecretKeySpec(byte[] key, String algorithm) { this.key = key; }
    public String getAlgorithm() { return "AES"; }
    public byte[] getEncoded() { return key.clone(); }
    public String getFormat() { return "RAW"; }
}