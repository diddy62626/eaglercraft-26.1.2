package java.security.cert;

/**
 * TeaVM-compatible stub for Certificate.
 * Browser doesn't deal with X.509 certificates directly.
 */
public abstract class Certificate implements java.io.Serializable {
    private final String type;

    protected Certificate(String type) { this.type = type; }

    public String getType() { return type; }
    public abstract byte[] getEncoded();
    public abstract String toString();
    public abstract boolean equals(Object other);
    public abstract int hashCode();
}
