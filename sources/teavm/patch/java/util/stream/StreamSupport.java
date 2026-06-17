package java.util.stream;

public final class StreamSupport {
    private StreamSupport() {}

    public static <T> Stream<T> stream(java.util.Spliterator<T> spliterator, boolean parallel) {
        return null;
    }
    public static <T> Stream<T> stream(java.util.function.Supplier<java.util.Spliterator<T>> supplier, int characteristics, boolean parallel) {
        return null;
    }
    public static IntStream intStream(java.util.Spliterator.OfInt spliterator, boolean parallel) {
        return null;
    }
    public static IntStream intStream(java.util.function.Supplier<java.util.Spliterator.OfInt> supplier, int characteristics, boolean parallel) {
        return null;
    }
    public static LongStream longStream(java.util.Spliterator.OfLong spliterator, boolean parallel) {
        return null;
    }
    public static LongStream longStream(java.util.function.Supplier<java.util.Spliterator.OfLong> supplier, int characteristics, boolean parallel) {
        return null;
    }
    public static DoubleStream doubleStream(java.util.Spliterator.OfDouble spliterator, boolean parallel) {
        return null;
    }
    public static DoubleStream doubleStream(java.util.function.Supplier<java.util.Spliterator.OfDouble> supplier, int characteristics, boolean parallel) {
        return null;
    }
}
