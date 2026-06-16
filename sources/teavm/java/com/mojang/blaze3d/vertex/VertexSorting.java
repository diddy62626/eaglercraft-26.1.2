package com.mojang.blaze3d.vertex;

public interface VertexSorting {
    VertexSorting ORTHOGRAPHIC = new VertexSorting() {};
    VertexSorting PERSPECTIVE = new VertexSorting() {};

    default void sort(float[] vertices, int vertexCount) {}
    default float[] getDepth(float[] vertices, int vertexCount) { return new float[vertexCount]; }

    static VertexSorting byDistance(float x, float y, float z) { return PERSPECTIVE; }
    default int[] sort(CompactVectorArray array) { return new int[0]; }
}
