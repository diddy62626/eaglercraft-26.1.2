package com.mojang.blaze3d.vertex;

public class VertexFormat {
    public enum Mode {
        LINES, LINE_STRIP, DEBUG_LINES, DEBUG_LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN, QUADS;

        public int indexCount(int vertexCount) {
            switch (this) {
                case LINES: return vertexCount * 2;
                case LINE_STRIP: return Math.max(0, vertexCount - 1) * 2;
                case DEBUG_LINES: return vertexCount * 2;
                case DEBUG_LINE_STRIP: return Math.max(0, vertexCount - 1) * 2;
                case TRIANGLES: return vertexCount;
                case TRIANGLE_STRIP: return Math.max(0, vertexCount - 2) * 3;
                case TRIANGLE_FAN: return Math.max(0, vertexCount - 2) * 3;
                case QUADS: return vertexCount * 6 / 4;
                default: return 0;
            }
        }
    }

    public static Builder builder() { return new Builder(); }

    public int getVertexSize() { return 32; }
    public boolean contains(VertexFormatElement element) { return false; }
    public String getElementName(VertexFormatElement element) { return ""; }

    public static class Builder {
        public Builder add(String name, VertexFormatElement element) { return this; }
        public VertexFormat build() { return new VertexFormat(); }
    }
}
