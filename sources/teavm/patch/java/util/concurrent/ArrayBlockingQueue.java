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
    public int drainTo(java.util.Collection<? super E> c) { int n = 0; while (!queue.isEmpty()) { c.add(queue.poll()); n++; } return n; }
    public int drainTo(java.util.Collection<? super E> c, int maxElements) { int n = 0; while (n < maxElements && !queue.isEmpty()) { c.add(queue.poll()); n++; } return n; }
    public int remainingCapacity() { return Integer.MAX_VALUE; }
    public E poll(long timeout, TimeUnit unit) throws InterruptedException { return poll(); }
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException { return offer(e); }
}
