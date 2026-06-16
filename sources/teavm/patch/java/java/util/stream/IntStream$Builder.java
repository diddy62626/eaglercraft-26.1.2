package java.util.stream;
public interface IntStream$Builder extends java.util.function.IntConsumer {
    void accept(int t);
    default IntStream$Builder add(int t) { accept(t); return this; }
    IntStream build();
}