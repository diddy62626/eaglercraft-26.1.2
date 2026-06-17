package org.joml;

import java.nio.ByteBuffer;

public class Matrix4f implements Matrix4fc {
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Matrix4f() { identity(); }
    public Matrix4f(Matrix4f mat) { set(mat); }
    public Matrix4f(Matrix4fc mat) { set(mat); }

    public Matrix4f identity() {
        m00 = 1; m01 = 0; m02 = 0; m03 = 0;
        m10 = 0; m11 = 1; m12 = 0; m13 = 0;
        m20 = 0; m21 = 0; m22 = 1; m23 = 0;
        m30 = 0; m31 = 0; m32 = 0; m33 = 1;
        return this;
    }

    public Matrix4f set(Matrix4f mat) {
        m00 = mat.m00; m01 = mat.m01; m02 = mat.m02; m03 = mat.m03;
        m10 = mat.m10; m11 = mat.m11; m12 = mat.m12; m13 = mat.m13;
        m20 = mat.m20; m21 = mat.m21; m22 = mat.m22; m23 = mat.m23;
        m30 = mat.m30; m31 = mat.m31; m32 = mat.m32; m33 = mat.m33;
        return this;
    }

    public Matrix4f set(Matrix4fc mat) {
        m00 = mat.m00(); m01 = mat.m01(); m02 = mat.m02(); m03 = mat.m03();
        m10 = mat.m10(); m11 = mat.m11(); m12 = mat.m12(); m13 = mat.m13();
        m20 = mat.m20(); m21 = mat.m21(); m22 = mat.m22(); m23 = mat.m23();
        m30 = mat.m30(); m31 = mat.m31(); m32 = mat.m32(); m33 = mat.m33();
        return this;
    }

    public Matrix4f set(float[] m) {
        m00 = m[0];  m01 = m[1];  m02 = m[2];  m03 = m[3];
        m10 = m[4];  m11 = m[5];  m12 = m[6];  m13 = m[7];
        m20 = m[8];  m21 = m[9];  m22 = m[10]; m23 = m[11];
        m30 = m[12]; m31 = m[13]; m32 = m[14]; m33 = m[15];
        return this;
    }

    public Matrix4f mul(Matrix4f right) { return mul(right, this); }
    public Matrix4f mul(Matrix4f right, Matrix4f dest) {
        float nm00 = m00 * right.m00 + m10 * right.m01 + m20 * right.m02 + m30 * right.m03;
        float nm01 = m01 * right.m00 + m11 * right.m01 + m21 * right.m02 + m31 * right.m03;
        float nm02 = m02 * right.m00 + m12 * right.m01 + m22 * right.m02 + m32 * right.m03;
        float nm03 = m03 * right.m00 + m13 * right.m01 + m23 * right.m02 + m33 * right.m03;
        float nm10 = m00 * right.m10 + m10 * right.m11 + m20 * right.m12 + m30 * right.m13;
        float nm11 = m01 * right.m10 + m11 * right.m11 + m21 * right.m12 + m31 * right.m13;
        float nm12 = m02 * right.m10 + m12 * right.m11 + m22 * right.m12 + m32 * right.m13;
        float nm13 = m03 * right.m10 + m13 * right.m11 + m23 * right.m12 + m33 * right.m13;
        float nm20 = m00 * right.m20 + m10 * right.m21 + m20 * right.m22 + m30 * right.m23;
        float nm21 = m01 * right.m20 + m11 * right.m21 + m21 * right.m22 + m31 * right.m23;
        float nm22 = m02 * right.m20 + m12 * right.m21 + m22 * right.m22 + m32 * right.m23;
        float nm23 = m03 * right.m20 + m13 * right.m21 + m23 * right.m22 + m33 * right.m23;
        float nm30 = m00 * right.m30 + m10 * right.m31 + m20 * right.m32 + m30 * right.m33;
        float nm31 = m01 * right.m30 + m11 * right.m31 + m21 * right.m32 + m31 * right.m33;
        float nm32 = m02 * right.m30 + m12 * right.m31 + m22 * right.m32 + m32 * right.m33;
        float nm33 = m03 * right.m30 + m13 * right.m31 + m23 * right.m32 + m33 * right.m33;
        dest.m00 = nm00; dest.m01 = nm01; dest.m02 = nm02; dest.m03 = nm03;
        dest.m10 = nm10; dest.m11 = nm11; dest.m12 = nm12; dest.m13 = nm13;
        dest.m20 = nm20; dest.m21 = nm21; dest.m22 = nm22; dest.m23 = nm23;
        dest.m30 = nm30; dest.m31 = nm31; dest.m32 = nm32; dest.m33 = nm33;
        return dest;
    }

    public Matrix4f mul(Matrix4fc right) { return this; }
    public Matrix4f mul(Matrix4fc right, Matrix4f dest) { return dest; }

    @Override public float m00() { return m00; }
    @Override public float m01() { return m01; }
    @Override public float m02() { return m02; }
    @Override public float m03() { return m03; }
    @Override public float m10() { return m10; }
    @Override public float m11() { return m11; }
    @Override public float m12() { return m12; }
    @Override public float m13() { return m13; }
    @Override public float m20() { return m20; }
    @Override public float m21() { return m21; }
    @Override public float m22() { return m22; }
    @Override public float m23() { return m23; }
    @Override public float m30() { return m30; }
    @Override public float m31() { return m31; }
    @Override public float m32() { return m32; }
    @Override public float m33() { return m33; }

    public Matrix4f perspective(float fovy, float aspect, float zNear, float zFar, boolean zZeroToOne) { return this; }
    public Matrix4f perspective(float fovy, float aspect, float zNear, float zFar) { return perspective(fovy, aspect, zNear, zFar, false); }
    public Matrix4f setPerspective(float fovy, float aspect, float zNear, float zFar, boolean zZeroToOne) { return this; }
    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) { return this; }
    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) { return ortho(left, right, bottom, top, zNear, zFar, false); }
    public Matrix4f setOrtho(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) { return this; }
    public Matrix4f ortho2D(float left, float right, float bottom, float top) { return this; }

    public Matrix4f rotation(float angle, Vector3fc axis) { return this; }
    public Matrix4f rotationAround(Quaternionfc q, float ox, float oy, float oz) { return this; }
    public Matrix4f rotationZYX(float z, float y, float x) { return this; }
    public Matrix4f rotationXYZ(float x, float y, float z) { return this; }
    public Matrix4f rotationYXZ(float y, float x, float z) { return this; }
    public Matrix4f rotate(float angle, Vector3fc axis) { return this; }
    public Matrix4f rotate(float angle, float ax, float ay, float az) { return this; }
    public Matrix4f rotateAround(Quaternionfc q, float ox, float oy, float oz) { return this; }
    public Matrix4f rotate(Quaternionfc q) { return this; }
    public Matrix4f rotateLocal(Quaternionfc q) { return this; }
    public Matrix4f rotateX(float angle) { return this; }
    public Matrix4f rotateY(float angle) { return this; }
    public Matrix4f rotateZ(float angle) { return this; }
    public Matrix4f rotateXYZ(float x, float y, float z) { return this; }
    public Matrix4f rotateZYX(float z, float y, float x) { return this; }
    public Matrix4f rotateYXZ(float y, float x, float z) { return this; }
    public Matrix4f rotateAxis(float angle, float x, float y, float z) { return this; }
    public Matrix4f rotateTo(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) { return this; }
    public Matrix4f translate(float x, float y, float z) { return this; }
    public Matrix4f translate(Vector3fc offset) { return this; }
    public Matrix4f translateLocal(Vector3fc offset) { return this; }
    public Matrix4f scale(float s) { return scale(s, s, s); }
    public Matrix4f scale(float x, float y, float z) {
        m00 *= x; m01 *= x; m02 *= x; m03 *= x;
        m10 *= y; m11 *= y; m12 *= y; m13 *= y;
        m20 *= z; m21 *= z; m22 *= z; m23 *= z;
        return this;
    }
    public Matrix4f scale(Vector3fc v) { return scale(v.x(), v.y(), v.z()); }
    public Matrix4f scaleLocal(float x, float y, float z) { return this; }
    public Matrix4f translation(float x, float y, float z) {
        identity();
        m30 = x; m31 = y; m32 = z;
        return this;
    }
    public Matrix4f translation(Vector3fc v) { return translation(v.x(), v.y(), v.z()); }

    public Matrix4f invert() { return this; }
    public Matrix4f invert(Matrix4f dest) { return dest; }
    public float determinant() { return 1.0f; }
    public Matrix4f transpose() { return this; }
    public Matrix4f transpose(Matrix4f dest) { return dest; }
    public Matrix4f normal() { return this; }

    public Vector4f transform(Vector4f v) { return v; }
    public Vector3f transformPosition(Vector3fc v, Vector3f dest) { return dest; }
    public Vector3f transformDirection(Vector3fc v, Vector3f dest) { return dest; }
    public Vector4f transformTranspose(Vector4f v) { return v; }
    public Vector4f transformTranspose(Vector4fc v, Vector4f dest) { return dest; }
    public Matrix4f setColumn(int column, Vector4fc v) { return this; }
    public Matrix4f setRow(int row, Vector4fc v) { return this; }
    public Vector4f getColumn(int column, Vector4f dest) { return dest; }
    public Vector4f getRow(int row, Vector4f dest) { return dest; }
    public float[] get(float[] arr) { return arr; }
    public ByteBuffer get(ByteBuffer buf) { return buf; }
    public ByteBuffer get(int offset, ByteBuffer buf) { return buf; }
    public Matrix4f get(Matrix4f dest) { return dest; }
    public Matrix3f get3x3(Matrix3f dest) { return dest; }
    public Matrix4f set3x3(Matrix3fc mat) { return this; }

    public int properties() { return 0; }
    public Matrix4f determineProperties() { return this; }
    public boolean isAffine() { return true; }
    public boolean isRotation() { return true; }
    public Matrix4f coFactor() { return this; }
    public Matrix4f add(Matrix4fc other) { return this; }
    public Matrix4f sub(Matrix4fc other) { return this; }
    public Matrix4f fma(Matrix4fc other, float t) { return this; }
    public Matrix4f lookAlong(float dx, float dy, float dz, float ux, float uy, float uz) { return this; }
    public Matrix4f lookAlong(Vector3fc dir, Vector3fc up) { return lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z()); }
    public Matrix4f lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) { return this; }
    public Matrix4f lookAt(Vector3fc eye, Vector3fc center, Vector3fc up) { return this; }
    public Matrix4f lookAtLH(Vector3fc eye, Vector3fc center, Vector3fc up) { return this; }
    public Matrix4f setLookAlong(Vector3fc dir, Vector3fc up) { return this; }
    public Matrix4f setLookAt(Vector3fc eye, Vector3fc center, Vector3fc up) { return this; }
    public Matrix4f reflect(float nx, float ny, float nz, float px, float py, float pz) { return this; }
    public Matrix4f reflect(Quaternionfc orientation, Vector3fc point) { return this; }
    public Matrix4f reflect(Vector3fc normal, Vector3fc point) { return this; }
    public Matrix4f billboard(Vector3fc objPos, Vector3fc targetPos, Vector3fc up) { return this; }
    public Matrix4f shadow(Vector3fc light, float lightW) { return this; }
    public Matrix4f shadow(Vector4fc light) { return this; }
    public Matrix4f shadow(Vector3fc light, float lightW, Matrix4f dest) { return dest; }
    public Matrix4f shadow(float a, float b, float c, float d) { return this; }
    public Matrix4f shadow(Vector4fc light, Matrix4f dest) { return dest; }
    public Matrix4f withRotation(float angleX, float angleY, float angleZ) { return this; }
    public Matrix4f withTranslation(Vector3fc translation) { return this; }
    public Matrix4f withScaling(float xyz) { return this; }
    public Matrix4f withScaling(float x, float y, float z) { return this; }
    public Matrix4f withLookAtUP(Vector3fc up) { return this; }
    public Matrix4f pick(float x, float y, float width, float height, int[] viewport) { return this; }
    public Matrix4f frustum(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) { return this; }
    public Matrix4f frustum(float left, float right, float bottom, float top, float zNear, float zFar) { return frustum(left, right, bottom, top, zNear, zFar, false); }
    public Matrix4f setFrustum(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) { return this; }
    public Matrix4f setColumn(int column, float x, float y, float z, float w) { return this; }
    public Matrix4f setFloats(float... m) { return this; }
    public Matrix4f setTransposed(float[] m) { return this; }
    public Matrix4f zero() { m00 = m01 = m02 = m03 = m10 = m11 = m12 = m13 = m20 = m21 = m22 = m23 = m30 = m31 = m32 = m33 = 0; return this; }
    public Matrix4f scaling(float factor) { return identity().scale(factor); }
    public Matrix4f scaling(float x, float y, float z) { return identity().scale(x, y, z); }
    public Matrix4f scaling(Vector3fc factor) { return scaling(factor.x(), factor.y(), factor.z()); }
    public Matrix4f rotation(float angle, float ax, float ay, float az) { return this; }
    public Matrix4f translationRotateScale(float tx, float ty, float tz, float qx, float qy, float qz, float qw, float sx, float sy, float sz) { return this; }
    public Matrix4f translationRotateScale(Vector3fc translation, Quaternionfc rotation, Vector3fc scale) { return this; }
    public Matrix4f translationRotateScaleMul(Vector3fc translation, Quaternionfc rotation, Vector3fc scale, float factor) { return this; }
    public Matrix4f translationRotate(float tx, float ty, float tz, float qx, float qy, float qz, float qw) { return this; }
    public Matrix4f rotationTo(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) { return this; }
    public Matrix4f arcball(float radius, float centerX, float centerY, float centerZ, float angleX, float angleY) { return this; }

    public boolean equals(Matrix4f m, float delta) { return true; }
    public boolean equals(Object o) { return o == this; }
    public int hashCode() { return 0; }
    public String toString() { return "Matrix4f"; }

    public Matrix4f rotationY(float angle) { return this; }
    public Matrix4f setTranslation(float x, float y, float z) { m30 = x; m31 = y; m32 = z; return this; }
    public Matrix4f rotation(Quaternionfc q) { return this; }
    public Matrix4f assume(int properties) { return this; }

    public Matrix4f mul(Matrix3x2fc right) { return this; }

    public Matrix4f set(float m00, float m01, float m02, float m03,
                        float m10, float m11, float m12, float m13,
                        float m20, float m21, float m22, float m23,
                        float m30, float m31, float m32, float m33) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
        return this;
    }
    public Vector3f transformPosition(Vector3f v) { return v; }
}
