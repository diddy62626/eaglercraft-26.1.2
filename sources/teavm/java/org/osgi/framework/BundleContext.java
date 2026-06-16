package org.osgi.framework;

import java.util.Dictionary;

/**
 * TeaVM stub for OSGi BundleContext.
 * Simplified to avoid requiring additional OSGi types.
 */
public interface BundleContext {
    String getProperty(String key);
    Bundle getBundle();
    Bundle getBundle(long id);
    Bundle[] getBundles();
    long getLastModified();
}
