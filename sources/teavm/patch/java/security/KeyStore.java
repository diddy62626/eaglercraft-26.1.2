package java.security;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Collections;

public class KeyStore {
    private KeyStore() {}

    public static KeyStore getInstance(String type) {
        return new KeyStore();
    }

    public static KeyStore getInstance(String type, String provider) {
        return new KeyStore();
    }

    public static KeyStore getInstance(String type, Provider provider) {
        return new KeyStore();
    }

    public static String getDefaultType() {
        return "jks";
    }

    public static KeyStore getDefault() {
        return new KeyStore();
    }

    public void load(InputStream stream, char[] password) {}
    public void load(java.security.KeyStore.LoadStoreParameter param) {}
    public void store(OutputStream stream, char[] password) {}
    public void store(java.security.KeyStore.LoadStoreParameter param) {}

    public Key getSecretKey(String alias, char[] password) { return null; }
    public void setSecretKeyEntry(String alias, java.security.KeyStore.SecretKeyEntry entry, java.security.KeyStore.ProtectionParameter protParam) {}
    public void setKeyEntry(String alias, Key key, char[] password, java.security.cert.Certificate[] chain) {}
    public Key getKey(String alias, char[] password) { return null; }
    public void setKeyEntry(String alias, byte[] key, java.security.cert.Certificate[] chain) {}
    public void deleteEntry(String alias) {}
    public Enumeration<String> aliases() { return Collections.emptyEnumeration(); }
    public boolean containsAlias(String alias) { return false; }
    public int size() { return 0; }
    public boolean isKeyEntry(String alias) { return false; }
    public boolean isCertificateEntry(String alias) { return false; }
    public String getType() { return "jks"; }
    public Provider getProvider() { return null; }
    public java.security.cert.Certificate getCertificate(String alias) { return null; }
    public String getCertificateAlias(java.security.cert.Certificate cert) { return null; }
    public java.security.cert.Certificate[] getCertificateChain(String alias) { return null; }
    public void setCertificateEntry(String alias, java.security.cert.Certificate cert) {}
    public java.util.Date getCreationDate(String alias) { return new java.util.Date(); }

    public interface LoadStoreParameter {}
    public interface ProtectionParameter {}
    public static class SecretKeyEntry implements java.security.KeyStore.Entry {
        private final Key key;
        public SecretKeyEntry(Key key) { this.key = key; }
        public Key getSecretKey() { return key; }
    }
    public interface Entry {}
}
