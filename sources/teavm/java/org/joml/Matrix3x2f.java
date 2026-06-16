package org.joml;

public class Matrix3x2f implements Matrix3x2fc {
    public float m00, m01;
    public float m10, m11;
    public float m20, m21;

    public Matrix3x2f() { identity(); }

    public Matrix3x2f identity() {
        m00 = 1; m01 = 0;
        m10 = 0; m11 = 1;
        m20 = 0; m21 = 0;
        return this;
    }

    @Override public float m00() { return m00; }
    @Override public float m01() { return m01; }
    @Override public float m10() { return m10; }
    @Override public float m11() { return m11; }
    @Override public float m20() { return m20; }
    @Override public float m21() { return m21; }

    public Matrix3x2f set(Matrix3x2fc m) {
        m00 = m.m00(); m01 = m.m01();
        m10 = m.m10(); m11 = m.m11();
        m20 = m.m20(); m21 = m.m21();
        return this;
    }

    public Matrix3x2f mul(Matrix3x2f right) { return this; }
    public Matrix3x2f mul(Matrix3x2fc right, Matrix3x2f dest) { return dest; }
    public Matrix3x2f translate(float x, float y) { return this; }
    public Matrix3x2f translate(float x, float y, Matrix3x2f dest) { return dest; }
    public Matrix3x2f rotate(float angle) { return this; }
    public Matrix3x2f scale(float x, float y) { return this; }
    public Matrix3x2f scaleLocal(float x, float y) { return this; }

    public Matrix3x2f(Matrix3x2fc mat) {
        m00 = mat.m00(); m01 = mat.m01();
        m10 = mat.m10(); m11 = mat.m11();
        m20 = mat.m20(); m21 = mat.m21();
    }

    public Matrix3x2f invert(Matrix3x2f dest) { return dest; }
    public Matrix3x2f invert() { return this; }
    public float determinant() { return m00 * m11 - m01 * m10; }

    public Matrix3x2f scale(float s) { return scale(s, s); }
    public org.joml.Vector2f transformPosition(org.joml.Vector2f v) { return v; }
}
