package java.lang.invoke;
public abstract class MethodHandle {
    public Object invoke(Object... args) throws Throwable { return null; }
    public Object invokeExact(Object... args) throws Throwable { return null; }
    public Object invokeWithArguments(Object... args) throws Throwable { return null; }
    public MethodType type() { return null; }

// === Plugin-injected methods ===
    public MethodHandle bindTo(Object x) { return this; }
    public MethodHandle asType(MethodType newType) { return this; }
}