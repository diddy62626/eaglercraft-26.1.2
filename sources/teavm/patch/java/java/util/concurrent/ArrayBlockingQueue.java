package java.util.concurrent;
public class ArrayBlockingQueue<E> extends java.util.AbstractQueue<E> implements BlockingQueue<E> {
    private final java.util.Queue<E> queue = new java.util.LinkedList<>();
    public ArrayBlockingQueue(int capacity) {}
    public ArrayBlockingQueue(int capacity, boolean fair) {}
    public boolean offer(E e) { return queue.offer(e); }
    public E poll() { return queue.poll(); }
    public E peek() { return queue.peek(); }
    public int size() { return queue.size(); }
    public void put(E e) { offer(e); }
    public E take() { return poll(); }
    public java.util.Iterator<E> iterator() { return queue.iterator(); }
}
