package java.lang.invoke;
public final class MethodHandles {
    public static final class Lookup {
        public Class<?> lookupClass() { return null; }
        public MethodHandle findStatic(Class<?> refc, String name, MethodType type) { return null; }
        public MethodHandle findVirtual(Class<?> refc, String name, MethodType type) { return null; }
        public MethodHandle findSpecial(Class<?> refc, String name, MethodType type, Class<?> specialCaller) { return null; }
        public MethodHandle findGetter(Class<?> refc, String name, Class<?> type) { return null; }
        public MethodHandle findSetter(Class<?> refc, String name, Class<?> type) { return null; }
        public MethodHandle unreflect(java.lang.reflect.Method m) { return null; }
        public MethodHandle unreflectSpecial(java.lang.reflect.Method m, Class<?> specialCaller) { return null; }
    }
    public static Lookup lookup() { return new Lookup(); }
    public static Lookup publicLookup() { return new Lookup(); }
}