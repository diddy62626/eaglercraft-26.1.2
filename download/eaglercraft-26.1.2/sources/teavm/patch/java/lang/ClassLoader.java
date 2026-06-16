package java.lang;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Collections;

/**
 * TeaVM stub for java.lang.ClassLoader.
 * Browser has no dynamic class loading. All classes are available
 * to TeaVM at compile time.
 */
public abstract class ClassLoader {
    private static ClassLoader systemClassLoader;

    protected ClassLoader() {}
    protected ClassLoader(ClassLoader parent) {}

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException("Dynamic class loading not supported in browser: " + name);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        throw new ClassNotFoundException("Dynamic class loading not supported in browser: " + name);
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }

    protected Class<?> defineClass(String name, byte[] b, int off, int len) throws ClassFormatError {
        throw new ClassFormatError("Cannot define classes in browser");
    }

    protected Class<?> defineClass(String name, java.nio.ByteBuffer b, int off, int len) throws ClassFormatError {
        throw new ClassFormatError("Cannot define classes in browser");
    }

    protected final Class<?> defineClass(String name, byte[] b, int off, int len, java.security.ProtectionDomain pd) throws ClassFormatError {
        throw new ClassFormatError("Cannot define classes in browser");
    }

    protected void resolveClass(Class<?> c) {}

    protected Class<?> findLoadedClass(String name) { return null; }

    protected final Class<?> findSystemClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }

    public InputStream getResourceAsStream(String name) { return null; }

    public URL getResource(String name) { return null; }

    public Enumeration<URL> getResources(String name) throws IOException {
        return Collections.enumeration(Collections.emptyList());
    }

    public static Enumeration<URL> getSystemResources(String name) throws IOException {
        return Collections.enumeration(Collections.emptyList());
    }

    public static InputStream getSystemResourceAsStream(String name) { return null; }

    public static URL getSystemResource(String name) { return null; }

    public ClassLoader getParent() { return null; }

    public static ClassLoader getSystemClassLoader() {
        if (systemClassLoader == null) {
            systemClassLoader = new ClassLoader() {};
        }
        return systemClassLoader;
    }

    public void setDefaultAssertionStatus(boolean enabled) {}
    public void setPackageAssertionStatus(String packageName, boolean enabled) {}
    public void setClassAssertionStatus(String className, boolean enabled) {}
    public void clearAssertionStatus() {}
}
