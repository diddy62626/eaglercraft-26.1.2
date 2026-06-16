package java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * TeaVM stub for java.util.concurrent.LinkedBlockingQueue.
 * Simple LinkedList-backed implementation; single-threaded so no real blocking.
 */
public class LinkedBlockingQueue<E> extends java.util.AbstractQueue<E> implements BlockingQueue<E>, java.io.Serializable {

    private final LinkedList<E> queue;
    private final int capacity;

    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public LinkedBlockingQueue(Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        queue.addAll(c);
    }

    // -- BlockingQueue methods --

    @Override
    public boolean add(E e) {
        if (offer(e)) return true;
        throw new IllegalStateException("Queue full");
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        if (queue.size() >= capacity) return false;
        queue.addLast(e);
        return true;
    }

    @Override
    public void put(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        queue.addLast(e);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offer(e);
    }

    @Override
    public E take() throws InterruptedException {
        if (queue.isEmpty()) return null;
        return queue.removeFirst();
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return poll();
    }

    @Override
    public int remainingCapacity() {
        return capacity - queue.size();
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        int count = 0;
        while (!queue.isEmpty()) {
            c.add(queue.removeFirst());
            count++;
        }
        return count;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        int count = 0;
        while (!queue.isEmpty() && count < maxElements) {
            c.add(queue.removeFirst());
            count++;
        }
        return count;
    }

    // -- AbstractQueue methods --

    @Override
    public E poll() {
        if (queue.isEmpty()) return null;
        return queue.removeFirst();
    }

    @Override
    public E peek() {
        if (queue.isEmpty()) return null;
        return queue.getFirst();
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }
}
