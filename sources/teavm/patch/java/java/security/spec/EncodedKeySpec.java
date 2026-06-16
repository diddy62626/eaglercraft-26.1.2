package java.security.spec;
public abstract class EncodedKeySpec {
    private final byte[] encoded;
    public EncodedKeySpec(byte[] encodedKey) { this.encoded = encodedKey; }
    public byte[] getEncoded() { return encoded.clone(); }
    public abstract String getFormat();
}