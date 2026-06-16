package com.mojang.blaze3d.vertex;

import java.util.stream.Stream;

public class VertexFormatElement {
    public enum Usage {
        POSITION, NORMAL, COLOR, UV, MATRIX, JOINT, PADDING;
    }

    public enum DataType {
        FLOAT, UNSIGNED_BYTE, BYTE, UNSIGNED_SHORT, SHORT, UNSIGNED_INT, INT;
    }

    private final Usage usage;
    private final DataType type;
    private final int count;

    public VertexFormatElement(Usage usage, DataType type, int count) {
        this.usage = usage;
        this.type = type;
        this.count = count;
    }

    public Usage getUsage() { return usage; }
    public DataType getType() { return type; }
    public int getCount() { return count; }
    public int getSize() { return count * 4; }

    public static Stream<VertexFormatElement> elementsFromMask(int mask) {
        return Stream.empty();
    }
}
