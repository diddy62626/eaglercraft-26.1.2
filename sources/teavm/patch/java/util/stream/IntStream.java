package java.util.stream;
public interface IntStream extends BaseStream<Integer, IntStream> {
    interface Builder extends java.util.function.IntConsumer {
        void accept(int t);
        default Builder add(int t) { accept(t); return this; }
        IntStream build();
    }
}
