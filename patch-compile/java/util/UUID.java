package java.util;

/**
 * TeaVM-compatible UUID with (long, long) constructor support.
 * TeaVM's classlib UUID doesn't have this constructor.
 */
public final class UUID implements java.io.Serializable, Comparable<UUID> {
    private static final long serialVersionUID = -4856846361193249489L;

    private final long mostSigBits;
    private final long leastSigBits;

    public UUID(long mostSigBits, long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    public static UUID randomUUID() {
        long msb = (long)(Math.random() * Long.MAX_VALUE);
        long lsb = (long)(Math.random() * Long.MAX_VALUE);
        msb = (msb & 0xFFFFFFFFFFFF0FFFL) | 0x0000000000004000L;
        lsb = (lsb & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;
        return new UUID(msb, lsb);
    }

    public static UUID fromString(String name) {
        String[] components = name.split("-");
        if (components.length != 5) throw new IllegalArgumentException("Invalid UUID string: " + name);
        long msb = Long.parseLong(components[0], 16);
        msb <<= 16;
        msb |= Long.parseLong(components[1], 16);
        msb <<= 16;
        msb |= Long.parseLong(components[2], 16);
        long lsb = Long.parseLong(components[3], 16);
        lsb <<= 48;
        lsb |= Long.parseLong(components[4], 16);
        return new UUID(msb, lsb);
    }

    public long getMostSignificantBits() { return mostSigBits; }
    public long getLeastSignificantBits() { return leastSigBits; }

    public int version() { return (int)((mostSigBits >> 12) & 0x0F); }
    public int variant() { return (int)((leastSigBits >>> (64 - (leastSigBits >>> 62))) & (leastSigBits >>> 63)); }

    public long timestamp() { return (mostSigBits & 0x0FFFL) << 48 | ((mostSigBits >> 16) & 0x0FFFFL) << 32 | (mostSigBits >>> 32); }
    public int clockSequence() { return (int)((leastSigBits & 0x3FFF000000000000L) >>> 48); }
    public long node() { return leastSigBits & 0x0000FFFFFFFFFFFFL; }

    public String toString() {
        return digits(mostSigBits >> 32, 8) + "-" +
               digits(mostSigBits >> 16, 4) + "-" +
               digits(mostSigBits, 4) + "-" +
               digits(leastSigBits >> 48, 4) + "-" +
               digits(leastSigBits, 12);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    public int hashCode() { return (int)((mostSigBits >> 32) ^ mostSigBits ^ (leastSigBits >> 32) ^ leastSigBits); }

    public boolean equals(Object obj) {
        if (!(obj instanceof UUID)) return false;
        UUID other = (UUID) obj;
        return mostSigBits == other.mostSigBits && leastSigBits == other.leastSigBits;
    }

    public int compareTo(UUID val) {
        int cmp = Long.compare(mostSigBits, val.mostSigBits);
        return cmp != 0 ? cmp : Long.compare(leastSigBits, val.leastSigBits);
    }
}
