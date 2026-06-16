package java.lang;

/**
 * TeaVM stub for java.lang.Integer, adding missing methods.
 */
public final class Integer extends Number implements Comparable<Integer> {
    public static final int MIN_VALUE = 0x80000000;
    public static final int MAX_VALUE = 0x7fffffff;
    public static final Class<Integer> TYPE = int.class;

    private final int value;

    public Integer(int value) { this.value = value; }
    public Integer(String s) { this.value = parseInt(s, 10); }

    public static int parseInt(String s) { return parseInt(s, 10); }
    public static int parseInt(String s, int radix) {
        return java.lang.Integer.parseInt(s, radix);
    }

    public static int parseUnsignedInt(String s) { return parseUnsignedInt(s, 10); }
    public static int parseUnsignedInt(String s, int radix) {
        long result = java.lang.Long.parseLong(s, radix);
        if (result < 0 || result > 0xFFFFFFFFL) {
            throw new NumberFormatException("Out of unsigned int range: " + s);
        }
        return (int) result;
    }

    public static Integer valueOf(int i) { return new Integer(i); }
    public static Integer valueOf(String s) { return new Integer(s); }
    public static Integer valueOf(String s, int radix) { return new Integer(parseInt(s, radix)); }

    public int intValue() { return value; }
    public long longValue() { return value; }
    public float floatValue() { return value; }
    public double doubleValue() { return value; }

    public static String toString(int i) { return java.lang.Integer.toString(i); }
    public static String toString(int i, int radix) { return java.lang.Integer.toString(i, radix); }
    public static String toUnsignedString(int i) { return java.lang.Long.toString(i & 0xFFFFFFFFL); }
    public static String toUnsignedString(int i, int radix) { return java.lang.Long.toString(i & 0xFFFFFFFFL, radix); }
    public static String toBinaryString(int i) { return java.lang.Integer.toBinaryString(i); }
    public static String toHexString(int i) { return java.lang.Integer.toHexString(i); }
    public static String toOctalString(int i) { return java.lang.Integer.toOctalString(i); }

    public static int compare(int x, int y) { return x < y ? -1 : (x > y ? 1 : 0); }
    public static int compareUnsigned(int x, int y) { return compare(x ^ 0x80000000, y ^ 0x80000000); }
    public static int signum(int i) { return i >> 31 | (-i >>> 31); }
    public static int bitCount(int i) { return java.lang.Integer.bitCount(i); }
    public static int highestOneBit(int i) { return i & (java.lang.Integer.MIN_VALUE >>> numberOfLeadingZeros(i)); }
    public static int lowestOneBit(int i) { return i & -i; }
    public static int numberOfLeadingZeros(int i) { return java.lang.Integer.numberOfLeadingZeros(i); }
    public static int numberOfTrailingZeros(int i) { return java.lang.Integer.numberOfTrailingZeros(i); }
    public static int reverse(int i) { return java.lang.Integer.reverse(i); }
    public static int reverseBytes(int i) { return java.lang.Integer.reverseBytes(i); }
    public static int rotateLeft(int i, int distance) { return (i << distance) | (i >>> (32 - distance)); }
    public static int rotateRight(int i, int distance) { return (i >>> distance) | (i << (32 - distance)); }
    public static int sum(int a, int b) { return a + b; }
    public static int max(int a, int b) { return Math.max(a, b); }
    public static int min(int a, int b) { return Math.min(a, b); }

    public static String toHexString(int i, int minDigits) {
        String s = java.lang.Integer.toHexString(i);
        while (s.length() < minDigits) s = "0" + s;
        return s;
    }

    public static int decode(String nm) {
        return java.lang.Integer.decode(nm);
    }

    public static Integer getInteger(String nm) { return null; }
    public static Integer getInteger(String nm, int val) { return val; }
    public static Integer getInteger(String nm, Integer val) { return val; }

    public int compareTo(Integer another) { return compare(value, another.value); }

    public boolean equals(Object obj) {
        if (obj instanceof Integer) return value == ((Integer) obj).value;
        return false;
    }

    public int hashCode() { return value; }
    public static int hashCode(int value) { return value; }

    public String toString() { return toString(value); }
}
