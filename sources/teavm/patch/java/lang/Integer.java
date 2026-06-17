package java.lang;

/**
 * TeaVM stub for java.lang.Integer.
 */
public final class Integer extends Number implements Comparable<Integer> {
    public static final int MIN_VALUE = 0x80000000;
    public static final int MAX_VALUE = 0x7fffffff;
    public static final Class<Integer> TYPE = int.class;

    private final int value;

    public Integer(int value) { this.value = value; }
    public Integer(String s) { this.value = parseInt(s, 10); }

    public static int parseInt(String s, int radix) throws NumberFormatException {
        if (s == null) throw new NumberFormatException("null");
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            throw new NumberFormatException("radix " + radix + " out of range");
        boolean negative = false;
        int i = 0;
        int len = s.length();
        if (len == 0) throw new NumberFormatException("Zero input string");
        char first = s.charAt(0);
        if (first == '-') { negative = true; i++; }
        else if (first == '+') { i++; }
        if (i >= len) throw new NumberFormatException(s);
        int result = 0;
        while (i < len) {
            int digit = Character.digit(s.charAt(i++), radix);
            if (digit < 0) throw new NumberFormatException(s);
            if (result < Integer.MIN_VALUE / radix) throw new NumberFormatException(s);
            result *= radix;
            if (result < Integer.MIN_VALUE + digit) throw new NumberFormatException(s);
            result -= digit;
        }
        return negative ? result : -result;
    }

    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s, 10);
    }

    public static int parseUnsignedInt(String s, int radix) throws NumberFormatException {
        if (s == null) throw new NumberFormatException("null");
        long result = 0;
        boolean negative = false;
        int i = 0;
        int len = s.length();
        if (len == 0) throw new NumberFormatException("Zero input string");
        char first = s.charAt(0);
        if (first == '-') { negative = true; i++; }
        else if (first == '+') { i++; }
        if (i >= len) throw new NumberFormatException(s);
        while (i < len) {
            int digit = Character.digit(s.charAt(i++), radix);
            if (digit < 0) throw new NumberFormatException(s);
            if (result > (0xFFFFFFFFL - digit) / radix) throw new NumberFormatException(s);
            result = result * radix + digit;
        }
        if (negative) {
            if (result != 0) throw new NumberFormatException(s);
            return 0;
        }
        return (int) result;
    }

    public static int parseUnsignedInt(String s) throws NumberFormatException {
        return parseUnsignedInt(s, 10);
    }

    public static Integer valueOf(int i) { return new Integer(i); }
    public static Integer valueOf(String s) throws NumberFormatException { return new Integer(parseInt(s, 10)); }
    public static Integer valueOf(String s, int radix) throws NumberFormatException { return new Integer(parseInt(s, radix)); }

    public byte byteValue() { return (byte) value; }
    public short shortValue() { return (short) value; }
    public int intValue() { return value; }
    public long longValue() { return (long) value; }
    public float floatValue() { return (float) value; }
    public double doubleValue() { return (double) value; }

    // Helper: convert non-negative long to string in given radix
    private static String longToString(long l, int radix) {
        if (l == 0) return "0";
        char[] buf = new char[65];
        int pos = buf.length;
        while (l > 0) {
            buf[--pos] = Character.forDigit((int)(l % radix), radix);
            l /= radix;
        }
        return new String(buf, pos, buf.length - pos);
    }

    public static String toString(int i, int radix) {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) radix = 10;
        if (i == 0) return "0";
        boolean negative = i < 0;
        if (negative) {
            if (i == Integer.MIN_VALUE) {
                // 2147483648 in unsigned
                long unsignedPart = -(long)Integer.MIN_VALUE;
                return "-" + longToString(unsignedPart, radix);
            }
            i = -i;
        }
        return (negative ? "-" : "") + longToString((long)i, radix);
    }

    public static String toString(int i) { return toString(i, 10); }

    public static String toUnsignedString(int i, int radix) {
        // Treat as unsigned: convert to long with high 32 bits zero
        long unsigned = i & 0xFFFFFFFFL;
        return longToString(unsigned, radix);
    }

    public static String toUnsignedString(int i) { return toUnsignedString(i, 10); }

    public static String toBinaryString(int i) {
        return toUnsignedString(i, 2);
    }

    public static String toHexString(int i) {
        return toUnsignedString(i, 16);
    }

    public static String toOctalString(int i) {
        return toUnsignedString(i, 8);
    }

    public static int compare(int x, int y) { return x < y ? -1 : (x > y ? 1 : 0); }
    public static int compareUnsigned(int x, int y) { return compare(x ^ 0x80000000, y ^ 0x80000000); }
    public static int signum(int i) { return (i >> 31) | (-i >>> 31); }
    public static int bitCount(int i) {
        i = i - ((i >>> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        i = (i + (i >>> 4)) & 0x0f0f0f0f;
        i = i + (i >>> 8);
        i = i + (i >>> 16);
        return i & 0x3f;
    }
    public static int numberOfLeadingZeros(int i) {
        if (i == 0) return 32;
        int n = 1;
        if (i >>> 16 == 0) { n += 16; i <<= 16; }
        if (i >>> 24 == 0) { n += 8; i <<= 8; }
        if (i >>> 28 == 0) { n += 4; i <<= 4; }
        if (i >>> 30 == 0) { n += 2; i <<= 2; }
        n -= i >>> 31;
        return n;
    }
    public static int numberOfTrailingZeros(int i) {
        if (i == 0) return 32;
        int n = 0;
        while ((i & 1) == 0) { n++; i >>>= 1; }
        return n;
    }
    public static int highestOneBit(int i) { return i & (Integer.MIN_VALUE >>> numberOfLeadingZeros(i)); }
    public static int lowestOneBit(int i) { return i & -i; }
    public static int reverse(int i) {
        i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;
        i = (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333;
        i = (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f;
        i = (i << 24) | ((i & 0xff00) << 8) | ((i >>> 8) & 0xff00) | (i >>> 24);
        return i;
    }
    public static int reverseBytes(int i) {
        return ((i >>> 24)) | ((i >> 8) & 0xff00) | ((i << 8) & 0xff0000) | (i << 24);
    }
    public static int rotateLeft(int i, int distance) { return (i << distance) | (i >>> (32 - distance)); }
    public static int rotateRight(int i, int distance) { return (i >>> distance) | (i << (32 - distance)); }
    public static int sum(int a, int b) { return a + b; }
    public static int max(int a, int b) { return Math.max(a, b); }
    public static int min(int a, int b) { return Math.min(a, b); }

    public static Integer decode(String nm) throws NumberFormatException {
        if (nm == null) throw new NumberFormatException("null");
        int radix = 10;
        int index = 0;
        boolean negative = false;
        if (nm.startsWith("-")) { negative = true; index++; }
        else if (nm.startsWith("+")) { index++; }
        if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) { radix = 16; index += 2; }
        else if (nm.startsWith("#", index)) { radix = 16; index++; }
        else if (nm.startsWith("0", index) && nm.length() > 1 + index) { radix = 8; index++; }
        String num = nm.substring(index);
        int result = parseInt(num, radix);
        return new Integer(negative ? -result : result);
    }

    public static Integer getInteger(String nm) { return getInteger(nm, null); }
    public static Integer getInteger(String nm, int val) { return getInteger(nm, new Integer(val)); }
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
