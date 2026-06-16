package java.util;

/**
 * TeaVM-compatible stub for HexFormat (Java 17+).
 * Used by MC 26.1.2 for hex encoding/decoding.
 */
public final class HexFormat {
    private final boolean uppercase;
    private final String delimiter;
    private final String prefix;
    private final String suffix;

    private HexFormat(boolean uppercase, String delimiter, String prefix, String suffix) {
        this.uppercase = uppercase;
        this.delimiter = delimiter;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public static HexFormat of() { return new HexFormat(false, "", "", ""); }
    public static HexFormat ofDelimiter(String delimiter) { return new HexFormat(false, delimiter, "", ""); }
    public static HexFormat uppercase() { return new HexFormat(true, "", "", ""); }

    private static final char[] LOWER_DIGITS = "0123456789abcdef".toCharArray();
    private static final char[] UPPER_DIGITS = "0123456789ABCDEF".toCharArray();

    public String formatHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        char[] digits = uppercase ? UPPER_DIGITS : LOWER_DIGITS;
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0 && !delimiter.isEmpty()) sb.append(delimiter);
            sb.append(prefix);
            int b = bytes[i] & 0xFF;
            sb.append(digits[b >>> 4]);
            sb.append(digits[b & 0xF]);
            sb.append(suffix);
        }
        return sb.toString();
    }

    public byte[] parseHex(String string) {
        String cleaned = string.replace(delimiter, "").replace(prefix, "").replace(suffix, "");
        byte[] result = new byte[cleaned.length() / 2];
        for (int i = 0; i < result.length; i++) {
            int hi = Character.digit(cleaned.charAt(i * 2), 16);
            int lo = Character.digit(cleaned.charAt(i * 2 + 1), 16);
            result[i] = (byte) ((hi << 4) | lo);
        }
        return result;
    }

    public char toLowHexDigit(int value) {
        return LOWER_DIGITS[value & 0xF];
    }

    public char toHighHexDigit(int value) {
        return (uppercase ? UPPER_DIGITS : LOWER_DIGITS)[(value >> 4) & 0xF];
    }

    public boolean isHexDigit(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
    }

    public static HexFormat withUpperCase() {
        return new HexFormat(true, "", "", "");
    }

    public long fromHexDigitsToLong(CharSequence sequence) {
        return fromHexDigitsToLong(sequence, 0, sequence.length());
    }

    public long fromHexDigitsToLong(CharSequence sequence, int fromIndex, int toIndex) {
        long result = 0;
        for (int i = fromIndex; i < toIndex; i++) {
            char c = sequence.charAt(i);
            int digit = Character.digit(c, 16);
            if (digit < 0) throw new IllegalArgumentException("Not a hex digit: " + c);
            result = (result << 4) | digit;
        }
        return result;
    }

    public int fromHexDigits(CharSequence sequence) {
        int result = 0;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            int digit = Character.digit(c, 16);
            if (digit < 0) throw new IllegalArgumentException("Not a hex digit: " + c);
            result = (result << 4) | digit;
        }
        return result;
    }

    public int fromHexDigits(CharSequence sequence, int fromIndex, int toIndex) {
        int result = 0;
        for (int i = fromIndex; i < toIndex; i++) {
            char c = sequence.charAt(i);
            int digit = Character.digit(c, 16);
            if (digit < 0) throw new IllegalArgumentException("Not a hex digit: " + c);
            result = (result << 4) | digit;
        }
        return result;
    }

    public String toHexDigits(byte value) {
        char[] digits = uppercase ? UPPER_DIGITS : LOWER_DIGITS;
        return new String(new char[]{
            digits[(value >> 4) & 0xF],
            digits[value & 0xF]
        });
    }

    public String toHexDigits(long value, int digits) {
        char[] d = uppercase ? UPPER_DIGITS : LOWER_DIGITS;
        StringBuilder sb = new StringBuilder(digits);
        for (int i = digits - 1; i >= 0; i--) {
            sb.append(d[(int)((value >> (i * 4)) & 0xF)]);
        }
        return sb.toString();
    }

    public String toHexDigits(long value) {
        return toHexDigits(value, 16);
    }

    public String toHexDigits(char value) {
        return toHexDigits((short) value, 4);
    }

    public String toHexDigits(short value) {
        return toHexDigits(value, 4);
    }

    public String toHexDigits(int value) {
        return toHexDigits(value, 8);
    }

    public char toHighHexDigit(byte value) {
        char[] d = uppercase ? UPPER_DIGITS : LOWER_DIGITS;
        return d[(value >> 4) & 0xF];
    }

    public char toLowHexDigit(byte value) {
        char[] d = uppercase ? UPPER_DIGITS : LOWER_DIGITS;
        return d[value & 0xF];
    }

    public <A extends Appendable> A toHexDigits(A out, byte value) {
        try {
            out.append(toHighHexDigit(value));
            out.append(toLowHexDigit(value));
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }


    public boolean isValidHexDigit(char ch) {
        return isHexDigit(ch);
    }

    public byte[] parseHex(CharSequence source) {
        return parseHex(source, 0, source.length());
    }

    public byte[] parseHex(CharSequence source, int fromIndex, int toIndex) {
        int len = toIndex - fromIndex;
        if (len % 2 != 0) throw new IllegalArgumentException("Odd number of hex digits");
        byte[] result = new byte[len / 2];
        for (int i = 0; i < result.length; i++) {
            int hi = Character.digit(source.charAt(fromIndex + i * 2), 16);
            int lo = Character.digit(source.charAt(fromIndex + i * 2 + 1), 16);
            if (hi < 0 || lo < 0) throw new IllegalArgumentException("Not a hex digit");
            result[i] = (byte) ((hi << 4) | lo);
        }
        return result;
    }

    public byte[] parseHex(char[] source) {
        return parseHex(new String(source));
    }

}
