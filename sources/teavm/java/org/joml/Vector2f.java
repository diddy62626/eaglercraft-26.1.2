package org.joml;

public class Vector2f {
    public float x, y;

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2f add(Vector2f v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2f add(Vector2f v, Vector2f dest) {
        dest.x = this.x + v.x;
        dest.y = this.y + v.y;
        return dest;
    }

    public Vector2f sub(Vector2f v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2f sub(Vector2f v, Vector2f dest) {
        dest.x = this.x - v.x;
        dest.y = this.y - v.y;
        return dest;
    }

    public Vector2f mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2f mul(float scalar, Vector2f dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        return dest;
    }

    public Vector2f mul(Vector2f v) {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vector2f div(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public float distance(Vector2f v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float dot(Vector2f v) {
        return this.x * v.x + this.y * v.y;
    }

    public Vector2f normalize() {
        float len = length();
        if (len > 0) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public Vector2f normalize(Vector2f dest) {
        float len = length();
        if (len > 0) {
            dest.x = this.x / len;
            dest.y = this.y / len;
        } else {
            dest.x = this.x;
            dest.y = this.y;
        }
        return dest;
    }

    public Vector2f negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public Vector2f zero() {
        this.x = 0;
        this.y = 0;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2f v = (Vector2f) obj;
        return Float.compare(v.x, x) == 0 && Float.compare(v.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
