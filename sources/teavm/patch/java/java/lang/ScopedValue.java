package java.lang;
public final class ScopedValue<T> {
    public ScopedValue() {}
    public T get() { return null; }
    public T orElse(T defaultValue) { return defaultValue; }
    public boolean isBound() { return false; }
    public static final class Carrier {
        public <T> Carrier bind(ScopedValue<T> key, T value) { return this; }
        public Runnable run(Runnable op) { op.run(); return null; }
    }
    public static <T> ScopedValue<T> newInstance() { return new ScopedValue<>(); }
    public static Carrier where(ScopedValue<?> key, Object value) { return new Carrier(); }
}