package com.ibm.icu.text;

public final class ArabicShaping {
    public static final int LENGTH_GROWS_SHAPING = 1;
    public static final int LENGTH_NEUTRAL_CONTEXT = 0;
    public static final int LETTERS_NOOP = 0;
    public static final int LETTERS_SHAPE = 1;
    public static final int LETTERS_UNSHAPE = 2;
    public static final int LETTERS_SHAPE_TASHKEEL_ISOLATED = 3;
    public static final int DIGITS_NOOP = 0;
    public static final int DIGITS_EN2AN = 1;
    public static final int DIGITS_AN2EN = 2;
    public static final int DIGITS_ALEN2AN_INIT_LEN = 3;
    public static final int DIGITS_ALEN2AN_INIT_AL = 4;
    public static final int DIGITS_RESERVED = 5;
    public static final int DEFAULT_DIGITS_TYPE = 0;
    public static final int DIGIT_TYPE_AN = 0;
    public static final int DIGIT_TYPE_AN_EXTENDED = 1;
    public static final int TASHKEEL_BEGIN = 1;
    public static final int TASHKEEL_END = 2;
    public static final int TASHKEEL_REPLACE_BY_TATWEEL = 3;
    public static final int TASHKEEL_RESPECT = 4;

    public ArabicShaping(int options) {}
    public int shape(String source, StringBuffer dest, int start, int count) { return 0; }
    public String shape(String source, int options) { return source; }
    public int getOptions() { return 0; }
    public boolean isLamAlefSpecial(int codepoint) { return false; }
    public String toString() { return "ArabicShaping"; }

    public String shape(String source) { return source; }
}
