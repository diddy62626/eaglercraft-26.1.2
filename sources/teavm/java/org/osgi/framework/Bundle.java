package org.osgi.framework;

import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;

/**
 * TeaVM stub for OSGi Bundle.
 */
public interface Bundle {
    int UNINSTALLED = 0x00000001;
    int INSTALLED = 0x00000002;
    int RESOLVED = 0x00000004;
    int STARTING = 0x00000008;
    int STOPPING = 0x00000010;
    int ACTIVE = 0x00000020;

    int getState();
    Dictionary<String, String> getHeaders();
    long getBundleId();
    String getLocation();
    String getSymbolicName();
    Class<?> loadClass(String name) throws ClassNotFoundException;
    URL getResource(String name);
    Enumeration<URL> getResources(String name);
    Enumeration<String> getEntryPaths(String path);
    URL getEntry(String path);
    long getLastModified();
    BundleContext getBundleContext();
    void start(int options);
    void start();
    void stop(int options);
    void stop();
    void update();
    void update(java.io.InputStream in);
    void uninstall();

    default <A> A adapt(Class<A> adapterType) { return null; }
}
