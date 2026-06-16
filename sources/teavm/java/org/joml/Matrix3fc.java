package org.joml;

/**
 * EaglerCraft stub for org.joml.Matrix3fc interface.
 */
public interface Matrix3fc {
    float m00();
    float m01();
    float m02();
    float m10();
    float m11();
    float m12();
    float m20();
    float m21();
    float m22();

    default Vector3f getRow(int row, Vector3f dest) { return dest; }
    default Vector3f getColumn(int column, Vector3f dest) { return dest; }
}
