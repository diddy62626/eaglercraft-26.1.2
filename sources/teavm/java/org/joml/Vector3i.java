package org.joml;

public class Vector3i {
    public int x, y, z;

    public Vector3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(Vector3i v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3i set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3i set(Vector3i v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3i add(Vector3i v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3i add(Vector3i v, Vector3i dest) {
        dest.x = this.x + v.x;
        dest.y = this.y + v.y;
        dest.z = this.z + v.z;
        return dest;
    }

    public Vector3i sub(Vector3i v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3i sub(Vector3i v, Vector3i dest) {
        dest.x = this.x - v.x;
        dest.y = this.y - v.y;
        dest.z = this.z - v.z;
        return dest;
    }

    public Vector3i mul(int scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public float distance(Vector3i v) {
        int dx = this.x - v.x;
        int dy = this.y - v.y;
        int dz = this.z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public int lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3i negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Vector3i zero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector3i v = (Vector3i) obj;
        return v.x == x && v.y == y && v.z == z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public int get(int component) {
        switch (component) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: throw new IndexOutOfBoundsException();
        }
    }

    public Vector3i setComponent(int component, int value) {
        switch (component) {
            case 0: x = value; break;
            case 1: y = value; break;
            case 2: z = value; break;
            default: throw new IndexOutOfBoundsException();
        }
        return this;
    }
}
