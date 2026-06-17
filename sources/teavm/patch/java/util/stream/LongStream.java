package java.util.stream;
public interface LongStream extends BaseStream<Long, LongStream> {
    interface Builder extends java.util.function.LongConsumer {
        void accept(long t);
        default Builder add(long t) { accept(t); return this; }
        LongStream build();
    }
}
