package org.joml;

public class Matrix3f {
    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;

    public Matrix3f() {
        identity();
    }

    public Matrix3f(Matrix3f mat) {
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

    public Matrix3f set(float[] m) {
        m00 = m[0]; m01 = m[1]; m02 = m[2];
        m10 = m[3]; m11 = m[4]; m12 = m[5];
        m20 = m[6]; m21 = m[7]; m22 = m[8];
        return this;
    }

    public Matrix3f mul(Matrix3f right) {
        return mul(right, this);
    }

    public Matrix3f mul(Matrix3f right, Matrix3f dest) {
        float nm00 = m00 * right.m00 + m10 * right.m01 + m20 * right.m02;
        float nm01 = m01 * right.m00 + m11 * right.m01 + m21 * right.m02;
        float nm02 = m02 * right.m00 + m12 * right.m01 + m22 * right.m02;
        float nm10 = m00 * right.m10 + m10 * right.m11 + m20 * right.m12;
        float nm11 = m01 * right.m10 + m11 * right.m11 + m21 * right.m12;
        float nm12 = m02 * right.m10 + m12 * right.m11 + m22 * right.m12;
        float nm20 = m00 * right.m20 + m10 * right.m21 + m20 * right.m22;
        float nm21 = m01 * right.m20 + m11 * right.m21 + m21 * right.m22;
        float nm22 = m02 * right.m20 + m12 * right.m21 + m22 * right.m22;
        dest.m00 = nm00; dest.m01 = nm01; dest.m02 = nm02;
        dest.m10 = nm10; dest.m11 = nm11; dest.m12 = nm12;
        dest.m20 = nm20; dest.m21 = nm21; dest.m22 = nm22;
        return dest;
    }

    public Matrix3f rotate(float angle, float x, float y, float z) {
        return rotate(angle, x, y, z, this);
    }

    public Matrix3f rotate(float angle, float x, float y, float z, Matrix3f dest) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float omc = 1.0f - c;
        float xy = x * y, yz = y * z, xz = x * z;
        float xs = x * s, ys = y * s, zs = z * s;
        float r00 = x * x * omc + c;
        float r01 = xy * omc + zs;
        float r02 = xz * omc - ys;
        float r10 = xy * omc - zs;
        float r11 = y * y * omc + c;
        float r12 = yz * omc + xs;
        float r20 = xz * omc + ys;
        float r21 = yz * omc - xs;
        float r22 = z * z * omc + c;
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

    public Matrix3f invert() {
        return invert(this);
    }

    public Matrix3f invert(Matrix3f dest) {
        float a00 = m00, a01 = m01, a02 = m02;
        float a10 = m10, a11 = m11, a12 = m12;
        float a20 = m20, a21 = m21, a22 = m22;
        float det = a00 * (a11 * a22 - a12 * a21)
                  - a01 * (a10 * a22 - a12 * a20)
                  + a02 * (a10 * a21 - a11 * a20);
        if (det == 0) return this;
        float invDet = 1.0f / det;
        dest.m00 = (a11 * a22 - a12 * a21) * invDet;
        dest.m01 = (a02 * a21 - a01 * a22) * invDet;
        dest.m02 = (a01 * a12 - a02 * a11) * invDet;
        dest.m10 = (a12 * a20 - a10 * a22) * invDet;
        dest.m11 = (a00 * a22 - a02 * a20) * invDet;
        dest.m12 = (a02 * a10 - a00 * a12) * invDet;
        dest.m20 = (a10 * a21 - a11 * a20) * invDet;
        dest.m21 = (a01 * a20 - a00 * a21) * invDet;
        dest.m22 = (a00 * a11 - a01 * a10) * invDet;
        return dest;
    }

    public Matrix3f transpose() {
        return transpose(this);
    }

    public Matrix3f transpose(Matrix3f dest) {
        dest.m00 = m00; dest.m01 = m10; dest.m02 = m20;
        dest.m10 = m01; dest.m11 = m11; dest.m12 = m21;
        dest.m20 = m02; dest.m21 = m12; dest.m22 = m22;
        return dest;
    }

    public float determinant() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public float[] get(float[] arr) {
        if (arr == null || arr.length < 9) arr = new float[9];
        arr[0] = m00; arr[1] = m01; arr[2] = m02;
        arr[3] = m10; arr[4] = m11; arr[5] = m12;
        arr[6] = m20; arr[7] = m21; arr[8] = m22;
        return arr;
    }

    public Vector3f transform(Vector3f v) {
        return transform(v, v);
    }

    public Vector3f transform(Vector3f v, Vector3f dest) {
        float rx = m00 * v.x + m10 * v.y + m20 * v.z;
        float ry = m01 * v.x + m11 * v.y + m21 * v.z;
        float rz = m02 * v.x + m12 * v.y + m22 * v.z;
        dest.x = rx; dest.y = ry; dest.z = rz;
        return dest;
    }

    public Matrix3f zero() {
        m00 = 0; m01 = 0; m02 = 0;
        m10 = 0; m11 = 0; m12 = 0;
        m20 = 0; m21 = 0; m22 = 0;
        return this;
    }

    public Matrix3f scale(float x, float y, float z) {
        m00 *= x; m01 *= x; m02 *= x;
        m10 *= y; m11 *= y; m12 *= y;
        m20 *= z; m21 *= z; m22 *= z;
        return this;
    }

    public Matrix3f scaling(float x, float y, float z) {
        identity();
        m00 = x; m11 = y; m22 = z;
        return this;
    }

    public Matrix3f rotation(float angle, float x, float y, float z) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float omc = 1.0f - c;
        m00 = x * x * omc + c;
        m01 = x * y * omc + z * s;
        m02 = x * z * omc - y * s;
        m10 = y * x * omc - z * s;
        m11 = y * y * omc + c;
        m12 = y * z * omc + x * s;
        m20 = z * x * omc + y * s;
        m21 = z * y * omc - x * s;
        m22 = z * z * omc + c;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Matrix3f m = (Matrix3f) obj;
        return Float.compare(m.m00, m00) == 0 && Float.compare(m.m01, m01) == 0 && Float.compare(m.m02, m02) == 0 &&
               Float.compare(m.m10, m10) == 0 && Float.compare(m.m11, m11) == 0 && Float.compare(m.m12, m12) == 0 &&
               Float.compare(m.m20, m20) == 0 && Float.compare(m.m21, m21) == 0 && Float.compare(m.m22, m22) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(m00);
        result = 31 * result + Float.floatToIntBits(m01);
        result = 31 * result + Float.floatToIntBits(m02);
        result = 31 * result + Float.floatToIntBits(m10);
        result = 31 * result + Float.floatToIntBits(m11);
        result = 31 * result + Float.floatToIntBits(m12);
        result = 31 * result + Float.floatToIntBits(m20);
        result = 31 * result + Float.floatToIntBits(m21);
        result = 31 * result + Float.floatToIntBits(m22);
        return result;
    }
}
