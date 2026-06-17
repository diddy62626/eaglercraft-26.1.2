package com.google.common.reflect;

import java.lang.reflect.Type;

/**
 * TeaVM/browser stub for Guava's TypeCapture.
 *
 * The original implementation uses Class.getGenericSuperclass() to
 * capture the generic type parameter at runtime via reflection on
 * anonymous class instances. TeaVM doesn't fully support generic
 * reflection, so we return null/Type-via-Object.class which causes
 * Guava's TypeToken to fall back to Object resolution.
 *
 * This is acceptable for MC because the datafixer system uses TypeToken
 * only for type-checking during schema construction; the actual data
 * transformations don't depend on runtime generic type info.
 */
public abstract class TypeCapture<T> {
    public Type capture() {
        // Cannot capture generic type info in TeaVM — return Object.class
        return Object.class;
    }
}
