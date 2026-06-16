package org.joml;

public class Vector2i {
    public int x, y;

    public Vector2i() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2i set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2i set(Vector2i v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2i add(Vector2i v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2i add(Vector2i v, Vector2i dest) {
        dest.x = this.x + v.x;
        dest.y = this.y + v.y;
        return dest;
    }

    public Vector2i sub(Vector2i v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2i sub(Vector2i v, Vector2i dest) {
        dest.x = this.x - v.x;
        dest.y = this.y - v.y;
        return dest;
    }

    public Vector2i mul(int scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public float distance(Vector2i v) {
        int dx = this.x - v.x;
        int dy = this.y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public int lengthSquared() {
        return x * x + y * y;
    }

    public Vector2i negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public Vector2i zero() {
        this.x = 0;
        this.y = 0;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2i v = (Vector2i) obj;
        return v.x == x && v.y == y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public long distanceSquared(int x2, int y2, int x3, int y3) {
        long dx = x3 - x2;
        long dy = y3 - y2;
        return dx * dx + dy * dy;
    }

    public long distanceSquared(Vector2i v) {
        long dx = v.x - x;
        long dy = v.y - y;
        return dx * dx + dy * dy;
    }

    public long distanceSquared(int x2, int y2) {
        long dx = x2 - x;
        long dy = y2 - y;
        return dx * dx + dy * dy;
    }

    public double distance(int x2, int y2) {
        return Math.sqrt(distanceSquared(x2, y2));
    }


    public Vector2i add(int x, int y) { this.x += x; this.y += y; return this; }
    public Vector2i add(Vector2ic v) { this.x += v.x(); this.y += v.y(); return this; }
    public Vector2i sub(int x, int y) { this.x -= x; this.y -= y; return this; }
    public Vector2i sub(Vector2ic v) { this.x -= v.x(); this.y -= v.y(); return this; }
    public int distanceSquared(Vector2ic v) {
        int dx = x - v.x(), dy = y - v.y();
        return dx*dx + dy*dy;
    }
    public int distance(Vector2ic v) { return (int) java.lang.Math.sqrt(distanceSquared(v)); }
}
