package java.lang;

public final class Long extends Number implements Comparable<Long> {
    public static final long MIN_VALUE = 0x8000000000000000L;
    public static final long MAX_VALUE = 0x7fffffffffffffffL;
    public static final Class<Long> TYPE = long.class;

    private final long value;

    public Long(long value) { this.value = value; }
    public Long(String s) { this.value = parseLong(s, 10); }

    public static long parseLong(String s) { return parseLong(s, 10); }
    public static long parseLong(String s, int radix) {
        return java.lang.Long.parseLong(s, radix);
    }

    public static long parseUnsignedLong(String s) { return parseUnsignedLong(s, 10); }
    public static long parseUnsignedLong(String s, int radix) {
        if (s == null) throw new NumberFormatException("null");
        long result = 0;
        boolean negative = false;
        int i = 0;
        int len = s.length();
        if (len == 0) throw new NumberFormatException("Empty string");
        char first = s.charAt(0);
        if (first == '-') { negative = true; i++; }
        else if (first == '+') { i++; }
        if (i >= len) throw new NumberFormatException(s);
        while (i < len) {
            int digit = Character.digit(s.charAt(i++), radix);
            if (digit < 0) throw new NumberFormatException(s);
            if (result < 0 || (result >>> (64 - 4)) != 0 || (Long.MAX_VALUE - digit) / radix < result) {
                throw new NumberFormatException("Out of unsigned long range: " + s);
            }
            result = result * radix + digit;
        }
        if (negative && result != 0) throw new NumberFormatException("Out of unsigned long range: " + s);
        return result;
    }

    public static Long valueOf(long l) { return new Long(l); }
    public static Long valueOf(String s) { return new Long(s); }
    public static Long valueOf(String s, int radix) { return new Long(parseLong(s, radix)); }

    public int intValue() { return (int) value; }
    public long longValue() { return value; }
    public float floatValue() { return value; }
    public double doubleValue() { return value; }

    public static String toString(long l) { return java.lang.Long.toString(l); }
    public static String toString(long l, int radix) { return java.lang.Long.toString(l, radix); }
    public static String toUnsignedString(long l) { return toString(l); }
    public static String toUnsignedString(long l, int radix) {
        if (l >= 0) return toString(l, radix);
        // Split into high and low parts
        long high = l >>> 32;
        long low = l & 0xFFFFFFFFL;
        long base = 1L << 32;
        StringBuilder sb = new StringBuilder();
        while (high > 0 || low > 0) {
            long[] divmod = unsignedDivMod(high, low, radix, base);
            high = divmod[0];
            long newLow = divmod[1];
            long rem = divmod[2];
            sb.insert(0, Character.forDigit((int) rem, radix));
            low = newLow;
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }
    private static long[] unsignedDivMod(long high, long low, int radix, long base) {
        // Simple implementation: use BigInteger-like math
        // For browser stub, return all zeros
        return new long[]{0, 0, 0};
    }
    public static String toBinaryString(long l) { return java.lang.Long.toBinaryString(l); }
    public static String toHexString(long l) { return java.lang.Long.toHexString(l); }
    public static String toOctalString(long l) { return java.lang.Long.toOctalString(l); }

    public static int compare(long x, long y) { return x < y ? -1 : (x > y ? 1 : 0); }
    public static int compareUnsigned(long x, long y) { return compare(x ^ MIN_VALUE, y ^ MIN_VALUE); }
    public static int signum(long i) { return (int) (i >> 63 | (-i >>> 63)); }
    public static long bitCount(long i) { return java.lang.Long.bitCount(i); }
    public static int numberOfLeadingZeros(long i) { return java.lang.Long.numberOfLeadingZeros(i); }
    public static int numberOfTrailingZeros(long i) { return java.lang.Long.numberOfTrailingZeros(i); }
    public static long highestOneBit(long i) { return i & (MIN_VALUE >>> numberOfLeadingZeros(i)); }
    public static long lowestOneBit(long i) { return i & -i; }
    public static long reverse(long i) { return java.lang.Long.reverse(i); }
    public static long reverseBytes(long i) { return java.lang.Long.reverseBytes(i); }
    public static long rotateLeft(long i, int distance) { return (i << distance) | (i >>> (64 - distance)); }
    public static long rotateRight(long i, int distance) { return (i >>> distance) | (i << (64 - distance)); }
    public static long sum(long a, long b) { return a + b; }
    public static long max(long a, long b) { return Math.max(a, b); }
    public static long min(long a, long b) { return Math.min(a, b); }

    public static long decode(String nm) { return java.lang.Long.decode(nm); }

    public int compareTo(Long another) { return compare(value, another.value); }

    public boolean equals(Object obj) {
        if (obj instanceof Long) return value == ((Long) obj).value;
        return false;
    }

    public int hashCode() { return (int) (value ^ (value >>> 32)); }
    public static int hashCode(long value) { return (int) (value ^ (value >>> 32)); }

    public String toString() { return toString(value); }
}
