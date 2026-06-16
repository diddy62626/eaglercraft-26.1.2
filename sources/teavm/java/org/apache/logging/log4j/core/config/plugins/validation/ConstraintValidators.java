package org.apache.logging.log4j.core.config.plugins.validation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TeaVM/browser stub for log4j2's ConstraintValidators.
 *
 * Real log4j2 method signature is:
 *   Collection<ConstraintValidator<?, ?>> findValidators(Annotation[] annotations)
 *
 * We return empty lists so log4j plugin initialization skips constraint validation.
 */
public class ConstraintValidators {
    public Collection<ConstraintValidator<?, ?>> findValidators(Annotation[] annotations) {
        return new ArrayList<>();
    }

    public Collection<ConstraintValidator<?, ?>> findValidators(Class<?> annotatedType) {
        return new ArrayList<>();
    }

    public ConstraintValidator<?, ?> getValidator(Class<? extends Annotation> annotationType) {
        return null;
    }

    public static Class<? extends Annotation> getConstraintValidatorAnnotationType(
            Class<? extends ConstraintValidator<?, ?>> validatorType) {
        return null;
    }
}
