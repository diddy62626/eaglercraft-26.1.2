package com.sun.jna;

public final class WString implements CharSequence, Comparable<WString> {
    private final String value;

    public WString(String value) { this.value = value; }
    public WString(char[] chars) { this.value = new String(chars); }

    public String toString() { return value; }
    public int length() { return value.length(); }
    public char charAt(int index) { return value.charAt(index); }
    public CharSequence subSequence(int start, int end) { return value.subSequence(start, end); }
    public int compareTo(WString other) { return value.compareTo(other.value); }
}
