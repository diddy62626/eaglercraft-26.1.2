package java.security;
public abstract class Signature {
    public static Signature getInstance(String algorithm) { return null; }
    public abstract void initVerify(PublicKey publicKey);
    public abstract void initSign(PrivateKey privateKey);
    public abstract byte[] sign();
    public abstract boolean verify(byte[] signature);
    public abstract void update(byte[] data);
}