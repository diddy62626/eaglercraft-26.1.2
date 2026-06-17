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

    default <S> java.util.Collection<org.osgi.framework.ServiceReference<S>> getServiceReferences(Class<S> clazz, String filter) {
        return new java.util.ArrayList<>();
    }
}
