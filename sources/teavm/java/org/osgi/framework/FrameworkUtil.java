package org.osgi.framework;

/**
 * TeaVM stub for OSGi FrameworkUtil.
 * MC/log4j reference this class for classloader-related operations
 * that don't apply in browser environment. Returns null for all
 * bundle lookups so callers fall back to non-OSGi behavior.
 */
public final class FrameworkUtil {
    private FrameworkUtil() {}

    public static Bundle getBundle(Class<?> classFromBundle) {
        return null;
    }

    public static Bundle getBundle(String className) {
        return null;
    }
}
