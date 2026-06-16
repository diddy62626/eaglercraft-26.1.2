package org.apache.logging.log4j.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * TeaVM/browser stub for log4j2's Log4jThreadFactory.
 *
 * The real implementation calls Thread.getThreadGroup() and SecurityManager
 * methods which TeaVM doesn't fully support. This stub provides a simple
 * ThreadFactory that creates daemon threads without invoking security APIs.
 */
public class Log4jThreadFactory implements ThreadFactory {
    private final String threadNamePrefix;
    private final boolean daemon;

    public static Log4jThreadFactory createDaemonThreadFactory(String factoryName) {
        return new Log4jThreadFactory("Log4j2-" + factoryName, true);
    }

    public static Log4jThreadFactory createThreadFactory(String factoryName) {
        return new Log4jThreadFactory("Log4j2-" + factoryName, false);
    }

    public Log4jThreadFactory(String threadNamePrefix, boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, threadNamePrefix + "-" + Thread.currentThread().getName());
        t.setDaemon(daemon);
        return t;
    }
}
