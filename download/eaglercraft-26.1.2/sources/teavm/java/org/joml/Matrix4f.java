package org.joml;

public class Matrix4f {
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Matrix4f() {
        identity();
    }

    public Matrix4f(Matrix4f mat) {
        set(mat);
    }

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

    public Matrix4f set(float[] m) {
        m00 = m[0];  m01 = m[1];  m02 = m[2];  m03 = m[3];
        m10 = m[4];  m11 = m[5];  m12 = m[6];  m13 = m[7];
        m20 = m[8];  m21 = m[9];  m22 = m[10]; m23 = m[11];
        m30 = m[12]; m31 = m[13]; m32 = m[14]; m33 = m[15];
        return this;
    }

    public Matrix4f mul(Matrix4f right) {
        return mul(right, this);
    }

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

    public Matrix4f mul(float scalar) {
        m00 *= scalar; m01 *= scalar; m02 *= scalar; m03 *= scalar;
        m10 *= scalar; m11 *= scalar; m12 *= scalar; m13 *= scalar;
        m20 *= scalar; m21 *= scalar; m22 *= scalar; m23 *= scalar;
        m30 *= scalar; m31 *= scalar; m32 *= scalar; m33 *= scalar;
        return this;
    }

    public Matrix4f translate(float x, float y, float z) {
        return translate(x, y, z, this);
    }

    public Matrix4f translate(float x, float y, float z, Matrix4f dest) {
        // dest = this * T(x,y,z)
        dest.m30 = m00 * x + m10 * y + m20 * z + m30;
        dest.m31 = m01 * x + m11 * y + m21 * z + m31;
        dest.m32 = m02 * x + m12 * y + m22 * z + m32;
        dest.m33 = m03 * x + m13 * y + m23 * z + m33;
        if (dest != this) {
            dest.m00 = m00; dest.m01 = m01; dest.m02 = m02; dest.m03 = m03;
            dest.m10 = m10; dest.m11 = m11; dest.m12 = m12; dest.m13 = m13;
            dest.m20 = m20; dest.m21 = m21; dest.m22 = m22; dest.m23 = m23;
        }
        return dest;
    }

    public Matrix4f translate(Vector3f offset) {
        return translate(offset.x, offset.y, offset.z);
    }

    public Matrix4f translate(Vector3f offset, Matrix4f dest) {
        return translate(offset.x, offset.y, offset.z, dest);
    }

    public Matrix4f rotate(float angle, float x, float y, float z) {
        return rotate(angle, x, y, z, this);
    }

    public Matrix4f rotate(float angle, float x, float y, float z, Matrix4f dest) {
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
        float nm03 = m03 * r00 + m13 * r01 + m23 * r02;
        float nm10 = m00 * r10 + m10 * r11 + m20 * r12;
        float nm11 = m01 * r10 + m11 * r11 + m21 * r12;
        float nm12 = m02 * r10 + m12 * r11 + m22 * r12;
        float nm13 = m03 * r10 + m13 * r11 + m23 * r12;
        float nm20 = m00 * r20 + m10 * r21 + m20 * r22;
        float nm21 = m01 * r20 + m11 * r21 + m21 * r22;
        float nm22 = m02 * r20 + m12 * r21 + m22 * r22;
        float nm23 = m03 * r20 + m13 * r21 + m23 * r22;
        dest.m00 = nm00; dest.m01 = nm01; dest.m02 = nm02; dest.m03 = nm03;
        dest.m10 = nm10; dest.m11 = nm11; dest.m12 = nm12; dest.m13 = nm13;
        dest.m20 = nm20; dest.m21 = nm21; dest.m22 = nm22; dest.m23 = nm23;
        if (dest != this) {
            dest.m30 = m30; dest.m31 = m31; dest.m32 = m32; dest.m33 = m33;
        }
        return dest;
    }

    public Matrix4f rotateX(float angle) {
        return rotateX(angle, this);
    }

    public Matrix4f rotateX(float angle, Matrix4f dest) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float nm10 = m10 * c + m20 * s;
        float nm11 = m11 * c + m21 * s;
        float nm12 = m12 * c + m22 * s;
        float nm13 = m13 * c + m23 * s;
        float nm20 = m20 * c - m10 * s;
        float nm21 = m21 * c - m11 * s;
        float nm22 = m22 * c - m12 * s;
        float nm23 = m23 * c - m13 * s;
        dest.m10 = nm10; dest.m11 = nm11; dest.m12 = nm12; dest.m13 = nm13;
        dest.m20 = nm20; dest.m21 = nm21; dest.m22 = nm22; dest.m23 = nm23;
        if (dest != this) {
            dest.m00 = m00; dest.m01 = m01; dest.m02 = m02; dest.m03 = m03;
            dest.m30 = m30; dest.m31 = m31; dest.m32 = m32; dest.m33 = m33;
        }
        return dest;
    }

    public Matrix4f rotateY(float angle) {
        return rotateY(angle, this);
    }

    public Matrix4f rotateY(float angle, Matrix4f dest) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float nm00 = m00 * c - m20 * s;
        float nm01 = m01 * c - m21 * s;
        float nm02 = m02 * c - m22 * s;
        float nm03 = m03 * c - m23 * s;
        float nm20 = m00 * s + m20 * c;
        float nm21 = m01 * s + m21 * c;
        float nm22 = m02 * s + m22 * c;
        float nm23 = m03 * s + m23 * c;
        dest.m00 = nm00; dest.m01 = nm01; dest.m02 = nm02; dest.m03 = nm03;
        dest.m20 = nm20; dest.m21 = nm21; dest.m22 = nm22; dest.m23 = nm23;
        if (dest != this) {
            dest.m10 = m10; dest.m11 = m11; dest.m12 = m12; dest.m13 = m13;
            dest.m30 = m30; dest.m31 = m31; dest.m32 = m32; dest.m33 = m33;
        }
        return dest;
    }

    public Matrix4f rotateZ(float angle) {
        return rotateZ(angle, this);
    }

    public Matrix4f rotateZ(float angle, Matrix4f dest) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float nm00 = m00 * c + m10 * s;
        float nm01 = m01 * c + m11 * s;
        float nm02 = m02 * c + m12 * s;
        float nm03 = m03 * c + m13 * s;
        float nm10 = m10 * c - m00 * s;
        float nm11 = m11 * c - m01 * s;
        float nm12 = m12 * c - m02 * s;
        float nm13 = m13 * c - m03 * s;
        dest.m00 = nm00; dest.m01 = nm01; dest.m02 = nm02; dest.m03 = nm03;
        dest.m10 = nm10; dest.m11 = nm11; dest.m12 = nm12; dest.m13 = nm13;
        if (dest != this) {
            dest.m20 = m20; dest.m21 = m21; dest.m22 = m22; dest.m23 = m23;
            dest.m30 = m30; dest.m31 = m31; dest.m32 = m32; dest.m33 = m33;
        }
        return dest;
    }

    public Matrix4f scale(float x, float y, float z) {
        return scale(x, y, z, this);
    }

    public Matrix4f scale(float x, float y, float z, Matrix4f dest) {
        dest.m00 = m00 * x; dest.m01 = m01 * x; dest.m02 = m02 * x; dest.m03 = m03 * x;
        dest.m10 = m10 * y; dest.m11 = m11 * y; dest.m12 = m12 * y; dest.m13 = m13 * y;
        dest.m20 = m20 * z; dest.m21 = m21 * z; dest.m22 = m22 * z; dest.m23 = m23 * z;
        if (dest != this) {
            dest.m30 = m30; dest.m31 = m31; dest.m32 = m32; dest.m33 = m33;
        }
        return dest;
    }

    public Matrix4f scale(float s) {
        return scale(s, s, s);
    }

    public Matrix4f scale(float s, Matrix4f dest) {
        return scale(s, s, s, dest);
    }

    public Matrix4f scale(Vector3f v) {
        return scale(v.x, v.y, v.z);
    }

    public Matrix4f scale(Vector3f v, Matrix4f dest) {
        return scale(v.x, v.y, v.z, dest);
    }

    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
        return ortho(left, right, bottom, top, zNear, zFar, this);
    }

    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar, Matrix4f dest) {
        float rm00 = 2.0f / (right - left);
        float rm11 = 2.0f / (top - bottom);
        float rm22 = -2.0f / (zFar - zNear);
        float rm30 = -(right + left) / (right - left);
        float rm31 = -(top + bottom) / (top - bottom);
        float rm32 = -(zFar + zNear) / (zFar - zNear);
        dest.m00 = m00 * rm00; dest.m01 = m01 * rm00; dest.m02 = m02 * rm00; dest.m03 = m03 * rm00;
        dest.m10 = m10 * rm11; dest.m11 = m11 * rm11; dest.m12 = m12 * rm11; dest.m13 = m13 * rm11;
        dest.m20 = m20 * rm22; dest.m21 = m21 * rm22; dest.m22 = m22 * rm22; dest.m23 = m23 * rm22;
        dest.m30 = m00 * rm30 + m10 * rm31 + m20 * rm32 + m30;
        dest.m31 = m01 * rm30 + m11 * rm31 + m21 * rm32 + m31;
        dest.m32 = m02 * rm30 + m12 * rm31 + m22 * rm32 + m32;
        dest.m33 = m03 * rm30 + m13 * rm31 + m23 * rm32 + m33;
        return dest;
    }

    public Matrix4f ortho2D(float left, float right, float bottom, float top) {
        return ortho(left, right, bottom, top, -1, 1);
    }

    public Matrix4f perspective(float fovy, float aspect, float zNear, float zFar) {
        return perspective(fovy, aspect, zNear, zFar, this);
    }

    public Matrix4f perspective(float fovy, float aspect, float zNear, float zFar, Matrix4f dest) {
        float h = (float) Math.tan(fovy * 0.5f);
        float rm00 = 1.0f / (h * aspect);
        float rm11 = 1.0f / h;
        float rm22 = -(zFar + zNear) / (zFar - zNear);
        float rm32 = -2.0f * zFar * zNear / (zFar - zNear);
        dest.m00 = m00 * rm00; dest.m01 = m01 * rm00; dest.m02 = m02 * rm00; dest.m03 = m03 * rm00;
        dest.m10 = m10 * rm11; dest.m11 = m11 * rm11; dest.m12 = m12 * rm11; dest.m13 = m13 * rm11;
        dest.m20 = m20 * rm22 + m30; dest.m21 = m21 * rm22 + m31; dest.m22 = m22 * rm22 + m32; dest.m23 = m23 * rm22 + m33;
        dest.m30 = m20 * rm32; dest.m31 = m21 * rm32; dest.m32 = m22 * rm32; dest.m33 = m23 * rm32;
        return dest;
    }

    public Matrix4f invert() {
        return invert(this);
    }

    public Matrix4f invert(Matrix4f dest) {
        float a00 = m00, a01 = m01, a02 = m02, a03 = m03;
        float a10 = m10, a11 = m11, a12 = m12, a13 = m13;
        float a20 = m20, a21 = m21, a22 = m22, a23 = m23;
        float a30 = m30, a31 = m31, a32 = m32, a33 = m33;
        float b00 = a00 * a11 - a01 * a10;
        float b01 = a00 * a12 - a02 * a10;
        float b02 = a00 * a13 - a03 * a10;
        float b03 = a01 * a12 - a02 * a11;
        float b04 = a01 * a13 - a03 * a11;
        float b05 = a02 * a13 - a03 * a12;
        float b06 = a20 * a31 - a21 * a30;
        float b07 = a20 * a32 - a22 * a30;
        float b08 = a20 * a33 - a23 * a30;
        float b09 = a21 * a32 - a22 * a31;
        float b10 = a21 * a33 - a23 * a31;
        float b11 = a22 * a33 - a23 * a32;
        float det = b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06;
        if (det == 0) return this;
        float invDet = 1.0f / det;
        dest.m00 = (a11 * b11 - a12 * b10 + a13 * b09) * invDet;
        dest.m01 = (a02 * b10 - a01 * b11 - a03 * b09) * invDet;
        dest.m02 = (a31 * b05 - a32 * b04 + a33 * b03) * invDet;
        dest.m03 = (a22 * b04 - a21 * b05 - a23 * b03) * invDet;
        dest.m10 = (a12 * b08 - a10 * b11 - a13 * b07) * invDet;
        dest.m11 = (a00 * b11 - a02 * b08 + a03 * b07) * invDet;
        dest.m12 = (a32 * b02 - a30 * b05 - a33 * b01) * invDet;
        dest.m13 = (a20 * b05 - a22 * b02 + a23 * b01) * invDet;
        dest.m20 = (a10 * b10 - a11 * b08 + a13 * b06) * invDet;
        dest.m21 = (a01 * b08 - a00 * b10 - a03 * b06) * invDet;
        dest.m22 = (a30 * b04 - a31 * b02 + a33 * b00) * invDet;
        dest.m23 = (a21 * b02 - a20 * b04 - a23 * b00) * invDet;
        dest.m30 = (a11 * b07 - a10 * b09 - a12 * b06) * invDet;
        dest.m31 = (a00 * b09 - a01 * b07 + a02 * b06) * invDet;
        dest.m32 = (a31 * b01 - a30 * b03 - a32 * b00) * invDet;
        dest.m33 = (a20 * b03 - a21 * b01 + a22 * b00) * invDet;
        return dest;
    }

    public Matrix4f transpose() {
        return transpose(this);
    }

    public Matrix4f transpose(Matrix4f dest) {
        float nm01 = m10, nm02 = m20, nm03 = m30;
        float nm10 = m01, nm12 = m30, nm13 = m31; // fix below
        float nm20 = m02, nm21 = m12, nm23 = m32;
        float nm30 = m03, nm31 = m13, nm32 = m23;
        // Full transpose
        dest.m00 = m00; dest.m01 = m10; dest.m02 = m20; dest.m03 = m30;
        dest.m10 = m01; dest.m11 = m11; dest.m12 = m21; dest.m13 = m31;
        dest.m20 = m02; dest.m21 = m12; dest.m22 = m22; dest.m23 = m32;
        dest.m30 = m03; dest.m31 = m13; dest.m32 = m23; dest.m33 = m33;
        return dest;
    }

    public float[] get(float[] arr) {
        if (arr == null || arr.length < 16) arr = new float[16];
        arr[0] = m00;  arr[1] = m01;  arr[2] = m02;  arr[3] = m03;
        arr[4] = m10;  arr[5] = m11;  arr[6] = m12;  arr[7] = m13;
        arr[8] = m20;  arr[9] = m21;  arr[10] = m22; arr[11] = m23;
        arr[12] = m30; arr[13] = m31; arr[14] = m32; arr[15] = m33;
        return arr;
    }

    public float[] get4x4(float[] arr) {
        return get(arr);
    }

    public Matrix4f get(Matrix4f dest) {
        dest.set(this);
        return dest;
    }

    public Matrix4f zero() {
        m00 = 0; m01 = 0; m02 = 0; m03 = 0;
        m10 = 0; m11 = 0; m12 = 0; m13 = 0;
        m20 = 0; m21 = 0; m22 = 0; m23 = 0;
        m30 = 0; m31 = 0; m32 = 0; m33 = 0;
        return this;
    }

    public float determinant() {
        float b00 = m00 * m11 - m01 * m10;
        float b01 = m00 * m12 - m02 * m10;
        float b02 = m00 * m13 - m03 * m10;
        float b03 = m01 * m12 - m02 * m11;
        float b04 = m01 * m13 - m03 * m11;
        float b05 = m02 * m13 - m03 * m12;
        float b06 = m20 * m31 - m21 * m30;
        float b07 = m20 * m32 - m22 * m30;
        float b08 = m20 * m33 - m23 * m30;
        float b09 = m21 * m32 - m22 * m31;
        float b10 = m21 * m33 - m23 * m31;
        float b11 = m22 * m33 - m23 * m32;
        return b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06;
    }

    public Matrix4f lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        return lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }

    public Matrix4f lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ, Matrix4f dest) {
        float zDirX = eyeX - centerX;
        float zDirY = eyeY - centerY;
        float zDirZ = eyeZ - centerZ;
        float len = (float) Math.sqrt(zDirX * zDirX + zDirY * zDirY + zDirZ * zDirZ);
        if (len > 0) { zDirX /= len; zDirY /= len; zDirZ /= len; }
        float xDirX = upY * zDirZ - upZ * zDirY;
        float xDirY = upZ * zDirX - upX * zDirZ;
        float xDirZ = upX * zDirY - upY * zDirX;
        len = (float) Math.sqrt(xDirX * xDirX + xDirY * xDirY + xDirZ * xDirZ);
        if (len > 0) { xDirX /= len; xDirY /= len; xDirZ /= len; }
        float yDirX = zDirY * xDirZ - zDirZ * xDirY;
        float yDirY = zDirZ * xDirX - zDirX * xDirZ;
        float yDirZ = zDirX * xDirY - zDirY * xDirX;
        float rm00 = xDirX, rm01 = yDirX, rm02 = zDirX;
        float rm10 = xDirY, rm11 = yDirY, rm12 = zDirY;
        float rm20 = xDirZ, rm21 = yDirZ, rm22 = zDirZ;
        float rm30 = -(xDirX * eyeX + xDirY * eyeY + xDirZ * eyeZ);
        float rm31 = -(yDirX * eyeX + yDirY * eyeY + yDirZ * eyeZ);
        float rm32 = -(zDirX * eyeX + zDirY * eyeY + zDirZ * eyeZ);
        dest.m00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
        dest.m01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
        dest.m02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
        dest.m03 = m03 * rm00 + m13 * rm01 + m23 * rm02;
        dest.m10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
        dest.m11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
        dest.m12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
        dest.m13 = m03 * rm10 + m13 * rm11 + m23 * rm12;
        dest.m20 = m00 * rm20 + m10 * rm21 + m20 * rm22;
        dest.m21 = m01 * rm20 + m11 * rm21 + m21 * rm22;
        dest.m22 = m02 * rm20 + m12 * rm21 + m22 * rm22;
        dest.m23 = m03 * rm20 + m13 * rm21 + m23 * rm22;
        dest.m30 = m00 * rm30 + m10 * rm31 + m20 * rm32 + m30;
        dest.m31 = m01 * rm30 + m11 * rm31 + m21 * rm32 + m31;
        dest.m32 = m02 * rm30 + m12 * rm31 + m22 * rm32 + m32;
        dest.m33 = m03 * rm30 + m13 * rm31 + m23 * rm32 + m33;
        return dest;
    }

    public Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
        return lookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
    }

    public Vector3f transformPosition(Vector3f v) {
        return transformPosition(v, v);
    }

    public Vector3f transformPosition(Vector3f v, Vector3f dest) {
        float rx = m00 * v.x + m10 * v.y + m20 * v.z + m30;
        float ry = m01 * v.x + m11 * v.y + m21 * v.z + m31;
        float rz = m02 * v.x + m12 * v.y + m22 * v.z + m32;
        dest.x = rx; dest.y = ry; dest.z = rz;
        return dest;
    }

    public Vector3f transformDirection(Vector3f v) {
        return transformDirection(v, v);
    }

    public Vector3f transformDirection(Vector3f v, Vector3f dest) {
        float rx = m00 * v.x + m10 * v.y + m20 * v.z;
        float ry = m01 * v.x + m11 * v.y + m21 * v.z;
        float rz = m02 * v.x + m12 * v.y + m22 * v.z;
        dest.x = rx; dest.y = ry; dest.z = rz;
        return dest;
    }

    public Vector4f transform(Vector4f v) {
        return transform(v, v);
    }

    public Vector4f transform(Vector4f v, Vector4f dest) {
        float rx = m00 * v.x + m10 * v.y + m20 * v.z + m30 * v.w;
        float ry = m01 * v.x + m11 * v.y + m21 * v.z + m31 * v.w;
        float rz = m02 * v.x + m12 * v.y + m22 * v.z + m32 * v.w;
        float rw = m03 * v.x + m13 * v.y + m23 * v.z + m33 * v.w;
        dest.x = rx; dest.y = ry; dest.z = rz; dest.w = rw;
        return dest;
    }

    public Matrix3f get3x3(Matrix3f dest) {
        dest.m00 = m00; dest.m01 = m01; dest.m02 = m02;
        dest.m10 = m10; dest.m11 = m11; dest.m12 = m12;
        dest.m20 = m20; dest.m21 = m21; dest.m22 = m22;
        return dest;
    }

    public Matrix4f set3x3(Matrix3f mat) {
        m00 = mat.m00; m01 = mat.m01; m02 = mat.m02;
        m10 = mat.m10; m11 = mat.m11; m12 = mat.m12;
        m20 = mat.m20; m21 = mat.m21; m22 = mat.m22;
        return this;
    }

    public Matrix4f rotation(float angle, float x, float y, float z) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float omc = 1.0f - c;
        m00 = x * x * omc + c;
        m01 = x * y * omc + z * s;
        m02 = x * z * omc - y * s;
        m03 = 0;
        m10 = y * x * omc - z * s;
        m11 = y * y * omc + c;
        m12 = y * z * omc + x * s;
        m13 = 0;
        m20 = z * x * omc + y * s;
        m21 = z * y * omc - x * s;
        m22 = z * z * omc + c;
        m23 = 0;
        m30 = 0; m31 = 0; m32 = 0; m33 = 1;
        return this;
    }

    public Matrix4f rotationX(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        identity();
        m11 = c;  m12 = s;
        m21 = -s; m22 = c;
        return this;
    }

    public Matrix4f rotationY(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        identity();
        m00 = c;  m02 = -s;
        m20 = s;  m22 = c;
        return this;
    }

    public Matrix4f rotationZ(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        identity();
        m00 = c;  m01 = s;
        m10 = -s; m11 = c;
        return this;
    }

    public Matrix4f translation(float x, float y, float z) {
        identity();
        m30 = x; m31 = y; m32 = z;
        return this;
    }

    public Matrix4f translation(Vector3f v) {
        return translation(v.x, v.y, v.z);
    }

    public Matrix4f scaling(float x, float y, float z) {
        identity();
        m00 = x; m11 = y; m22 = z;
        return this;
    }

    public Matrix4f scaling(float s) {
        return scaling(s, s, s);
    }

    public Vector3f getTranslation(Vector3f dest) {
        dest.x = m30; dest.y = m31; dest.z = m32;
        return dest;
    }

    public Quaternionf getUnnormalizedRotation(Quaternionf dest) {
        return dest;
    }

    public Quaternionf getNormalizedRotation(Quaternionf dest) {
        return dest;
    }

    public Matrix4f normal(Matrix4f dest) {
        float invDet = determinant();
        if (invDet == 0) invDet = 1;
        invDet = 1.0f / invDet;
        float m00m11 = m00 * m11, m00m12 = m00 * m12, m01m10 = m01 * m10;
        float m01m12 = m01 * m12, m02m10 = m02 * m10, m02m11 = m02 * m11;
        float m10m11 = m10 * m11, m10m12 = m10 * m12, m10m21 = m10 * m21;
        float m10m22 = m10 * m22, m11m20 = m11 * m20, m11m22 = m11 * m22;
        float m12m20 = m12 * m20, m12m21 = m12 * m21, m20m11 = m20 * m11;
        float m20m12 = m20 * m12, m21m10 = m21 * m10, m21m12 = m21 * m12;
        float m22m10 = m22 * m10, m22m11 = m22 * m11;
        // Simplified normal matrix (transpose of inverse of upper-left 3x3)
        dest.m00 = (m11m22 - m12m21) * invDet;
        dest.m01 = (m02m11 - m01m12) * invDet;
        dest.m02 = (m01m12 - m02m11) * invDet;
        dest.m03 = 0;
        dest.m10 = (m12m20 - m10m22) * invDet;
        dest.m11 = (m00m12 - m02m10) * invDet;
        dest.m12 = (m02m10 - m00m12) * invDet;
        dest.m13 = 0;
        dest.m20 = (m10m21 - m11m20) * invDet;
        dest.m21 = (m01m10 - m00m11) * invDet;
        dest.m22 = (m00m11 - m01m10) * invDet;
        dest.m23 = 0;
        dest.m30 = 0; dest.m31 = 0; dest.m32 = 0; dest.m33 = 1;
        return dest;
    }

    public Matrix4f normal() {
        return normal(new Matrix4f());
    }

    public Matrix3f normal(Matrix3f dest) {
        float invDet = 1.0f / determinant();
        if (Float.isInfinite(invDet)) invDet = 1.0f;
        dest.m00 = (m11 * m22 - m12 * m21) * invDet;
        dest.m01 = (m02 * m21 - m01 * m22) * invDet;
        dest.m02 = (m01 * m12 - m02 * m11) * invDet;
        dest.m10 = (m12 * m20 - m10 * m22) * invDet;
        dest.m11 = (m00 * m22 - m02 * m20) * invDet;
        dest.m12 = (m02 * m10 - m00 * m12) * invDet;
        dest.m20 = (m10 * m21 - m11 * m20) * invDet;
        dest.m21 = (m01 * m20 - m00 * m21) * invDet;
        dest.m22 = (m00 * m11 - m01 * m10) * invDet;
        return dest;
    }

    public float[] getTransposed(float[] arr) {
        if (arr == null || arr.length < 16) arr = new float[16];
        arr[0] = m00;  arr[1] = m10;  arr[2] = m20;  arr[3] = m30;
        arr[4] = m01;  arr[5] = m11;  arr[6] = m21;  arr[7] = m31;
        arr[8] = m02;  arr[9] = m12;  arr[10] = m22; arr[11] = m32;
        arr[12] = m03; arr[13] = m13; arr[14] = m23; arr[15] = m33;
        return arr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Matrix4f m = (Matrix4f) obj;
        return Float.compare(m.m00, m00) == 0 && Float.compare(m.m01, m01) == 0 && Float.compare(m.m02, m02) == 0 && Float.compare(m.m03, m03) == 0 &&
               Float.compare(m.m10, m10) == 0 && Float.compare(m.m11, m11) == 0 && Float.compare(m.m12, m12) == 0 && Float.compare(m.m13, m13) == 0 &&
               Float.compare(m.m20, m20) == 0 && Float.compare(m.m21, m21) == 0 && Float.compare(m.m22, m22) == 0 && Float.compare(m.m23, m23) == 0 &&
               Float.compare(m.m30, m30) == 0 && Float.compare(m.m31, m31) == 0 && Float.compare(m.m32, m32) == 0 && Float.compare(m.m33, m33) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(m00);
        result = 31 * result + Float.floatToIntBits(m01);
        result = 31 * result + Float.floatToIntBits(m02);
        result = 31 * result + Float.floatToIntBits(m03);
        result = 31 * result + Float.floatToIntBits(m10);
        result = 31 * result + Float.floatToIntBits(m11);
        result = 31 * result + Float.floatToIntBits(m12);
        result = 31 * result + Float.floatToIntBits(m13);
        result = 31 * result + Float.floatToIntBits(m20);
        result = 31 * result + Float.floatToIntBits(m21);
        result = 31 * result + Float.floatToIntBits(m22);
        result = 31 * result + Float.floatToIntBits(m23);
        result = 31 * result + Float.floatToIntBits(m30);
        result = 31 * result + Float.floatToIntBits(m31);
        result = 31 * result + Float.floatToIntBits(m32);
        result = 31 * result + Float.floatToIntBits(m33);
        return result;
    }
}
