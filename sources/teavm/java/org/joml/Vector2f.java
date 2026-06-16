package org.joml;

public class Vector2f implements Vector2fc {
    public float x, y;

    public Vector2f() {}
    public Vector2f(float x, float y) { this.x = x; this.y = y; }
    public Vector2f(float f) { this.x = this.y = f; }
    public Vector2f(Vector2fc v) { this.x = v.x(); this.y = v.y(); }
    public Vector2f(Vector2f v) { this.x = v.x; this.y = v.y; }

    @Override public float x() { return x; }
    @Override public float y() { return y; }

    public Vector2f set(float x, float y) { this.x = x; this.y = y; return this; }
    public Vector2f set(Vector2fc v) { this.x = v.x(); this.y = v.y(); return this; }
    public Vector2f set(Vector2f v) { this.x = v.x; this.y = v.y; return this; }

    public Vector2f add(Vector2f v) { x += v.x; y += v.y; return this; }
    public Vector2f add(float x, float y) { this.x += x; this.y += y; return this; }
    public Vector2f sub(Vector2f v) { x -= v.x; y -= v.y; return this; }
    public Vector2f mul(float s) { x *= s; y *= s; return this; }
    public Vector2f div(float s) { x /= s; y /= s; return this; }
    public Vector2f negate() { x = -x; y = -y; return this; }
    public Vector2f normalize() { float l = length(); if (l > 0) mul(1/l); return this; }
    public float length() { return (float) java.lang.Math.sqrt(x*x + y*y); }
    public float lengthSquared() { return x*x + y*y; }
    public float dot(Vector2f v) { return x*v.x + y*v.y; }
    public float distance(Vector2f v) {
        float dx = x - v.x, dy = y - v.y;
        return (float) java.lang.Math.sqrt(dx*dx + dy*dy);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
