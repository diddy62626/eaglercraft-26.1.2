package java.util.concurrent;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque.Node;

/**
 * TeaVM stub for ConcurrentLinkedDeque.
 * Backed by a simple non-concurrent ArrayDeque since browser is single-threaded.
 */
public class ConcurrentLinkedDeque<E> extends AbstractCollection<E>
        implements java.util.Deque<E>, java.io.Serializable {
    private static final long serialVersionUID = 876323262645176235L;

    private final java.util.ArrayDeque<E> deque = new java.util.ArrayDeque<>();

    public ConcurrentLinkedDeque() {}
    public ConcurrentLinkedDeque(Collection<? extends E> c) { addAll(c); }

    @Override public boolean add(E e) { return deque.add(e); }
    @Override public boolean offer(E e) { return deque.offer(e); }
    public boolean offerFirst(E e) { deque.offerFirst(e); return true; }
    public boolean offerLast(E e) { deque.offerLast(e); return true; }
    @Override public E remove() { return deque.remove(); }
    public E removeFirst() { return deque.removeFirst(); }
    public E removeLast() { return deque.removeLast(); }
    @Override public E poll() { return deque.poll(); }
    public E pollFirst() { return deque.pollFirst(); }
    public E pollLast() { return deque.pollLast(); }
    @Override public E element() { return deque.element(); }
    public E getFirst() { return deque.getFirst(); }
    public E getLast() { return deque.getLast(); }
    @Override public E peek() { return deque.peek(); }
    public E peekFirst() { return deque.peekFirst(); }
    public E peekLast() { return deque.peekLast(); }
    @Override public void addFirst(E e) { deque.addFirst(e); }
    @Override public void addLast(E e) { deque.addLast(e); }
    @Override public boolean remove(Object o) { return deque.remove(o); }
    public boolean removeFirstOccurrence(Object o) { return deque.removeFirstOccurrence(o); }
    public boolean removeLastOccurrence(Object o) { return deque.removeLastOccurrence(o); }
    @Override public boolean contains(Object o) { return deque.contains(o); }
    @Override public int size() { return deque.size(); }
    @Override public Iterator<E> iterator() { return deque.iterator(); }
    public Iterator<E> descendingIterator() { return deque.descendingIterator(); }
    @Override public void clear() { deque.clear(); }
    @Override public Object[] toArray() { return deque.toArray(); }
    @Override public <T> T[] toArray(T[] a) { return deque.toArray(a); }

    public void push(E e) { addFirst(e); }
    public E pop() { return removeFirst(); }

    static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;
    }
}
