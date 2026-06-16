package org.joml;

import java.nio.ByteBuffer;

public class Vector3f implements Vector3fc {
    public float x, y, z;

    public Vector3f() {}
    public Vector3f(float x, float y, float z) { this.x = x; this.y = y; this.z = z; }
    public Vector3f(float f) { this.x = this.y = this.z = f; }
    public Vector3f(Vector3fc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); }
    public Vector3f(Vector3f v) { this.x = v.x; this.y = v.y; this.z = v.z; }

    @Override public float x() { return x; }
    @Override public float y() { return y; }
    @Override public float z() { return z; }

    public Vector3f set(float x, float y, float z) { this.x = x; this.y = y; this.z = z; return this; }
    public Vector3f set(Vector3fc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); return this; }
    public Vector3f set(Vector3f v) { this.x = v.x; this.y = v.y; this.z = v.z; return this; }
    public Vector3f set(float f) { this.x = this.y = this.z = f; return this; }

    public Vector3f add(Vector3f v) { x += v.x; y += v.y; z += v.z; return this; }
    public Vector3f add(Vector3fc v) { x += v.x(); y += v.y(); z += v.z(); return this; }
    public Vector3f add(float x, float y, float z) { this.x += x; this.y += y; this.z += z; return this; }
    public Vector3f sub(Vector3f v) { x -= v.x; y -= v.y; z -= v.z; return this; }
    public Vector3f sub(Vector3fc v) { x -= v.x(); y -= v.y(); z -= v.z(); return this; }
    public Vector3f sub(float x, float y, float z) { this.x -= x; this.y -= y; this.z -= z; return this; }
    public Vector3f mul(float s) { x *= s; y *= s; z *= s; return this; }
    public Vector3f mul(float x, float y, float z) { this.x *= x; this.y *= y; this.z *= z; return this; }
    public Vector3f mul(Vector3fc v) { x *= v.x(); y *= v.y(); z *= v.z(); return this; }
    public Vector3f div(float s) { x /= s; y /= s; z /= s; return this; }
    public Vector3f negate() { x = -x; y = -y; z = -z; return this; }
    public Vector3f normalize() {
        float len = length();
        if (len > 0) { x /= len; y /= len; z /= len; }
        return this;
    }
    public float length() { return (float) java.lang.Math.sqrt(x*x + y*y + z*z); }
    public float lengthSquared() { return x*x + y*y + z*z; }
    public float dot(Vector3fc v) { return x*v.x() + y*v.y() + z*v.z(); }
    public Vector3f cross(Vector3fc v) {
        float nx = y*v.z() - z*v.y();
        float ny = z*v.x() - x*v.z();
        float nz = x*v.y() - y*v.x();
        x = nx; y = ny; z = nz;
        return this;
    }
    public float distance(Vector3fc v) {
        float dx = x - v.x(), dy = y - v.y(), dz = z - v.z();
        return (float) java.lang.Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    public float distanceSquared(Vector3fc v) {
        float dx = x - v.x(), dy = y - v.y(), dz = z - v.z();
        return dx*dx + dy*dy + dz*dz;
    }
    public Vector3f rotate(Quaternionfc q) { return this; }
    public Vector3f rotate(float angle, float ax, float ay, float az) { return this; }
    public Vector3f lerp(Vector3fc v, float t) {
        x = x + (v.x() - x) * t;
        y = y + (v.y() - y) * t;
        z = z + (v.z() - z) * t;
        return this;
    }
    public Vector3f zero() { x = y = z = 0; return this; }
    public Vector3f setComponent(int component, float value) {
        switch (component) {
            case 0: x = value; break;
            case 1: y = value; break;
            case 2: z = value; break;
        }
        return this;
    }
    public float get(int component) {
        switch (component) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: return 0;
        }
    }
    public float angle(Vector3fc v) { return 0; }
    public float angleCos(Vector3fc v) { return 1; }
    public Vector3f smoothStep(Vector3fc v, float t, Vector3f dest) { return dest; }
    public Vector3f hermite(Vector3fc t0, Vector3fc v1, Vector3fc t1, float t, Vector3f dest) { return dest; }
    public Vector3f catmullRom(Vector3fc v0, Vector3fc v1, Vector3fc v2, float t, Vector3f dest) { return dest; }
    public Vector3f max(Vector3fc v) { return this; }
    public Vector3f min(Vector3fc v) { return this; }
    public Vector3f fma(Vector3fc a, Vector3fc b) { return this; }
    public Vector3f fma(float a, Vector3fc b) { return this; }
    public Vector3f perpendicular() { return this; }
    public Vector3f reflect(Vector3fc normal) { return this; }
    public Vector3f half(Vector3fc other) { return this; }
    public Vector3f floor() { return this; }
    public Vector3f ceil() { return this; }
    public Vector3f round() { return this; }
    public Vector3f absolute() { x = java.lang.Math.abs(x); y = java.lang.Math.abs(y); z = java.lang.Math.abs(z); return this; }
    public Vector3f negate(Vector3f dest) { return dest; }

    public java.nio.ByteBuffer get(java.nio.ByteBuffer buf) { return buf; }
    public java.nio.ByteBuffer get(int offset, java.nio.ByteBuffer buf) { return buf; }

    @Override public String toString() { return "(" + x + "," + y + "," + z + ")"; }
    @Override public boolean equals(Object o) {
        if (!(o instanceof Vector3f)) return false;
        Vector3f v = (Vector3f) o;
        return x == v.x && y == v.y && z == v.z;
    }
    @Override public int hashCode() { return java.lang.Float.hashCode(x) + java.lang.Float.hashCode(y) + java.lang.Float.hashCode(z); }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public Vector3f rotate(Quaternionfc q, Vector3f dest) { return dest; }
    public Vector3f mulPosition(Matrix4fc mat) { return this; }

    public Vector3f rotateY(float angle) { return this; }

    public Vector3f rotateX(float angle) { return this; }
}
