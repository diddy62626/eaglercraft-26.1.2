package com.mojang.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Browser-compatible override of Mojang's LogUtils.
 *
 * <p>The real Mojang LogUtils does:
 * <pre>
 * public static Logger getLogger() {
 *     return LoggerFactory.getLogger(LogUtils.class);
 * }
 * </pre>
 *
 * <p>In TeaVM, class literals like {@code LogUtils.class} can be null for classes
 * that aren't directly instantiated elsewhere. This causes
 * {@code LoggerFactory.getLogger(null)} to call {@code null.getName()} which
 * crashes the MC static initializer.
 *
 * <p>This stub uses a string literal instead of a class literal, sidestepping
 * the null Class issue entirely. The MC code that calls {@code LogUtils.getLogger()}
 * will get a working NOP logger instead of crashing.
 *
 * <p>This class is placed in the patch module so it OVERRIDES the real
 * LogUtils from the MC JAR via --patch-module java.base ordering.
 */
public class LogUtils {

    /** Lazy holder for the standard "Minecraft" logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger("Minecraft");

    /**
     * Returns the standard Minecraft logger. Uses a string literal
     * ("Minecraft") instead of {@code LogUtils.class} to avoid the
     * null Class literal issue in TeaVM.
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Returns a named logger. Uses the string overload of LoggerFactory
     * directly to avoid passing Class objects through.
     */
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }

    /**
     * Returns a logger for a class. Defensively handles null Class
     * (which can occur in TeaVM for class literals of stub-only classes).
     */
    public static Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            return LoggerFactory.getLogger("null");
        }
        try {
            return LoggerFactory.getLogger(clazz.getName());
        } catch (Throwable t) {
            return LoggerFactory.getLogger("Minecraft");
        }
    }
}
