package java.util.concurrent.atomic;
public class AtomicReferenceArray<E> {
    private final Object[] array;
    public AtomicReferenceArray(int length) { array = new Object[length]; }
    public E get(int i) { return (E) array[i]; }
    public void set(int i, E newValue) { array[i] = newValue; }
    public boolean compareAndSet(int i, E expect, E update) { if (array[i] == expect) { array[i] = update; return true; } return false; }
    public int length() { return array.length; }
}