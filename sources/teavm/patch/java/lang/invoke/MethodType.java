package java.lang.invoke;
public final class MethodType {
    public static MethodType methodType(Class<?> rtype, Class<?>... ptypes) { return new MethodType(); }
    public static MethodType methodType(Class<?> rtype) { return new MethodType(); }
    public Class<?> returnType() { return Object.class; }
    public Class<?>[] parameterArray() { return new Class[0]; }
}