package java.util;
public interface Spliterator<T> {
    boolean tryAdvance(java.util.function.Consumer<? super T> action);
    Spliterator<T> trySplit();
    long estimateSize();
    int characteristics();
    interface OfLong extends Spliterator<Long> {
        boolean tryAdvance(java.util.function.LongConsumer action);
    }
}
