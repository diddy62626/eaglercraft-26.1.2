package java.lang.invoke;

import java.lang.invoke.VarHandle;

public abstract class VarHandle {
    protected VarHandle() {}

    public final native Object get(Object... args);
    public final native void set(Object... args);
    public final native Object getVolatile(Object... args);
    public final native void setVolatile(Object... args);
    public final native Object getOpaque(Object... args);
    public final native void setOpaque(Object... args);
    public final native boolean compareAndSet(Object... args);
    public final native Object compareAndExchange(Object... args);
    public final native Object getAndAdd(Object... args);
    public final native Object getAndBitwiseOr(Object... args);

    public boolean weakCompareAndSet(Object... args) { return false; }
    public boolean weakCompareAndSetPlain(Object... args) { return false; }
    public boolean weakCompareAndSetVolatile(Object... args) { return false; }
    public boolean weakCompareAndSetAcquire(Object... args) { return false; }
    public boolean weakCompareAndSetRelease(Object... args) { return false; }

    public final Object getAndSet(Object... args) { return null; }
    public final Object getAndBitwiseAnd(Object... args) { return null; }
    public final Object getAndBitwiseXor(Object... args) { return null; }

    public static long byteOffset(Object[] array, int index) { return 0L; }

    public final java.lang.invoke.MethodHandle toMethodHandle(java.lang.invoke.VarHandle.AccessMode accessMode) {
        return null;
    }

    public enum AccessMode {
        GET, SET, GET_VOLATILE, SET_VOLATILE, GET_OPAQUE, SET_OPAQUE,
        COMPARE_AND_SET, COMPARE_AND_EXCHANGE, COMPARE_AND_EXCHANGE_ACQUIRE,
        COMPARE_AND_EXCHANGE_RELEASE, WEAK_COMPARE_AND_SET_PLAIN,
        WEAK_COMPARE_AND_SET, WEAK_COMPARE_AND_SET_ACQUIRE,
        WEAK_COMPARE_AND_SET_RELEASE, GET_AND_SET, GET_AND_SET_ACQUIRE,
        GET_AND_SET_RELEASE, GET_AND_ADD, GET_AND_ADD_ACQUIRE,
        GET_AND_ADD_RELEASE, GET_AND_BITWISE_OR, GET_AND_BITWISE_OR_ACQUIRE,
        GET_AND_BITWISE_OR_RELEASE, GET_AND_BITWISE_AND, GET_AND_BITWISE_AND_ACQUIRE,
        GET_AND_BITWISE_AND_RELEASE, GET_AND_BITWISE_XOR, GET_AND_BITWISE_XOR_ACQUIRE,
        GET_AND_BITWISE_XOR_RELEASE;
    }
}
