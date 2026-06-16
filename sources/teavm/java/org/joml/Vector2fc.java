package org.joml;

public interface Vector2fc {
    float x();
    float y();

    default float length() { return (float) java.lang.Math.sqrt(x()*x() + y()*y()); }
    default float lengthSquared() { return x()*x() + y()*y(); }
    default float dot(Vector2fc v) { return x()*v.x() + y()*v.y(); }
    default Vector2f add(Vector2fc v, Vector2f dest) { return dest; }
    default Vector2f sub(Vector2fc v, Vector2f dest) { return dest; }
    default Vector2f mul(float s, Vector2f dest) { return dest; }
    default Vector2f normalize(Vector2f dest) { return dest; }
    default Vector2f negate(Vector2f dest) { return dest; }
}
