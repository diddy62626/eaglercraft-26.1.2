package java.security;
public class SecureRandom extends java.util.Random {
    public SecureRandom() { super(); }
    public SecureRandom(byte[] seed) { super(); }
    public static SecureRandom getInstanceStrong() { return new SecureRandom(); }
}