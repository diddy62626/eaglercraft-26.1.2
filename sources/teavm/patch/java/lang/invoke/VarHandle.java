package java.lang.invoke;

/**
 * EaglerCraft stub for java.lang.invoke.VarHandle.
 *
 * Real VarHandle has polymorphic signatures (each call site has its own
 * signature). To satisfy TeaVM without predicting every signature, we
 * provide Object-taking overloads for the most common Arity (2 and 3 args).
 * Java reference-type widening lets these match most Guava call sites.
 */
public abstract class VarHandle {
    protected VarHandle() {}

    // 2-arg variants (returns Object - widened from concrete types)
    public Object get(Object a, Object b) { return null; }
    public Object getVolatile(Object a, Object b) { return null; }
    public Object getOpaque(Object a, Object b) { return null; }
    public Object getAndSet(Object a, Object b) { return null; }
    public Object getAndSetAcquire(Object a, Object b) { return null; }
    public Object getAndSetRelease(Object a, Object b) { return null; }
    public void set(Object a, Object b, Object c) {}
    public void setVolatile(Object a, Object b, Object c) {}
    public void setOpaque(Object a, Object b, Object c) {}
    public void setRelease(Object a, Object b) {}
    public void setRelease(Object a, Object b, Object c) {}

    // 3-arg compareAndSet / compareAndExchange
    public boolean compareAndSet(Object a, Object b, Object c) { return false; }
    public boolean weakCompareAndSet(Object a, Object b, Object c) { return false; }
    public boolean weakCompareAndSetPlain(Object a, Object b, Object c) { return false; }
    public boolean weakCompareAndSetVolatile(Object a, Object b, Object c) { return false; }
    public boolean weakCompareAndSetAcquire(Object a, Object b, Object c) { return false; }
    public boolean weakCompareAndSetRelease(Object a, Object b, Object c) { return false; }
    public Object compareAndExchange(Object a, Object b, Object c) { return null; }
    public Object compareAndExchangeAcquire(Object a, Object b, Object c) { return null; }
    public Object compareAndExchangeRelease(Object a, Object b, Object c) { return null; }

    // 3-arg getAndAdd / getAndBitwise* (returns Object for compatibility)
    public Object getAndAdd(Object a, Object b, Object c) { return null; }
    public Object getAndBitwiseOr(Object a, Object b, Object c) { return null; }
    public Object getAndBitwiseAnd(Object a, Object b, Object c) { return null; }
    public Object getAndBitwiseXor(Object a, Object b, Object c) { return null; }

    // Static fence operations
    public static void storeStoreFence() {}
    public static void loadLoadFence() {}
    public static void acquireFence() {}
    public static void releaseFence() {}
    public static void fullFence() {}

    // Misc
    public java.lang.invoke.MethodHandle toMethodHandle(java.lang.invoke.VarHandle.AccessMode accessMode) {
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
