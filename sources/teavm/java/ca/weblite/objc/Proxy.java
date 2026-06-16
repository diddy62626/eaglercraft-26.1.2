package ca.weblite.objc;

public class Proxy {
    public static NSObject create(String className, Object... args) { return new NSObject(); }
    public static NSObject fromPointer(long peer) { return new NSObject(peer); }
    public static NSObject registerMethodCallback(String selector) { return new NSObject(); }
}
