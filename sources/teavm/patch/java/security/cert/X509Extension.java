package java.security.cert;

import java.util.Set;

/**
 * TeaVM stub for X509Extension interface.
 */
public interface X509Extension {
    Set<String> getCriticalExtensionOIDs();
    Set<String> getNonCriticalExtensionOIDs();
    byte[] getExtensionValue(String oid);
    boolean hasUnsupportedCriticalExtension();
}
