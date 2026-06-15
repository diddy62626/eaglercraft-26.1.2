package org.joml;

public class Vector3f {
    public float x, y, z;

    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3f add(Vector3f v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3f add(Vector3f v, Vector3f dest) {
        dest.x = this.x + v.x;
        dest.y = this.y + v.y;
        dest.z = this.z + v.z;
        return dest;
    }

    public Vector3f sub(Vector3f v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3f sub(Vector3f v, Vector3f dest) {
        dest.x = this.x - v.x;
        dest.y = this.y - v.y;
        dest.z = this.z - v.z;
        return dest;
    }

    public Vector3f mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public Vector3f mul(float scalar, Vector3f dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }

    public Vector3f mul(Vector3f v) {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector3f mul(Vector3f v, Vector3f dest) {
        dest.x = this.x * v.x;
        dest.y = this.y * v.y;
        dest.z = this.z * v.z;
        return dest;
    }

    public Vector3f div(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public float distance(Vector3f v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public float distanceSquared(Vector3f v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public float dot(Vector3f v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3f cross(Vector3f v) {
        float rx = this.y * v.z - this.z * v.y;
        float ry = this.z * v.x - this.x * v.z;
        float rz = this.x * v.y - this.y * v.x;
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }

    public Vector3f cross(Vector3f v, Vector3f dest) {
        dest.x = this.y * v.z - this.z * v.y;
        dest.y = this.z * v.x - this.x * v.z;
        dest.z = this.x * v.y - this.y * v.x;
        return dest;
    }

    public Vector3f normalize() {
        float len = length();
        if (len > 0) {
            this.x /= len;
            this.y /= len;
            this.z /= len;
        }
        return this;
    }

    public Vector3f normalize(Vector3f dest) {
        float len = length();
        if (len > 0) {
            dest.x = this.x / len;
            dest.y = this.y / len;
            dest.z = this.z / len;
        } else {
            dest.x = this.x;
            dest.y = this.y;
            dest.z = this.z;
        }
        return dest;
    }

    public Vector3f negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Vector3f negate(Vector3f dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }

    public Vector3f zero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        return this;
    }

    public Vector3f absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }

    public float get(int component) {
        switch (component) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector3f v = (Vector3f) obj;
        return Float.compare(v.x, x) == 0 && Float.compare(v.y, y) == 0 && Float.compare(v.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
