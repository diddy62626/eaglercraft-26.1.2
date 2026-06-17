package javax.security.cert;

/**
 * TeaVM stub for javax.security.cert.X509Certificate.
 * Note: this is the deprecated javax.security.cert package, not java.security.cert.
 */
public abstract class X509Certificate {
    public abstract byte[] getEncoded() throws java.security.cert.CertificateEncodingException;
    public abstract void checkValidity() throws java.security.cert.CertificateExpiredException, java.security.cert.CertificateNotYetValidException;
    public abstract void checkValidity(java.util.Date date) throws java.security.cert.CertificateExpiredException, java.security.cert.CertificateNotYetValidException;
    public abstract java.security.PublicKey getPublicKey();

    public static X509Certificate getInstance(byte[] certData) throws java.security.cert.CertificateException {
        throw new java.security.cert.CertificateException("X.509 certs not supported in browser");
    }
}
