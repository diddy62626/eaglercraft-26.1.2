package java.security;

public abstract class MessageDigest {
    private final String algorithm;

    protected MessageDigest() {
        this.algorithm = "unknown";
    }

    protected MessageDigest(String algorithm) {
        this.algorithm = algorithm;
    }

    public static MessageDigest getInstance(String algorithm) {
        return new MessageDigest(algorithm) {
            public void update(byte[] input) {}
            public byte[] digest() { return new byte[0]; }
        };
    }

    public static MessageDigest getInstance(String algorithm, String provider) {
        return getInstance(algorithm);
    }

    public static MessageDigest getInstance(String algorithm, Provider provider) {
        return getInstance(algorithm);
    }

    public abstract void update(byte[] input);
    public abstract byte[] digest();

    public void update(byte input) {}
    public void update(byte[] input, int offset, int len) {}
    public byte[] digest(byte[] input) {
        update(input);
        return digest();
    }
    public void reset() {}
    public int digest(byte[] buf, int offset, int len) { return 0; }

    public String getAlgorithm() { return algorithm; }
    public int getDigestLength() { return 0; }
    public String toString() { return algorithm + " (stub)"; }
    public Object clone() throws CloneNotSupportedException { return super.clone(); }

    public static boolean isEqual(byte[] digesta, byte[] digestb) {
        if (digesta == digestb) return true;
        if (digesta == null || digestb == null) return false;
        if (digesta.length != digestb.length) return false;
        for (int i = 0; i < digesta.length; i++) {
            if (digesta[i] != digestb[i]) return false;
        }
        return true;
    }
}
