package org.joml;

public interface Matrix3fc {
    float m00(); float m01(); float m02();
    float m10(); float m11(); float m12();
    float m20(); float m21(); float m22();

    default Matrix3f get(Matrix3f dest) { return dest; }
    default float determinant() { return 1.0f; }
    default Matrix3f invert(Matrix3f dest) { return dest; }
    default Matrix3f transpose(Matrix3f dest) { return dest; }
    default Matrix3f mul(Matrix3fc right, Matrix3f dest) { return dest; }
    default Vector3f transform(Vector3f v) { return v; }
    default Vector3f transform(Vector3fc v, Vector3f dest) { return dest; }
}
