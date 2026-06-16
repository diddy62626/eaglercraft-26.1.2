package java.util.stream;
public interface Stream<T> extends BaseStream<T, Stream<T>> {
    static <T> Stream<T> empty() { return null; }
    interface Builder<T> extends java.util.function.Consumer<T> {
        void accept(T t);
        default Builder<T> add(T t) { accept(t); return this; }
        Stream<T> build();
    }
}
