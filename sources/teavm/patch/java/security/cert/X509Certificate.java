package java.security.cert;

import java.math.BigInteger;
import java.security.Principal;
import java.util.Date;
import java.util.Set;

/**
 * TeaVM stub for X509Certificate.
 * Browser crypto APIs use SubtleCrypto instead of X.509 parsing.
 * This stub provides no-op implementations so authlib's certificate
 * handling code compiles and runs without errors.
 */
public abstract class X509Certificate extends Certificate implements X509Extension {
    protected X509Certificate() {
        super("X.509");
    }

    public abstract byte[] getEncoded();
    public abstract void verify(java.security.PublicKey key);
    public abstract void verify(java.security.PublicKey key, String sigProvider);
    public abstract String toString();
    public abstract java.security.PublicKey getPublicKey();

    public byte[] getSignature() { return new byte[0]; }
    public String getSigAlgName() { return "SHA256withRSA"; }
    public String getSigAlgOID() { return "1.2.840.113549.1.1.11"; }
    public byte[] getSigAlgParams() { return null; }

    public int getVersion() { return 3; }
    public BigInteger getSerialNumber() { return BigInteger.ZERO; }
    public Principal getIssuerDN() { return null; }
    public Principal getSubjectDN() { return null; }
    public Date getNotBefore() { return new Date(0); }
    public Date getNotAfter() { return new Date(Long.MAX_VALUE); }
    public byte[] getTBSCertificate() { return new byte[0]; }
    public byte[] getIssuerUniqueID() { return null; }
    public byte[] getSubjectUniqueID() { return null; }
    public boolean[] getKeyUsage() { return new boolean[0]; }
    public java.util.List<String> getExtendedKeyUsage() { return new java.util.ArrayList<>(); }
    public Set<String> getCriticalExtensionOIDs() { return new java.util.HashSet<>(); }
    public Set<String> getNonCriticalExtensionOIDs() { return new java.util.HashSet<>(); }
    public byte[] getExtensionValue(String oid) { return null; }
    public boolean hasUnsupportedCriticalExtension() { return false; }

    @Override
    public boolean equals(Object other) { return this == other; }

    @Override
    public int hashCode() { return System.identityHashCode(this); }
}
