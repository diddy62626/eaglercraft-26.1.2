package org.osgi.framework;

/**
 * EaglerCraft stub for org.osgi.framework.ServiceReference.
 */
public interface ServiceReference<S> extends Comparable<ServiceReference<S>> {
    Object getProperty(String key);
    String[] getPropertyKeys();
    Bundle getBundle();
    Bundle[] getUsingBundles();
    boolean isAssignableTo(Bundle bundle, String className);
    @Override
    default int compareTo(ServiceReference<S> o) { return 0; }
}
