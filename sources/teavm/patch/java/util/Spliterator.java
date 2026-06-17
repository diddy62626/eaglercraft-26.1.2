package java.util;

public interface Spliterator<T> {
    boolean tryAdvance(java.util.function.Consumer<? super T> action);
    Spliterator<T> trySplit();
    long estimateSize();
    int characteristics();
    default void forEachRemaining(java.util.function.Consumer<? super T> action) {
        while (tryAdvance(action));
    }
    default long getExactSizeIfKnown() { return -1L; }
    default boolean hasCharacteristics(int characteristics) {
        return (characteristics() & characteristics) == characteristics;
    }
    default Comparator<? super T> getComparator() {
        throw new IllegalStateException();
    }

    interface OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>>
            extends Spliterator<T> {
        @SuppressWarnings("overloads")
        boolean tryAdvance(T_CONS action);
        @SuppressWarnings("overloads")
        default void forEachRemaining(T_CONS action) {
            while (tryAdvance(action));
        }
        @Override
        T_SPLITR trySplit();
    }

    interface OfInt extends Spliterator.OfPrimitive<Integer, java.util.function.IntConsumer, Spliterator.OfInt> {
        boolean tryAdvance(java.util.function.IntConsumer action);
        default boolean tryAdvance(java.util.function.Consumer<? super Integer> action) {
            return tryAdvance((java.util.function.IntConsumer) action::accept);
        }
        @Override
        Spliterator.OfInt trySplit();
    }

    interface OfLong extends Spliterator.OfPrimitive<Long, java.util.function.LongConsumer, Spliterator.OfLong> {
        boolean tryAdvance(java.util.function.LongConsumer action);
        default boolean tryAdvance(java.util.function.Consumer<? super Long> action) {
            return tryAdvance((java.util.function.LongConsumer) action::accept);
        }
        @Override
        Spliterator.OfLong trySplit();
    }

    interface OfDouble extends Spliterator.OfPrimitive<Double, java.util.function.DoubleConsumer, Spliterator.OfDouble> {
        boolean tryAdvance(java.util.function.DoubleConsumer action);
        default boolean tryAdvance(java.util.function.Consumer<? super Double> action) {
            return tryAdvance((java.util.function.DoubleConsumer) action::accept);
        }
        @Override
        Spliterator.OfDouble trySplit();
    }

    int ORDERED = 0x00000010;
    int DISTINCT = 0x00000001;
    int SORTED = 0x00000004;
    int SIZED = 0x00000040;
    int NONNULL = 0x00000100;
    int IMMUTABLE = 0x00000400;
    int CONCURRENT = 0x00001000;
    int SUBSIZED = 0x00004000;
}
