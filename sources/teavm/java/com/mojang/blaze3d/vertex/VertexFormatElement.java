package com.mojang.blaze3d.vertex;

import java.util.stream.Stream;

public class VertexFormatElement {
    public enum Usage {
        POSITION, NORMAL, COLOR, UV, MATRIX, JOINT, PADDING;
    }

    public enum DataType {
        FLOAT, UNSIGNED_BYTE, BYTE, UNSIGNED_SHORT, SHORT, UNSIGNED_INT, INT;
    }

    public static final VertexFormatElement POSITION = new VertexFormatElement(Usage.POSITION, DataType.FLOAT, 3);
    public static final VertexFormatElement NORMAL = new VertexFormatElement(Usage.NORMAL, DataType.FLOAT, 3);
    public static final VertexFormatElement COLOR = new VertexFormatElement(Usage.COLOR, DataType.UNSIGNED_BYTE, 4);
    public static final VertexFormatElement UV0 = new VertexFormatElement(Usage.UV, DataType.FLOAT, 2);
    public static final VertexFormatElement UV1 = new VertexFormatElement(Usage.UV, DataType.FLOAT, 2);
    public static final VertexFormatElement UV2 = new VertexFormatElement(Usage.UV, DataType.FLOAT, 2);
    public static final VertexFormatElement LINE_WIDTH = new VertexFormatElement(Usage.PADDING, DataType.FLOAT, 1);

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
    public int id() { return ordinal(); }
    public int mask() { return 1 << ordinal(); }
    public int offset() { return 0; }

    private int ordinal() {
        switch (usage) {
            case POSITION: return 0;
            case NORMAL: return 1;
            case COLOR: return 2;
            case UV: return 3;
            case MATRIX: return 4;
            case JOINT: return 5;
            case PADDING: return 6;
            default: return 0;
        }
    }

    public static Stream<VertexFormatElement> elementsFromMask(int mask) {
        return Stream.empty();
    }

}
