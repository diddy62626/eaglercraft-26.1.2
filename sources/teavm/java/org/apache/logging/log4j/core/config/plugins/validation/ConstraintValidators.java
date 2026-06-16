package org.apache.logging.log4j.core.config.plugins.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * TeaVM/browser stub for log4j2's ConstraintValidators.
 *
 * The original implementation uses reflection on generic interfaces
 * (Class.getGenericInterfaces) which TeaVM doesn't support. We provide
 * a stub that returns empty validator lists so log4j plugin initialization
 * skips constraint validation entirely. This is safe because log4j
 * constraints are optional runtime checks, not core functionality.
 */
public class ConstraintValidators {
    public List<ConstraintValidator<?, ?>> findValidators(Class<?> annotatedType) {
        return new ArrayList<>();
    }

    public ConstraintValidator<?, ?> getValidator(Class<? extends Annotation> annotationType) {
        return null;
    }

    public static Class<? extends Annotation> getConstraintValidatorAnnotationType(
            Class<? extends ConstraintValidator<?, ?>> validatorType) {
        // Return null so callers fall through to "no constraint" branch.
        return null;
    }
}
