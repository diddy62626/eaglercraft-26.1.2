package java.security;
public class KeyPair {
    private final PublicKey pub; private final PrivateKey priv;
    public KeyPair(PublicKey publicKey, PrivateKey privateKey) { this.pub = publicKey; this.priv = privateKey; }
    public PublicKey getPublic() { return pub; }
    public PrivateKey getPrivate() { return priv; }
}