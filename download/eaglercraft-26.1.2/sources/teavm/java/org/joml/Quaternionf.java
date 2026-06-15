package org.joml;

public class Quaternionf {
    public float x, y, z, w;

    public Quaternionf() {
        identity();
    }

    public Quaternionf(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternionf(Quaternionf q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }

    public Quaternionf identity() {
        x = 0; y = 0; z = 0; w = 1;
        return this;
    }

    public Quaternionf set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternionf set(Quaternionf q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
        return this;
    }

    public Quaternionf normalize() {
        float len = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        if (len > 0) {
            x /= len;
            y /= len;
            z /= len;
            w /= len;
        }
        return this;
    }

    public Quaternionf normalize(Quaternionf dest) {
        float len = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        if (len > 0) {
            dest.x = x / len;
            dest.y = y / len;
            dest.z = z / len;
            dest.w = w / len;
        } else {
            dest.x = x; dest.y = y; dest.z = z; dest.w = w;
        }
        return dest;
    }

    public Quaternionf rotateX(float angle) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        float nw = w * c - x * s;
        float nx = w * s + x * c;
        float ny = y * c + z * s;
        float nz = -y * s + z * c;
        this.x = nx; this.y = ny; this.z = nz; this.w = nw;
        return this;
    }

    public Quaternionf rotateX(float angle, Quaternionf dest) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        dest.x = w * s + x * c;
        dest.y = y * c + z * s;
        dest.z = -y * s + z * c;
        dest.w = w * c - x * s;
        return dest;
    }

    public Quaternionf rotateY(float angle) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        float nw = w * c - y * s;
        float nx = x * c - z * s;
        float ny = w * s + y * c;
        float nz = x * s + z * c;
        this.x = nx; this.y = ny; this.z = nz; this.w = nw;
        return this;
    }

    public Quaternionf rotateY(float angle, Quaternionf dest) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        dest.x = x * c - z * s;
        dest.y = w * s + y * c;
        dest.z = x * s + z * c;
        dest.w = w * c - y * s;
        return dest;
    }

    public Quaternionf rotateZ(float angle) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        float nw = w * c - z * s;
        float nx = x * c + y * s;
        float ny = -x * s + y * c;
        float nz = w * s + z * c;
        this.x = nx; this.y = ny; this.z = nz; this.w = nw;
        return this;
    }

    public Quaternionf rotateZ(float angle, Quaternionf dest) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        dest.x = x * c + y * s;
        dest.y = -x * s + y * c;
        dest.z = w * s + z * c;
        dest.w = w * c - z * s;
        return dest;
    }

    public Quaternionf rotate(float angle, float axisX, float axisY, float axisZ) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        float qx = axisX * s;
        float qy = axisY * s;
        float qz = axisZ * s;
        float qw = c;
        float nw = w * qw - x * qx - y * qy - z * qz;
        float nx = w * qx + x * qw + y * qz - z * qy;
        float ny = w * qy - x * qz + y * qw + z * qx;
        float nz = w * qz + x * qy - y * qx + z * qw;
        this.x = nx; this.y = ny; this.z = nz; this.w = nw;
        return this;
    }

    public Quaternionf slerp(Quaternionf target, float alpha) {
        float cosom = x * target.x + y * target.y + z * target.z + w * target.w;
        float bx = target.x, by = target.y, bz = target.z, bw = target.w;
        if (cosom < 0) {
            cosom = -cosom;
            bx = -bx; by = -by; bz = -bz; bw = -bw;
        }
        float s0, s1;
        if (1.0f - cosom > 1e-6f) {
            float omega = (float) Math.acos(cosom);
            float sinom = (float) Math.sin(omega);
            s0 = (float) Math.sin((1.0f - alpha) * omega) / sinom;
            s1 = (float) Math.sin(alpha * omega) / sinom;
        } else {
            s0 = 1.0f - alpha;
            s1 = alpha;
        }
        this.x = s0 * x + s1 * bx;
        this.y = s0 * y + s1 * by;
        this.z = s0 * z + s1 * bz;
        this.w = s0 * w + s1 * bw;
        return this;
    }

    public Quaternionf slerp(Quaternionf target, float alpha, Quaternionf dest) {
        dest.set(this);
        return dest.slerp(target, alpha);
    }

    public Quaternionf mul(Quaternionf q) {
        return mul(q, this);
    }

    public Quaternionf mul(Quaternionf q, Quaternionf dest) {
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y - x * q.z + y * q.w + z * q.x;
        float nz = w * q.z + x * q.y - y * q.x + z * q.w;
        dest.x = nx; dest.y = ny; dest.z = nz; dest.w = nw;
        return dest;
    }

    public Quaternionf conjugate() {
        x = -x; y = -y; z = -z;
        return this;
    }

    public Quaternionf conjugate(Quaternionf dest) {
        dest.x = -x; dest.y = -y; dest.z = -z; dest.w = w;
        return dest;
    }

    public Quaternionf invert() {
        float len = x * x + y * y + z * z + w * w;
        if (len > 0) {
            float invLen = 1.0f / len;
            x = -x * invLen; y = -y * invLen; z = -z * invLen; w = w * invLen;
        }
        return this;
    }

    public Quaternionf invert(Quaternionf dest) {
        float len = x * x + y * y + z * z + w * w;
        if (len > 0) {
            float invLen = 1.0f / len;
            dest.x = -x * invLen; dest.y = -y * invLen; dest.z = -z * invLen; dest.w = w * invLen;
        } else {
            dest.x = x; dest.y = y; dest.z = z; dest.w = w;
        }
        return dest;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public float dot(Quaternionf q) {
        return x * q.x + y * q.y + z * q.z + w * q.w;
    }

    public Vector3f transform(Vector3f v) {
        return transform(v, v);
    }

    public Vector3f transform(Vector3f v, Vector3f dest) {
        float qx = x, qy = y, qz = z, qw = w;
        float tx = 2 * (qy * v.z - qz * v.y);
        float ty = 2 * (qz * v.x - qx * v.z);
        float tz = 2 * (qx * v.y - qy * v.x);
        dest.x = v.x + qw * tx + qy * tz - qz * ty;
        dest.y = v.y + qw * ty + qz * tx - qx * tz;
        dest.z = v.z + qw * tz + qx * ty - qy * tx;
        return dest;
    }

    public Quaternionf rotationX(float angle) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        x = s; y = 0; z = 0; w = c;
        return this;
    }

    public Quaternionf rotationY(float angle) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        x = 0; y = s; z = 0; w = c;
        return this;
    }

    public Quaternionf rotationZ(float angle) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        x = 0; y = 0; z = s; w = c;
        return this;
    }

    public Quaternionf rotation(float angle, float axisX, float axisY, float axisZ) {
        float s = (float) Math.sin(angle * 0.5f);
        float c = (float) Math.cos(angle * 0.5f);
        x = axisX * s; y = axisY * s; z = axisZ * s; w = c;
        return this;
    }

    public Matrix4f get(Matrix4f dest) {
        float qx = x, qy = y, qz = z, qw = w;
        float qx2 = qx * qx, qy2 = qy * qy, qz2 = qz * qz;
        float qxy = qx * qy, qxz = qx * qz, qyz = qy * qz;
        float qwx = qw * qx, qwy = qw * qy, qwz = qw * qz;
        dest.m00 = 1 - 2 * (qy2 + qz2); dest.m01 = 2 * (qxy + qwz);     dest.m02 = 2 * (qxz - qwy);     dest.m03 = 0;
        dest.m10 = 2 * (qxy - qwz);     dest.m11 = 1 - 2 * (qx2 + qz2); dest.m12 = 2 * (qyz + qwx);     dest.m13 = 0;
        dest.m20 = 2 * (qxz + qwy);     dest.m21 = 2 * (qyz - qwx);     dest.m22 = 1 - 2 * (qx2 + qy2); dest.m23 = 0;
        dest.m30 = 0;                    dest.m31 = 0;                    dest.m32 = 0;                    dest.m33 = 1;
        return dest;
    }

    public Matrix3f get(Matrix3f dest) {
        float qx = x, qy = y, qz = z, qw = w;
        float qx2 = qx * qx, qy2 = qy * qy, qz2 = qz * qz;
        float qxy = qx * qy, qxz = qx * qz, qyz = qy * qz;
        float qwx = qw * qx, qwy = qw * qy, qwz = qw * qz;
        dest.m00 = 1 - 2 * (qy2 + qz2); dest.m01 = 2 * (qxy + qwz);     dest.m02 = 2 * (qxz - qwy);
        dest.m10 = 2 * (qxy - qwz);     dest.m11 = 1 - 2 * (qx2 + qz2); dest.m12 = 2 * (qyz + qwx);
        dest.m20 = 2 * (qxz + qwy);     dest.m21 = 2 * (qyz - qwx);     dest.m22 = 1 - 2 * (qx2 + qy2);
        return dest;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quaternionf q = (Quaternionf) obj;
        return Float.compare(q.x, x) == 0 && Float.compare(q.y, y) == 0 && Float.compare(q.z, z) == 0 && Float.compare(q.w, w) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        result = 31 * result + Float.floatToIntBits(w);
        return result;
    }

    @Override
    public String toString() {
        return "Quaternionf(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
