package org.joml;

/**
 * EaglerCraft stub for org.joml.Quaternionf.
 */
public class Quaternionf implements Quaternionfc {
    public float x, y, z, w;

    public Quaternionf() {
        x = 0; y = 0; z = 0; w = 1;
    }

    public Quaternionf(float x, float y, float z, float w) {
        this.x = x; this.y = y; this.z = z; this.w = w;
    }

    public Quaternionf(Quaternionf q) {
        this.x = q.x; this.y = q.y; this.z = q.z; this.w = q.w;
    }

    public Quaternionf(AxisAngle4f axisAngle) {
        float halfAngle = axisAngle.angle * 0.5f;
        float sin = (float) Math.sin(halfAngle);
        float cos = (float) Math.cos(halfAngle);
        x = axisAngle.x * sin;
        y = axisAngle.y * sin;
        z = axisAngle.z * sin;
        w = cos;
    }

    public Quaternionf identity() {
        x = 0; y = 0; z = 0; w = 1;
        return this;
    }

    public Quaternionf set(float x, float y, float z, float w) {
        this.x = x; this.y = y; this.z = z; this.w = w;
        return this;
    }

    public Quaternionf set(Quaternionf q) {
        this.x = q.x; this.y = q.y; this.z = q.z; this.w = q.w;
        return this;
    }

    public Quaternionf normalize() {
        float invNorm = 1.0f / (float) Math.sqrt(x * x + y * y + z * z + w * w);
        x *= invNorm; y *= invNorm; z *= invNorm; w *= invNorm;
        return this;
    }

    // Quaternionfc interface
    @Override public float x() { return x; }
    @Override public float y() { return y; }
    @Override public float z() { return z; }
    @Override public float w() { return w; }

    /**
     * MC 26.1.2: Sets this quaternion to a rotation around the Y axis.
     */
    public Quaternionf rotationY(float angle) {
        float halfAngle = angle * 0.5f;
        float sin = (float) Math.sin(halfAngle);
        float cos = (float) Math.cos(halfAngle);
        this.x = 0;
        this.y = sin;
        this.z = 0;
        this.w = cos;
        return this;
    }
}
