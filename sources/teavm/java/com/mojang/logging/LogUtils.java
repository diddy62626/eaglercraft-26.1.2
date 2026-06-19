package com.mojang.logging;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Browser-compatible override of Mojang's LogUtils.
 *
 * <p>This stub replaces Mojang's real LogUtils to work around a TeaVM issue
 * where class literals like {@code LogUtils.class} are represented as null
 * for stub-only classes that aren't directly instantiated elsewhere. When
 * that null is passed to {@code LoggerFactory.getLogger(Class)}, it tries
 * to call {@code null.getName()} and crashes the MC static initializer.
 *
 * <p>This stub:
 * <ul>
 *   <li>Uses a string literal ("Minecraft") instead of {@code LogUtils.class}
 *       to sidestep the null Class issue entirely.</li>
 *   <li>Provides the {@code FATAL_MARKER} field that MC references for
 *       fatal log markers.</li>
 *   <li>Provides the {@code defer(Supplier)} method that MC uses for
 *       lazy log message evaluation.</li>
 * </ul>
 *
 * <p>This class is placed in the teavm/java source set so it's compiled
 * and put on the TeaVM classpath BEFORE the MC JAR, overriding Mojang's
 * real LogUtils via classpath ordering.
 */
public class LogUtils {

    /**
     * SLF4J Marker used by MC to tag fatal log messages.
     * Real Mojang code uses MarkerFactory.getMarker("FATAL").
     */
    public static final Marker FATAL_MARKER = MarkerFactory.getMarker("FATAL");

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

    /**
     * Defers evaluation of a log message supplier until it's actually needed.
     * Real Mojang code returns a supplier-backed lazy object; for the browser
     * stub we just call the supplier immediately and return its result.
     *
     * @param <T> The type of the supplied value
     * @param supplier The supplier to evaluate
     * @return The supplied value (evaluated immediately)
     */
    public static <T> T defer(Supplier<T> supplier) {
        if (supplier == null) {
            return null;
        }
        try {
            return supplier.get();
        } catch (Throwable t) {
            // Defensive: if the supplier throws, return null rather than
            // crashing the calling code (which usually just logs the value).
            return null;
        }
    }
}
