package java.lang.reflect;
public final class Modifier {
    public static final int PUBLIC = 1, PRIVATE = 2, PROTECTED = 4, STATIC = 8;
    public static final int FINAL = 16, SYNCHRONIZED = 32, VOLATILE = 64;
    public static final int TRANSIENT = 128, NATIVE = 256, INTERFACE = 512;
    public static final int ABSTRACT = 1024, STRICT = 2048;
    public static boolean isPublic(int mod) { return (mod & PUBLIC) != 0; }
    public static boolean isPrivate(int mod) { return (mod & PRIVATE) != 0; }
    public static boolean isProtected(int mod) { return (mod & PROTECTED) != 0; }
    public static boolean isStatic(int mod) { return (mod & STATIC) != 0; }
    public static boolean isFinal(int mod) { return (mod & FINAL) != 0; }
    public static boolean isAbstract(int mod) { return (mod & ABSTRACT) != 0; }
    public static boolean isInterface(int mod) { return (mod & INTERFACE) != 0; }
    public static boolean isNative(int mod) { return (mod & NATIVE) != 0; }
    public static boolean isSynchronized(int mod) { return (mod & SYNCHRONIZED) != 0; }
    public static boolean isVolatile(int mod) { return (mod & VOLATILE) != 0; }
    public static boolean isTransient(int mod) { return (mod & TRANSIENT) != 0; }
    public static String toString(int mod) { return ""; }
}