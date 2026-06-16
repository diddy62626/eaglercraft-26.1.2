package org.apache.logging.log4j.core.jmx;

/**
 * TeaVM/browser stub for log4j2's JMX Server class.
 *
 * The real Server class creates thread pools via Executors.newFixedThreadPool
 * and tries to register MBeans with the platform MBeanServer, neither of
 * which work in the browser. This stub removes all JMX functionality,
 * which is fine because JMX is only used for runtime monitoring, not core logging.
 */
public final class Server {
    private Server() {}

    public static void reregisterMBeansAfterReconfigure() {
        // No-op
    }

    public static void unregisterMBeans() {
        // No-op
    }

    public static void unregisterLoggerContext(String contextName) {
        // No-op
    }
}
