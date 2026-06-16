package java.util.concurrent;
public class CopyOnWriteArrayList<E> implements java.util.List<E> {
    public CopyOnWriteArrayList() {}
    public boolean add(E e) { return true; }
    public E get(int index) { return null; }
    public int size() { return 0; }
    public java.util.Iterator<E> iterator() { return java.util.Collections.<E>emptyList().iterator(); }
}