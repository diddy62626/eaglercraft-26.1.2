package org.joml;

public class AxisAngle4f {
    public float angle, x, y, z;

    public AxisAngle4f() {
        this.angle = 0;
        this.x = 0;
        this.y = 0;
        this.z = 1;
    }

    public AxisAngle4f(float angle, float x, float y, float z) {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AxisAngle4f(AxisAngle4f a) {
        this.angle = a.angle;
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public AxisAngle4f(Quaternionf q) {
        set(q);
    }

    public AxisAngle4f set(float angle, float x, float y, float z) {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public AxisAngle4f set(AxisAngle4f a) {
        this.angle = a.angle;
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        return this;
    }

    public AxisAngle4f set(Quaternionf q) {
        float cosHalfAngle = q.w;
        float sinHalfAngle = (float) Math.sqrt(q.x * q.x + q.y * q.y + q.z * q.z);
        this.angle = 2.0f * (float) Math.atan2(sinHalfAngle, cosHalfAngle);
        if (sinHalfAngle > 1e-6f) {
            this.x = q.x / sinHalfAngle;
            this.y = q.y / sinHalfAngle;
            this.z = q.z / sinHalfAngle;
        } else {
            this.x = 0;
            this.y = 0;
            this.z = 1;
        }
        return this;
    }

    public Quaternionf get(Quaternionf q) {
        float halfAngle = angle * 0.5f;
        float s = (float) Math.sin(halfAngle);
        q.x = x * s;
        q.y = y * s;
        q.z = z * s;
        q.w = (float) Math.cos(halfAngle);
        return q;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AxisAngle4f a = (AxisAngle4f) obj;
        return Float.compare(a.angle, angle) == 0 && Float.compare(a.x, x) == 0 && Float.compare(a.y, y) == 0 && Float.compare(a.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(angle);
        result = 31 * result + Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        return result;
    }

    @Override
    public String toString() {
        return "AxisAngle4f(" + angle + ", " + x + ", " + y + ", " + z + ")";
    }
}
