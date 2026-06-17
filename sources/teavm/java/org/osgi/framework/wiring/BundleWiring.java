package org.osgi.framework.wiring;

import java.util.Collection;
import java.util.List;

/**
 * TeaVM stub for OSGi BundleWiring.
 * Browser: no OSGi runtime — all methods return empty defaults.
 */
public interface BundleWiring {
    boolean isInUse();
    BundleRevision getRevision();
    ClassLoader getClassLoader();
    List<BundleCapability> getCapabilities(String namespace);
    List<BundleRequirement> getRequirements(String namespace);
    Collection<BundleWire> getProvidedWires(String namespace);
    Collection<BundleWire> getRequiredWires(String namespace);
    Collection<BundleCapability> getResourceCapabilities(String namespace);
    Collection<BundleRequirement> getResourceRequirements(String namespace);
    Collection<BundleWire> getProvidedResourceWires(String namespace);
    Collection<BundleWire> getRequiredResourceWires(String namespace);

    /**
     * List resources visible to this bundle wiring.
     * Browser: returns empty collection (no real OSGi bundle content).
     */
    Collection<String> listResources(String path, String filePattern, int options);
}
