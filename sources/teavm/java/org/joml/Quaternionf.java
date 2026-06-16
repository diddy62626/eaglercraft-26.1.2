package org.joml;

public class Quaternionf implements Quaternionfc {
    public float x, y, z, w;

    public Quaternionf() { x = 0; y = 0; z = 0; w = 1; }
    public Quaternionf(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; }
    public Quaternionf(Quaternionfc q) { this.x = q.x(); this.y = q.y(); this.z = q.z(); this.w = q.w(); }

    @Override public float x() { return x; }
    @Override public float y() { return y; }
    @Override public float z() { return z; }
    @Override public float w() { return w; }

    public Quaternionf set(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; return this; }
    public Quaternionf set(Quaternionfc q) { this.x = q.x(); this.y = q.y(); this.z = q.z(); this.w = q.w(); return this; }
    public Quaternionf set(Quaternionf q) { this.x = q.x; this.y = q.y; this.z = q.z; this.w = q.w; return this; }

    public Quaternionf identity() { x = 0; y = 0; z = 0; w = 1; return this; }
    public Quaternionf normalize() { return this; }
    public Quaternionf conjugate() { x = -x; y = -y; z = -z; return this; }
    public Quaternionf conjugate(Quaternionf dest) { return dest; }
    public Quaternionf invert() { return this; }
    public Quaternionf invert(Quaternionf dest) { return dest; }
    public Quaternionf mul(Quaternionfc q) { return this; }
    public Quaternionf mul(Quaternionfc q, Quaternionf dest) { return dest; }

    public Quaternionf rotateX(float angle) { return this; }
    public Quaternionf rotateY(float angle) { return this; }
    public Quaternionf rotateZ(float angle) { return this; }
    public Quaternionf rotateXYZ(float x, float y, float z) { return this; }
    public Quaternionf rotateZYX(float z, float y, float x) { return this; }
    public Quaternionf rotateYXZ(float y, float x, float z) { return this; }
    public Quaternionf rotateAxis(float angle, float x, float y, float z) { return this; }
    public Quaternionf rotateLocalX(float angle) { return this; }
    public Quaternionf rotateLocalY(float angle) { return this; }
    public Quaternionf rotateLocalZ(float angle) { return this; }

    public Quaternionf rotationX(float angle) {
        float half = angle * 0.5f;
        float sin = (float) java.lang.Math.sin(half);
        float cos = (float) java.lang.Math.cos(half);
        this.x = sin; this.y = 0; this.z = 0; this.w = cos;
        return this;
    }
    public Quaternionf rotationY(float angle) {
        float half = angle * 0.5f;
        float sin = (float) java.lang.Math.sin(half);
        float cos = (float) java.lang.Math.cos(half);
        this.x = 0; this.y = sin; this.z = 0; this.w = cos;
        return this;
    }
    public Quaternionf rotationZ(float angle) {
        float half = angle * 0.5f;
        float sin = (float) java.lang.Math.sin(half);
        float cos = (float) java.lang.Math.cos(half);
        this.x = 0; this.y = 0; this.z = sin; this.w = cos;
        return this;
    }
    public Quaternionf rotationXYZ(float x, float y, float z) { return this; }
    public Quaternionf rotationYXZ(float y, float x, float z) { return this; }
    public Quaternionf rotationZYX(float z, float y, float x) { return this; }
    public Quaternionf fromAxisAngle(float x, float y, float z, float angle) { return this; }
    public Quaternionf fromAxisAngle(Vector3fc axis, float angle) { return this; }
    public Quaternionf fromAxisAngleRad(float x, float y, float z, float angle) { return this; }

    public float length() { return (float) java.lang.Math.sqrt(x*x + y*y + z*z + w*w); }
    public float lengthSquared() { return x*x + y*y + z*z + w*w; }
    public float dot(Quaternionfc q) { return x*q.x() + y*q.y() + z*q.z() + w*q.w(); }
    public float angle() { return 0; }
    public Vector3f get(Vector3f dest) { return dest; }
    public Quaternionf slerp(Quaternionfc target, float alpha) { return this; }
    public Quaternionf nlerp(Quaternionfc target, float alpha) { return this; }
    public Quaternionf difference(Quaternionfc other) { return this; }
}
