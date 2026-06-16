package java.lang.reflect;
public final class Method extends Executable {
    public Object invoke(Object obj, Object... args) { return null; }
    public String getName() { return ""; }
    public Class<?> getReturnType() { return void.class; }
    public Class<?> getDeclaringClass() { return Object.class; }
    public Class<?>[] getParameterTypes() { return new Class[0]; }
}