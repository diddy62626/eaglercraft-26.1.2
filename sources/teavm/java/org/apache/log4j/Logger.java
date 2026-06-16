package org.apache.log4j;

/**
 * TeaVM/browser stub for log4j 1.x Logger.
 *
 * The log4j2 -> log4j-1.2 API compatibility shim (org.apache.log4j.Category)
 * references this. We provide a stub that silently swallows all log calls.
 * This lets the SLF4J -> log4j2 -> log4j-1.x adapter chain initialize
 * without errors when log4j2's Log4jLoggerContext tries to register
 * legacy log4j 1.x loggers.
 */
public class Logger {
    public static Logger getLogger(String name) { return new Logger(); }
    public static Logger getLogger(Class<?> clazz) { return new Logger(); }

    public void debug(Object message) {}
    public void debug(Object message, Throwable t) {}
    public void info(Object message) {}
    public void info(Object message, Throwable t) {}
    public void warn(Object message) {}
    public void warn(Object message, Throwable t) {}
    public void error(Object message) {}
    public void error(Object message, Throwable t) {}
    public void fatal(Object message) {}
    public void fatal(Object message, Throwable t) {}
    public void trace(Object message) {}
    public void trace(Object message, Throwable t) {}

    public boolean isDebugEnabled() { return false; }
    public boolean isInfoEnabled() { return false; }
    public boolean isWarnEnabled() { return false; }
    public boolean isErrorEnabled() { return false; }
    public boolean isFatalEnabled() { return false; }
    public boolean isTraceEnabled() { return false; }

    public String getName() { return ""; }
    public Level getLevel() { return Level.INFO; }
    public void setLevel(Level level) {}

    public boolean isEnabledFor(Priority priority) { return false; }

    public void log(String fqcn, Priority priority, Object message, Throwable t) {
        // No-op stub
    }
}
