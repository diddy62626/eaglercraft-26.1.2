package org.slf4j;

/**
 * Browser-compatible SLF4J LoggerFactory stub.
 * Returns NOP loggers that do nothing in the browser.
 * Real logging goes through MC's own logging system.
 */
public class LoggerFactory {

    public static Logger getLogger(String name) {
        return new NOPLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        // In TeaVM, class literals like LogUtils.class can sometimes be null
        // (especially for classes that aren't directly referenced elsewhere).
        // Guard against null to prevent Class.getName() from crashing.
        if (clazz == null) {
            return getLogger("null");
        }
        try {
            return getLogger(clazz.getName());
        } catch (Throwable t) {
            // Defensive: if clazz.getName() crashes for any reason (e.g. the
            // Class object is a stub), fall back to a safe name.
            return getLogger(clazz.toString());
        }
    }

    private static class NOPLogger implements Logger {
        private final String name;

        NOPLogger(String name) {
            this.name = name;
        }

        @Override public String getName() { return name; }
        @Override public boolean isTraceEnabled() { return false; }
        @Override public void trace(String msg) {}
        @Override public void trace(String format, Object arg) {}
        @Override public void trace(String format, Object arg1, Object arg2) {}
        @Override public void trace(String format, Object... arguments) {}
        @Override public void trace(String msg, Throwable t) {}
        @Override public boolean isDebugEnabled() { return false; }
        @Override public void debug(String msg) {}
        @Override public void debug(String format, Object arg) {}
        @Override public void debug(String format, Object arg1, Object arg2) {}
        @Override public void debug(String format, Object... arguments) {}
        @Override public void debug(String msg, Throwable t) {}
        @Override public boolean isInfoEnabled() { return false; }
        @Override public void info(String msg) {}
        @Override public void info(String format, Object arg) {}
        @Override public void info(String format, Object arg1, Object arg2) {}
        @Override public void info(String format, Object... arguments) {}
        @Override public void info(String msg, Throwable t) {}
        @Override public boolean isWarnEnabled() { return false; }
        @Override public void warn(String msg) {}
        @Override public void warn(String format, Object arg) {}
        @Override public void warn(String format, Object arg1, Object arg2) {}
        @Override public void warn(String format, Object... arguments) {}
        @Override public void warn(String msg, Throwable t) {}
        @Override public boolean isErrorEnabled() { return false; }
        @Override public void error(String msg) {}
        @Override public void error(String format, Object arg) {}
        @Override public void error(String format, Object arg1, Object arg2) {}
        @Override public void error(String format, Object... arguments) {}
        @Override public void error(String msg, Throwable t) {}
        @Override public void error(Marker marker, String format, Object arg1, Object arg2) {}
    }

    public static org.slf4j.ILoggerFactory getILoggerFactory() {
        return null; // Stub
    }
}
