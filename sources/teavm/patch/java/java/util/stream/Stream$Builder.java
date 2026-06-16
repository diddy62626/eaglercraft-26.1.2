package java.util.stream;
public interface Stream$Builder<T> extends java.util.function.Consumer<T> {
    void accept(T t);
    default Stream$Builder<T> add(T t) { accept(t); return this; }
    Stream<T> build();
}