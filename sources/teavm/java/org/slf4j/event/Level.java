package org.slf4j.event;

/**
 * Browser-compatible SLF4J Level enum stub.
 */
public enum Level {
    ERROR(40),
    WARN(30),
    INFO(20),
    DEBUG(10),
    TRACE(0);

    private final int levelInt;

    Level(int levelInt) {
        this.levelInt = levelInt;
    }

    public int toInt() {
        return levelInt;
    }
}
