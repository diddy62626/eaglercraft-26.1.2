package java.lang.invoke;
public abstract class MethodHandle {
    public Object invoke(Object... args) throws Throwable { return null; }
    public Object invokeExact(Object... args) throws Throwable { return null; }
    public Object invokeWithArguments(Object... args) throws Throwable { return null; }
    public MethodType type() { return null; }
}