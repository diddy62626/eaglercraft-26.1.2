package org.joml;

/**
 * EaglerCraft stub for org.joml.AxisAngle4f.
 */
public class AxisAngle4f {
    public float x, y, z, angle;

    public AxisAngle4f() {}

    public AxisAngle4f(float angle, float x, float y, float z) {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AxisAngle4f(float angle, Vector3fc v) {
        this.angle = angle;
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }

    public AxisAngle4f(AxisAngle4f a) {
        this.angle = a.angle;
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public AxisAngle4f(Quaternionfc q) {
        float cos = q.w();
        float angle = (float) Math.acos(cos) * 2.0f;
        float sin = (float) Math.sin(angle / 2.0f);
        if (sin != 0) {
            this.x = q.x() / sin;
            this.y = q.y() / sin;
            this.z = q.z() / sin;
        } else {
            this.x = 1.0f;
            this.y = 0.0f;
            this.z = 0.0f;
        }
        this.angle = angle;
    }
}
