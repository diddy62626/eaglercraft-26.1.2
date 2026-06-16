package javax.crypto.spec;
public class IvParameterSpec implements java.security.spec.AlgorithmParameterSpec {
    private final byte[] iv;
    public IvParameterSpec(byte[] iv) { this.iv = iv; }
    public byte[] getIV() { return iv.clone(); }
}