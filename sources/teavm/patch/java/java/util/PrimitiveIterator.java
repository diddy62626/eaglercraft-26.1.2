package java.util;
public interface PrimitiveIterator<T, T_CONS> extends Iterator<T> {
    interface OfLong extends PrimitiveIterator<Long, java.util.function.LongConsumer> {
        long nextLong();
        default Long next() { return nextLong(); }
    }
}