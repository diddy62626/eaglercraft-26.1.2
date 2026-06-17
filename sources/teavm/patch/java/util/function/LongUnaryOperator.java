package java.util.function;

/**
 * TeaVM stub for java.util.function.LongUnaryOperator.
 */
@FunctionalInterface
public interface LongUnaryOperator {

    long applyAsLong(long operand);

    default LongUnaryOperator compose(LongUnaryOperator before) {
        return (long v) -> applyAsLong(before.applyAsLong(v));
    }

    default LongUnaryOperator andThen(LongUnaryOperator after) {
        return (long v) -> after.applyAsLong(applyAsLong(v));
    }

    static LongUnaryOperator identity() {
        return t -> t;
    }
}
