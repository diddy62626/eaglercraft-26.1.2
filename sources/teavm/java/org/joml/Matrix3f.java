package org.joml;

/**
 * EaglerCraft stub for org.joml.Matrix3f.
 * Implements Matrix3fc interface so MC code that takes Matrix3fc args works.
 */
public class Matrix3f implements Matrix3fc {
    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;

    public Matrix3f() {
        identity();
    }

    public Matrix3f(Matrix3f mat) {
        set(mat);
    }

    public Matrix3f(Matrix3fc mat) {
        set(mat);
    }

    /**
     * MC 26.1.2: Constructs a Matrix3f from the upper-left 3x3 of a Matrix4fc.
     */
    public Matrix3f(Matrix4fc mat) {
        m00 = mat.m00(); m01 = mat.m01(); m02 = mat.m02();
        m10 = mat.m10(); m11 = mat.m11(); m12 = mat.m12();
        m20 = mat.m20(); m21 = mat.m21(); m22 = mat.m22();
    }

    public Matrix3f identity() {
        m00 = 1; m01 = 0; m02 = 0;
        m10 = 0; m11 = 1; m12 = 0;
        m20 = 0; m21 = 0; m22 = 1;
        return this;
    }

    public Matrix3f set(Matrix3f mat) {
        m00 = mat.m00; m01 = mat.m01; m02 = mat.m02;
        m10 = mat.m10; m11 = mat.m11; m12 = mat.m12;
        m20 = mat.m20; m21 = mat.m21; m22 = mat.m22;
        return this;
    }

    public Matrix3f set(Matrix3fc mat) {
        if (mat instanceof Matrix3f) return set((Matrix3f) mat);
        m00 = mat.m00(); m01 = mat.m01(); m02 = mat.m02();
        m10 = mat.m10(); m11 = mat.m11(); m12 = mat.m12();
        m20 = mat.m20(); m21 = mat.m21(); m22 = mat.m22();
        return this;
    }

    public Matrix3f set(float[] m) {
        m00 = m[0]; m01 = m[1]; m02 = m[2];
        m10 = m[3]; m11 = m[4]; m12 = m[5];
        m20 = m[6]; m21 = m[7]; m22 = m[8];
        return this;
    }

    /** Set element at (row, col) to value. */
    public Matrix3f set(int row, int col, float value) {
        switch (row * 3 + col) {
            case 0: m00 = value; break;
            case 1: m01 = value; break;
            case 2: m02 = value; break;
            case 3: m10 = value; break;
            case 4: m11 = value; break;
            case 5: m12 = value; break;
            case 6: m20 = value; break;
            case 7: m21 = value; break;
            case 8: m22 = value; break;
            default: throw new IndexOutOfBoundsException();
        }
        return this;
    }

    public Matrix3f mul(Matrix3f right) {
        return mul(right, this);
    }

    public Matrix3f mul(Matrix3fc right) {
        return mul(right, this);
    }

    public Matrix3f mul(Matrix3fc right, Matrix3f dest) {
        float r00, r01, r02, r10, r11, r12, r20, r21, r22;
        if (right instanceof Matrix3f) {
            Matrix3f r = (Matrix3f) right;
            r00 = r.m00; r01 = r.m01; r02 = r.m02;
            r10 = r.m10; r11 = r.m11; r12 = r.m12;
            r20 = r.m20; r21 = r.m21; r22 = r.m22;
        } else {
            r00 = right.m00(); r01 = right.m01(); r02 = right.m02();
            r10 = right.m10(); r11 = right.m11(); r12 = right.m12();
            r20 = right.m20(); r21 = right.m21(); r22 = right.m22();
        }
        float nm00 = m00 * r00 + m10 * r01 + m20 * r02;
        float nm01 = m01 * r00 + m11 * r01 + m21 * r02;
        float nm02 = m02 * r00 + m12 * r01 + m22 * r02;
        float nm10 = m00 * r10 + m10 * r11 + m20 * r12;
        float nm11 = m01 * r10 + m11 * r11 + m21 * r12;
        float nm12 = m02 * r10 + m12 * r11 + m22 * r12;
        float nm20 = m00 * r20 + m10 * r21 + m20 * r22;
        float nm21 = m01 * r20 + m11 * r21 + m21 * r22;
        float nm22 = m02 * r20 + m12 * r21 + m22 * r22;
        dest.m00 = nm00; dest.m01 = nm01; dest.m02 = nm02;
        dest.m10 = nm10; dest.m11 = nm11; dest.m12 = nm12;
        dest.m20 = nm20; dest.m21 = nm21; dest.m22 = nm22;
        return dest;
    }

    public Matrix3f mul(Matrix3f right, Matrix3f dest) {
        return mul((Matrix3fc) right, dest);
    }


    public Matrix3f scaling(float x, float y, float z) {
        m00 = x; m01 = 0; m02 = 0;
        m10 = 0; m11 = y; m12 = 0;
        m20 = 0; m21 = 0; m22 = z;
        return this;
    }

    public Matrix3f scaling(Vector3fc scale) {
        return scaling(scale.x(), scale.y(), scale.z());
    }

    public Matrix3f zero() {
        m00 = 0; m01 = 0; m02 = 0;
        m10 = 0; m11 = 0; m12 = 0;
        m20 = 0; m21 = 0; m22 = 0;
        return this;
    }

    public Matrix3f scaling(float factor) {
        return scaling(factor, factor, factor);
    }

    public Matrix3f rotation(float angle, float x, float y, float z) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float omc = 1.0f - c;
        m00 = c + x * x * omc;
        m11 = c + y * y * omc;
        m22 = c + z * z * omc;
        float tmp1 = x * y * omc;
        float tmp2 = z * s;
        m01 = tmp1 - tmp2;
        m10 = tmp1 + tmp2;
        tmp1 = x * z * omc;
        tmp2 = y * s;
        m02 = tmp1 + tmp2;
        m20 = tmp1 - tmp2;
        tmp1 = y * z * omc;
        tmp2 = x * s;
        m12 = tmp1 - tmp2;
        m21 = tmp1 + tmp2;
        return this;
    }

    public Matrix3f rotationX(float angle) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        m00 = 1; m01 = 0; m02 = 0;
        m10 = 0; m11 = cos; m12 = -sin;
        m20 = 0; m21 = sin; m22 = cos;
        return this;
    }

    public Matrix3f rotationY(float angle) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        m00 = cos; m01 = 0; m02 = sin;
        m10 = 0; m11 = 1; m12 = 0;
        m20 = -sin; m21 = 0; m22 = cos;
        return this;
    }

    public Matrix3f rotationZ(float angle) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        m00 = cos; m01 = -sin; m02 = 0;
        m10 = sin; m11 = cos; m12 = 0;
        m20 = 0; m21 = 0; m22 = 1;
        return this;
    }

    public float determinant() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix3f invert() {
        float s = 1.0f / determinant();
        float nm00 = (m11 * m22 - m12 * m21) * s;
        float nm01 = (m02 * m21 - m01 * m22) * s;
        float nm02 = (m01 * m12 - m02 * m11) * s;
        float nm10 = (m12 * m20 - m10 * m22) * s;
        float nm11 = (m00 * m22 - m02 * m20) * s;
        float nm12 = (m02 * m10 - m00 * m12) * s;
        float nm20 = (m10 * m21 - m11 * m20) * s;
        float nm21 = (m01 * m20 - m00 * m21) * s;
        float nm22 = (m00 * m11 - m01 * m10) * s;
        m00 = nm00; m01 = nm01; m02 = nm02;
        m10 = nm10; m11 = nm11; m12 = nm12;
        m20 = nm20; m21 = nm21; m22 = nm22;
        return this;
    }

    public Matrix3f transpose() {
        float tmp = m01; m01 = m10; m10 = tmp;
        tmp = m02; m02 = m20; m20 = tmp;
        tmp = m12; m12 = m21; m21 = tmp;
        return this;
    }

    public Vector3f getTranslation(Vector3f dest) {
        return dest.set(0, 0, 0);
    }

    public Matrix3f get(Matrix3f dest) {
        return dest.set(this);
    }

    // Matrix3fc interface methods
    @Override public float m00() { return m00; }
    @Override public float m01() { return m01; }
    @Override public float m02() { return m02; }
    @Override public float m10() { return m10; }
    @Override public float m11() { return m11; }
    @Override public float m12() { return m12; }
    @Override public float m20() { return m20; }
    @Override public float m21() { return m21; }
    @Override public float m22() { return m22; }

    public Matrix3f scale(float s) {
        m00 *= s; m01 *= s; m02 *= s;
        m10 *= s; m11 *= s; m12 *= s;
        m20 *= s; m21 *= s; m22 *= s;
        return this;
    }

    public Matrix3f scale(float sx, float sy, float sz) {
        m00 *= sx; m01 *= sy; m02 *= sz;
        m10 *= sx; m11 *= sy; m12 *= sz;
        m20 *= sx; m21 *= sy; m22 *= sz;
        return this;
    }
    public Matrix3f rotate(float angle, float ax, float ay, float az) { return this; }
    public Matrix3f rotate(float angle, Vector3fc axis) { return this; }
    public Matrix3f rotateX(float angle) { return this; }
    public Matrix3f rotateY(float angle) { return this; }
    public Matrix3f rotateZ(float angle) { return this; }
    public Matrix3f rotationXYZ(float x, float y, float z) { return this; }
    public Matrix3f rotationZYX(float z, float y, float x) { return this; }
    public Matrix3f rotationYXZ(float y, float x, float z) { return this; }
    public Vector3f transform(Vector3fc v, Vector3f dest) { return dest; }

    public Vector3f transform(float x, float y, float z, Vector3f dest) { return dest; }
}
