package org.joml;

public interface Matrix3x2fc {
    float m00(); float m01();
    float m10(); float m11();
    float m20(); float m21();

    default Matrix3x2f invert(Matrix3x2f dest) { return dest; }
    default Matrix3x2f scale(float x, float y, Matrix3x2f dest) { return dest; }
    default Vector2f transformPosition(float x, float y, Vector2f dest) { return dest; }
    default Vector2f transformPosition(Vector2fc v, Vector2f dest) { return transformPosition(v.x(), v.y(), dest); }
    default Matrix3x2f get(Matrix3x2f dest) { return dest; }
    default float determinant() { return 1.0f; }
}
