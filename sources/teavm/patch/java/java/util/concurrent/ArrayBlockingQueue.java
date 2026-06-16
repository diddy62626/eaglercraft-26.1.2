package java.util.concurrent;
public class ArrayBlockingQueue<E> extends java.util.AbstractQueue<E> implements BlockingQueue<E> {
    public ArrayBlockingQueue(int capacity) {}
    public ArrayBlockingQueue(int capacity, boolean fair) {}
    public boolean offer(E e) { return false; }
    public E poll() { return null; }
    public E peek() { return null; }
    public int size() { return 0; }
    public void put(E e) {}
    public E take() { return null; }
}