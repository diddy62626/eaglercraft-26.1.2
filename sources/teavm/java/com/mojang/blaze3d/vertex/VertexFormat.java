package com.mojang.blaze3d.vertex;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class VertexFormat {
    public enum Mode {
        LINES(2, vertexCount -> vertexCount / 2),
        LINE_STRIP(1, vertexCount -> Math.max(0, vertexCount - 1)),
        DEBUG_LINES(2, vertexCount -> vertexCount / 2),
        DEBUG_LINE_STRIP(1, vertexCount -> Math.max(0, vertexCount - 1)),
        TRIANGLES(3, vertexCount -> vertexCount / 3),
        TRIANGLE_STRIP(1, vertexCount -> Math.max(0, vertexCount - 2)),
        TRIANGLE_FAN(1, vertexCount -> Math.max(0, vertexCount - 2)),
        QUADS(4, vertexCount -> vertexCount / 4),
        POINTS(1, vertexCount -> vertexCount);

        public final int verticesPerPrimitive;
        public final IntFunction<Integer> connectedPrimitives;

        Mode(int verticesPerPrimitive, IntFunction<Integer> connectedPrimitives) {
            this.verticesPerPrimitive = verticesPerPrimitive;
            this.connectedPrimitives = connectedPrimitives;
        }

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
                case POINTS: return vertexCount;
                default: return 0;
            }
        }

        public int connectedPrimitives(int vertexCount) {
            return connectedPrimitives.apply(vertexCount);
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
    public int[] getOffsetsByElement() { return new int[0]; }

    public com.mojang.blaze3d.buffers.GpuBuffer uploadImmediateVertexBuffer(ByteBuffer data) { return new com.mojang.blaze3d.buffers.GpuBuffer() {}; }
    public com.mojang.blaze3d.buffers.GpuBuffer uploadImmediateIndexBuffer(ByteBuffer data) { return new com.mojang.blaze3d.buffers.GpuBuffer() {}; }

    public static class Builder {
        public Builder add(String name, VertexFormatElement element) { return this; }
        public Builder padding(int bytes) { return this; }
        public VertexFormat build() { return new VertexFormat(); }
    }
}
