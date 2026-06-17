package org.osgi.framework.wiring;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * TeaVM stub for OSGi BundleRevision.
 */
public interface BundleRevision {
    String BUNDLE_NAMESPACE = "osgi.wiring.bundle";
    String PACKAGE_NAMESPACE = "osgi.wiring.package";
    String HOST_NAMESPACE = "osgi.wiring.host";
    String SERVICE_NAMESPACE = "osgi.wiring.service";

    Bundle getBundle();
    BundleWiring getWiring();
    String getSymbolicName();
    Version getVersion();
    java.util.List<BundleCapability> getDeclaredCapabilities(String namespace);
    java.util.List<BundleRequirement> getDeclaredRequirements(String namespace);
    java.util.List<BundleCapability> getCapabilities(String namespace);
    java.util.List<BundleRequirement> getRequirements(String namespace);
    int getTypes();
    BundleRevision getRevisions();
}
