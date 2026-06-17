package com.mojang.blaze3d;

import com.mojang.blaze3d.vertex.VertexSorting;

public enum ProjectionType {
    ORTHOGRAPHIC, PERSPECTIVE, PERSPECTIVE_REVERSED;

    public void applyLayeringTransform(org.joml.Matrix4f mat, float partialTicks) {}
    public VertexSorting vertexSorting() { return VertexSorting.ORTHOGRAPHIC; }
}
