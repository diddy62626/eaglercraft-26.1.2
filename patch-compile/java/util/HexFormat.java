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
}
