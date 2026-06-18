package java.security;

public class AccessController {
    public static void checkPermission(Permission perm) {}
    public static <T> T doPrivileged(java.security.PrivilegedAction<T> action) { return action.run(); }
}
