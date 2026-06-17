package org.osgi.framework.wiring;

/**
 * TeaVM stub for OSGi BundleWire.
 */
public interface BundleWire {
    BundleCapability getCapability();
    BundleRequirement getRequirement();
    BundleWiring getProviderWiring();
    BundleWiring getRequirerWiring();
    BundleRevision getProviderRevision();
    BundleRevision getRequirerRevision();
}
