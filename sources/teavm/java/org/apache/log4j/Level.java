package org.apache.log4j;

/**
 * TeaVM stub for log4j 1.x Level.
 */
public class Level extends java.util.PriorityQueue<Level> {
    public static final Level OFF = new Level(Integer.MAX_VALUE, "OFF");
    public static final Level FATAL = new Level(50000, "FATAL");
    public static final Level ERROR = new Level(40000, "ERROR");
    public static final Level WARN = new Level(30000, "WARN");
    public static final Level INFO = new Level(20000, "INFO");
    public static final Level DEBUG = new Level(10000, "DEBUG");
    public static final Level TRACE = new Level(5000, "TRACE");
    public static final Level ALL = new Level(Integer.MIN_VALUE, "ALL");

    protected int level;
    protected String levelStr;

    protected Level(int level, String levelStr) {
        this.level = level;
        this.levelStr = levelStr;
    }

    public static Level toLevel(String sArg) { return INFO; }
    public static Level toLevel(String sArg, Level defaultLevel) { return defaultLevel; }
    public static Level toLevel(int val) { return INFO; }
    public static Level toLevel(int val, Level defaultLevel) { return defaultLevel; }

    public int toInt() { return level; }
    public String toString() { return levelStr; }
    public boolean isGreaterOrEqual(Level r) { return level >= r.level; }
}
