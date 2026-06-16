package org.joml;

public class Vector4f implements Vector4fc {
    public float x, y, z, w;

    public Vector4f() {}
    public Vector4f(float f) { x = y = z = w = f; }
    public Vector4f(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; }
    public Vector4f(Vector3fc v, float w) { this.x = v.x(); this.y = v.y(); this.z = v.z(); this.w = w; }
    public Vector4f(Vector4f v) { this.x = v.x; this.y = v.y; this.z = v.z; this.w = v.w; }

    public float x() { return x; }
    public float y() { return y; }
    public float z() { return z; }
    public float w() { return w; }

    public Vector4f set(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; return this; }
    public Vector4f set(Vector4f v) { this.x = v.x; this.y = v.y; this.z = v.z; this.w = v.w; return this; }

    public Vector4f add(Vector4f v) { x += v.x; y += v.y; z += v.z; w += v.w; return this; }
    public Vector4f sub(Vector4f v) { x -= v.x; y -= v.y; z -= v.z; w -= v.w; return this; }
    public Vector4f mul(float s) { x *= s; y *= s; z *= s; w *= s; return this; }
    public Vector4f div(float s) { x /= s; y /= s; z /= s; w /= s; return this; }
    public Vector4f negate() { x = -x; y = -y; z = -z; w = -w; return this; }
    public float length() { return (float) java.lang.Math.sqrt(x*x + y*y + z*z + w*w); }
    public float lengthSquared() { return x*x + y*y + z*z + w*w; }
    public float dot(Vector4f v) { return x*v.x + y*v.y + z*v.z + w*v.w; }
    public Vector4f normalize() { float l = length(); if (l > 0) mul(1/l); return this; }
    public Vector4f zero() { x = y = z = w = 0; return this; }

    public Vector4f set(double x, double y, double z, double w) { this.x = (float)x; this.y = (float)y; this.z = (float)z; this.w = (float)w; return this; }
    public Vector4f set(Vector4fc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); this.w = v.w(); return this; }
    public Vector4f lerp(Vector4fc v, float t, Vector4f dest) { return dest; }

    public Vector4f mul(Matrix4fc mat, Vector4f dest) { return dest; }
}
