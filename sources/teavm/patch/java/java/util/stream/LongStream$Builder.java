package java.util.stream;
public interface LongStream$Builder extends java.util.function.LongConsumer {
    void accept(long t);
    default LongStream$Builder add(long t) { accept(t); return this; }
    LongStream build();
}