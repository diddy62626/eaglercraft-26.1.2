package java.util.concurrent;
public class CopyOnWriteArrayList<E> implements java.util.List<E> {
    private final java.util.List<E> list = new java.util.ArrayList<>();
    public CopyOnWriteArrayList() {}
    public boolean add(E e) { return list.add(e); }
    public E get(int index) { return list.get(index); }
    public E set(int index, E element) { return list.set(index, element); }
    public void add(int index, E element) { list.add(index, element); }
    public E remove(int index) { return list.remove(index); }
    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public boolean contains(Object o) { return list.contains(o); }
    public java.util.Iterator<E> iterator() { return list.iterator(); }
    public java.util.ListIterator<E> listIterator() { return list.listIterator(); }
    public java.util.ListIterator<E> listIterator(int index) { return list.listIterator(index); }
    public java.util.List<E> subList(int fromIndex, int toIndex) { return list.subList(fromIndex, toIndex); }
    public Object[] toArray() { return list.toArray(); }
    public <T> T[] toArray(T[] a) { return list.toArray(a); }
    public boolean remove(Object o) { return list.remove(o); }
    public boolean containsAll(java.util.Collection<?> c) { return list.containsAll(c); }
    public boolean addAll(java.util.Collection<? extends E> c) { return list.addAll(c); }
    public boolean addAll(int index, java.util.Collection<? extends E> c) { return list.addAll(index, c); }
    public boolean removeAll(java.util.Collection<?> c) { return list.removeAll(c); }
    public boolean retainAll(java.util.Collection<?> c) { return list.retainAll(c); }
    public void clear() { list.clear(); }
    public int indexOf(Object o) { return list.indexOf(o); }
    public int lastIndexOf(Object o) { return list.lastIndexOf(o); }
}
