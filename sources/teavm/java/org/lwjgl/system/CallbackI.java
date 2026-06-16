package org.lwjgl.system;

public interface CallbackI {
    default long address() { return 0L; }
    default void free() {}
}
