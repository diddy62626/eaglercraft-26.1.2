package java.util;
public class Spliterators {
    public static abstract class AbstractSpliterator<T> implements Spliterator<T> {
        public long estimateSize() { return Long.MAX_VALUE; }
        public int characteristics() { return 0; }
    }

// === Plugin-injected methods ===
    public static java.util.Iterator iterator(java.util.Spliterator.OfInt spliterator) { return new java.util.ArrayList<Integer>().iterator(); }
    public static java.util.Iterator iterator(java.util.Spliterator spliterator) { return new java.util.ArrayList<Object>().iterator(); }
}
