package java.lang;

public final class Character implements java.io.Serializable, Comparable<Character> {
    public static final int MIN_RADIX = 2;
    public static final int MAX_RADIX = 36;
    public static final char MIN_VALUE = '\u0000';
    public static final char MAX_VALUE = '\uFFFF';
    public static final Class<Character> TYPE = char.class;
    public static final byte UNASSIGNED = 0;
    public static final byte UPPERCASE_LETTER = 1;
    public static final byte LOWERCASE_LETTER = 2;
    public static final byte TITLECASE_LETTER = 3;
    public static final byte MODIFIER_LETTER = 4;
    public static final byte OTHER_LETTER = 5;
    public static final byte DECIMAL_DIGIT_NUMBER = 9;
    public static final byte SPACE_SEPARATOR = 12;
    public static final byte LINE_SEPARATOR = 13;
    public static final byte PARAGRAPH_SEPARATOR = 14;

    private final char value;

    public Character(char value) { this.value = value; }

    public static Character valueOf(char c) { return new Character(c); }
    public char charValue() { return value; }

    public static int digit(char ch, int radix) {
        return java.lang.Character.digit(ch, radix);
    }

    public static int digit(int codePoint, int radix) {
        return java.lang.Character.digit(codePoint, radix);
    }

    public static char forDigit(int digit, int radix) {
        return java.lang.Character.forDigit(digit, radix);
    }

    public static boolean isDigit(char ch) { return java.lang.Character.isDigit(ch); }
    public static boolean isDigit(int codePoint) { return java.lang.Character.isDigit(codePoint); }
    public static boolean isLetter(char ch) { return java.lang.Character.isLetter(ch); }
    public static boolean isLetter(int codePoint) { return java.lang.Character.isLetter(codePoint); }
    public static boolean isLetterOrDigit(char ch) { return java.lang.Character.isLetterOrDigit(ch); }
    public static boolean isLetterOrDigit(int codePoint) { return java.lang.Character.isLetterOrDigit(codePoint); }
    public static boolean isWhitespace(char ch) { return java.lang.Character.isWhitespace(ch); }
    public static boolean isWhitespace(int codePoint) { return java.lang.Character.isWhitespace(codePoint); }
    public static boolean isUpperCase(char ch) { return java.lang.Character.isUpperCase(ch); }
    public static boolean isUpperCase(int codePoint) { return java.lang.Character.isUpperCase(codePoint); }
    public static boolean isLowerCase(char ch) { return java.lang.Character.isLowerCase(ch); }
    public static boolean isLowerCase(int codePoint) { return java.lang.Character.isLowerCase(codePoint); }
    public static char toUpperCase(char ch) { return java.lang.Character.toUpperCase(ch); }
    public static int toUpperCase(int codePoint) { return java.lang.Character.toUpperCase(codePoint); }
    public static char toLowerCase(char ch) { return java.lang.Character.toLowerCase(ch); }
    public static int toLowerCase(int codePoint) { return java.lang.Character.toLowerCase(codePoint); }
    public static boolean isAlphabetic(int codePoint) { return java.lang.Character.isAlphabetic(codePoint); }
    public static boolean isISOControl(char ch) { return java.lang.Character.isISOControl(ch); }
    public static boolean isISOControl(int codePoint) { return java.lang.Character.isISOControl(codePoint); }
    public static boolean isTitleCase(char ch) { return java.lang.Character.isTitleCase(ch); }
    public static boolean isTitleCase(int codePoint) { return java.lang.Character.isTitleCase(codePoint); }
    public static boolean isJavaIdentifierStart(char ch) { return java.lang.Character.isJavaIdentifierStart(ch); }
    public static boolean isJavaIdentifierStart(int codePoint) { return java.lang.Character.isJavaIdentifierStart(codePoint); }
    public static boolean isJavaIdentifierPart(char ch) { return java.lang.Character.isJavaIdentifierPart(ch); }
    public static boolean isJavaIdentifierPart(int codePoint) { return java.lang.Character.isJavaIdentifierPart(codePoint); }
    public static boolean isUnicodeIdentifierStart(char ch) { return java.lang.Character.isUnicodeIdentifierStart(ch); }
    public static boolean isUnicodeIdentifierStart(int codePoint) { return java.lang.Character.isUnicodeIdentifierStart(codePoint); }
    public static boolean isUnicodeIdentifierPart(char ch) { return java.lang.Character.isUnicodeIdentifierPart(ch); }
    public static boolean isUnicodeIdentifierPart(int codePoint) { return java.lang.Character.isUnicodeIdentifierPart(codePoint); }
    public static boolean isIdentifierIgnorable(char ch) { return java.lang.Character.isIdentifierIgnorable(ch); }
    public static boolean isIdentifierIgnorable(int codePoint) { return java.lang.Character.isIdentifierIgnorable(codePoint); }
    public static boolean isMirrored(char ch) { return java.lang.Character.isMirrored(ch); }
    public static boolean isMirrored(int codePoint) { return java.lang.Character.isMirrored(codePoint); }
    public static boolean isSupplementaryCodePoint(int codePoint) { return java.lang.Character.isSupplementaryCodePoint(codePoint); }
    public static boolean isHighSurrogate(char ch) { return java.lang.Character.isHighSurrogate(ch); }
    public static boolean isLowSurrogate(char ch) { return java.lang.Character.isLowSurrogate(ch); }
    public static boolean isSurrogate(char ch) { return java.lang.Character.isSurrogate(ch); }
    public static boolean isSurrogatePair(char high, char low) { return java.lang.Character.isSurrogatePair(high, low); }
    public static char highSurrogate(int codePoint) { return java.lang.Character.highSurrogate(codePoint); }
    public static char lowSurrogate(int codePoint) { return java.lang.Character.lowSurrogate(codePoint); }
    public static int toCodePoint(char high, char low) { return java.lang.Character.toCodePoint(high, low); }
    public static int charCount(int codePoint) { return java.lang.Character.charCount(codePoint); }
    public static int codePointAt(char[] a, int index) { return java.lang.Character.codePointAt(a, index); }
    public static int codePointAt(char[] a, int index, int limit) { return java.lang.Character.codePointAt(a, index, limit); }
    public static int codePointAt(CharSequence seq, int index) { return java.lang.Character.codePointAt(seq, index); }
    public static int codePointBefore(char[] a, int index) { return java.lang.Character.codePointBefore(a, index); }
    public static int codePointBefore(char[] a, int index, int start) { return java.lang.Character.codePointBefore(a, index, start); }
    public static int codePointBefore(CharSequence seq, int index) { return java.lang.Character.codePointBefore(seq, index); }
    public static int codePointCount(char[] a, int offset, int count) { return java.lang.Character.codePointCount(a, offset, count); }
    public static int codePointCount(CharSequence seq, int beginIndex, int endIndex) { return java.lang.Character.codePointCount(seq, beginIndex, endIndex); }
    public static int offsetByCodePoints(char[] a, int start, int count, int index, int codePointOffset) { return java.lang.Character.offsetByCodePoints(a, start, count, index, codePointOffset); }
    public static int offsetByCodePoints(CharSequence seq, int index, int codePointOffset) { return java.lang.Character.offsetByCodePoints(seq, index, codePointOffset); }
    public static char[] toChars(int codePoint) { return java.lang.Character.toChars(codePoint); }
    public static int toChars(int codePoint, char[] dst, int dstIndex) { return java.lang.Character.toChars(codePoint, dst, dstIndex); }
    public static int getType(char ch) { return java.lang.Character.getType(ch); }
    public static int getType(int codePoint) { return java.lang.Character.getType(codePoint); }
    public static int getNumericValue(char ch) { return java.lang.Character.getNumericValue(ch); }
    public static int getNumericValue(int codePoint) { return java.lang.Character.getNumericValue(codePoint); }
    public static String getName(int codePoint) { return java.lang.Character.getName(codePoint); }

    public static int codePointOf(String name) {
        if (name == null) throw new IllegalArgumentException("name is null");
        // Try common names
        switch (name.toUpperCase()) {
            case "NULL": return 0;
            case "BELL": return 7;
            case "TAB": return 9;
            case "NEWLINE":
            case "LINE FEED":
            case "LINE FEED (LF)": return 10;
            case "CARRIAGE RETURN":
            case "CR": return 13;
            case "SPACE": return 32;
            case "EXCLAMATION MARK": return 33;
            case "QUOTATION MARK": return 34;
            case "NUMBER SIGN": return 35;
            case "DOLLAR SIGN": return 36;
            case "PERCENT SIGN": return 37;
            case "AMPERSAND": return 38;
            case "APOSTROPHE": return 39;
            case "LEFT PARENTHESIS": return 40;
            case "RIGHT PARENTHESIS": return 41;
            case "ASTERISK": return 42;
            case "PLUS SIGN": return 43;
            case "COMMA": return 44;
            case "HYPHEN-MINUS": return 45;
            case "FULL STOP": return 46;
            case "SOLIDUS": return 47;
            default:
                // Try single-char names
                if (name.length() == 1) return name.charAt(0);
                throw new IllegalArgumentException("Unknown character name: " + name);
        }
    }

    public static int codePointAt(String s, int index) { return codePointAt((CharSequence) s, index); }
    public static int codePointBefore(String s, int index) { return codePointBefore((CharSequence) s, index); }

    public static int compare(char x, char y) { return x - y; }

    public int compareTo(Character another) { return compare(value, another.value); }

    public boolean equals(Object obj) {
        if (obj instanceof Character) return value == ((Character) obj).value;
        return false;
    }

    public int hashCode() { return value; }

    public static int hashCode(char value) { return value; }

    public String toString() { return String.valueOf(value); }

    public static String toString(char c) { return String.valueOf(c); }

    public static String toString(int codePoint) {
        if (codePoint < 0 || codePoint > 0x10FFFF) throw new IllegalArgumentException();
        if (codePoint < 0x10000) return String.valueOf((char) codePoint);
        return new String(toChars(codePoint));
    }

}
