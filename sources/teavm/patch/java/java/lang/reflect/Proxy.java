package java.lang.reflect;
public class Proxy {
    protected InvocationHandler h;
    public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) { return null; }
    public static boolean isProxyClass(Class<?> cl) { return false; }
    public static InvocationHandler getInvocationHandler(Object proxy) { return null; }
}