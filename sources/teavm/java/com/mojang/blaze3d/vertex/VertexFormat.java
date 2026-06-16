package com.mojang.blaze3d.vertex;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

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

        public int connectedPrimitives(int vertexCount) {
            switch (this) {
                case LINES: return vertexCount / 2;
                case LINE_STRIP: return Math.max(0, vertexCount - 1);
                case DEBUG_LINES: return vertexCount / 2;
                case DEBUG_LINE_STRIP: return Math.max(0, vertexCount - 1);
                case TRIANGLES: return vertexCount / 3;
                case TRIANGLE_STRIP: return Math.max(0, vertexCount - 2);
                case TRIANGLE_FAN: return Math.max(0, vertexCount - 2);
                case QUADS: return vertexCount / 4;
                default: return 0;
            }
        }
    }

    public enum IndexType {
        SHORT(2), INT(4);

        public final int bytes;
        IndexType(int bytes) { this.bytes = bytes; }

        public static IndexType least(int vertexCount) {
            return vertexCount < 65536 ? SHORT : INT;
        }
    }

    public static Builder builder() { return new Builder(); }

    public int getVertexSize() { return 32; }
    public boolean contains(VertexFormatElement element) { return false; }
    public String getElementName(VertexFormatElement element) { return ""; }
    public int getElementsMask() { return 0; }
    public int getOffset(VertexFormatElement element) { return 0; }
    public Stream<VertexFormatElement> elements() { return Stream.empty(); }

    public com.mojang.blaze3d.buffers.GpuBuffer uploadImmediateVertexBuffer(ByteBuffer data) { return new com.mojang.blaze3d.buffers.GpuBuffer() {}; }
    public com.mojang.blaze3d.buffers.GpuBuffer uploadImmediateIndexBuffer(ByteBuffer data) { return new com.mojang.blaze3d.buffers.GpuBuffer() {}; }

    public static class Builder {
        public Builder add(String name, VertexFormatElement element) { return this; }
        public Builder padding(int bytes) { return this; }
        public VertexFormat build() { return new VertexFormat(); }
    }
}
