package org.apache.logging.log4j.core.config.plugins.validation;

/**
 * Stub ConstraintValidator interface matching log4j2's real signature.
 *
 * Real log4j2 ConstraintValidator<A extends Annotation, V> has:
 *   void initialize(A annotation);
 *   boolean isValid(String name, V value);
 *
 * We use a flexible signature that accepts both V alone and (String, V).
 */
public interface ConstraintValidator<A extends java.lang.annotation.Annotation, V> {
    void initialize(A annotation);

    /** Real log4j2 method: isValid(String name, V value). */
    boolean isValid(String name, V value);

    /** Backwards-compat: isValid(V value) calls isValid(null, value). */
    default boolean isValid(V value) {
        return isValid(null, value);
    }
}
