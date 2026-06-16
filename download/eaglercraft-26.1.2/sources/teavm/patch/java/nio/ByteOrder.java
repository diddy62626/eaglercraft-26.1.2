package java.nio;

/**
 * TeaVM stub for java.nio.ByteOrder.
 * Browser is typically little-endian, but MC expects BIG_ENDIAN for network/rendering.
 */
public final class ByteOrder {

    private String name;

    private ByteOrder(String name) {
        this.name = name;
    }

    public static final ByteOrder BIG_ENDIAN = new ByteOrder("BIG_ENDIAN");
    public static final ByteOrder LITTLE_ENDIAN = new ByteOrder("LITTLE_ENDIAN");

    /**
     * Returns the native byte order of the platform.
     * Most browsers run on little-endian hardware.
     */
    public static ByteOrder nativeOrder() {
        return LITTLE_ENDIAN;
    }

    @Override
    public String toString() {
        return name;
    }
}
