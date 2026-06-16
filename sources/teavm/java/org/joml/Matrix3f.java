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
    @Override public Vector3f get(Vector3f dest) { return dest; }
}
