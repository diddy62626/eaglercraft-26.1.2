package com.ibm.icu.text;

public final class Bidi {
    public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = 0;
    public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = 1;
    public static final int DIRECTION_LEFT_TO_RIGHT = 2;
    public static final int DIRECTION_RIGHT_TO_LEFT = 3;

    public Bidi() {}
    public Bidi(String paragraph, int flags) {}
    public Bidi(java.text.AttributedCharacterIterator paragraph) {}

    public static Bidi createLineBidi(Bidi para, int start, int limit) { return new Bidi(); }
    public boolean isLeftToRight() { return true; }
    public boolean isRightToLeft() { return false; }
    public boolean isMixed() { return false; }
    public int getLength() { return 0; }
    public boolean isBaseIsLeftToRight() { return true; }
    public int getBaseDirection() { return DIRECTION_LEFT_TO_RIGHT; }
    public int getVisualLength() { return 0; }
    public byte getLevelAt(int offset) { return 0; }
    public byte[] getLevels() { return new byte[0]; }
    public int[] getReordering(int[] levels) { return new int[0]; }
    public int[] getReordering(java.text.AttributedCharacterIterator paragraph) { return new int[0]; }
    public int[] getReordering(byte[] levels) { return new int[0]; }
    public int getVisualIndex(int logicalIndex) { return logicalIndex; }
    public int getLogicalIndex(int visualIndex) { return visualIndex; }
    public int[] getLogicalMap() { return new int[0]; }
    public int[] getVisualMap() { return new int[0]; }
    public static int getVisualIndex(int logicalIndex, byte[] levels) { return logicalIndex; }
    public static int getLogicalIndex(int visualIndex, byte[] levels) { return visualIndex; }
    public static void reorder(byte[] levels, Object[] array) {}
    public static void reorderVisual(byte[] levels, Object[] array) {}
    public static int countBaseCharacters(java.text.AttributedCharacterIterator paragraph) { return 0; }
    public static Bidi writeReverse(String src, int options) { return new Bidi(); }
    public String writeReordered(int options) { return ""; }

    public void setReorderingMode(int mode) {}
    public int getReorderingMode() { return 0; }
    public void setReorderingOptions(int options) {}
    public int getReorderingOptions() { return 0; }
}
