package java.util;
public class Spliterators {
    public static abstract class AbstractSpliterator<T> implements Spliterator<T> {
        public long estimateSize() { return Long.MAX_VALUE; }
        public int characteristics() { return 0; }
    }
}
