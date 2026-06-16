package org.apache.logging.log4j.core.config.plugins.validation;

/**
 * Stub ConstraintValidator interface so ConstraintValidators stub compiles.
 */
public interface ConstraintValidator<A extends java.lang.annotation.Annotation, V> {
    void initialize(A annotation);
    boolean isValid(V value);
}
