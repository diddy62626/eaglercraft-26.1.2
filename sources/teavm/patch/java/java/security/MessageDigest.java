package java.security;
public abstract class MessageDigest {
    public static MessageDigest getInstance(String algorithm) { return new MessageDigest() { public byte[] digest() { return new byte[0]; } public void update(byte[] input) {} }; }
    public abstract void update(byte[] input);
    public abstract byte[] digest();
    public byte[] digest(byte[] input) { update(input); return digest(); }
}