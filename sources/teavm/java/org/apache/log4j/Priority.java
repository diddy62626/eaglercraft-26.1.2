package org.apache.log4j;

/**
 * EaglerCraft stub for log4j v1 Priority.
 */
public class Priority {
    public static final Priority DEBUG = new Priority(5, "DEBUG");
    public static final Priority INFO = new Priority(4, "INFO");
    public static final Priority WARN = new Priority(3, "WARN");
    public static final Priority ERROR = new Priority(2, "ERROR");
    public static final Priority FATAL = new Priority(1, "FATAL");

    final int level;
    final String levelStr;

    protected Priority(int level, String levelStr) {
        this.level = level;
        this.levelStr = levelStr;
    }

    public int toInt() { return level; }
    public String toString() { return levelStr; }
    public boolean isGreaterOrEqual(Priority r) { return level <= r.level; }
}
