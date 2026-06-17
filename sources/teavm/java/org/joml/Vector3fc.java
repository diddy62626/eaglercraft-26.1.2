package org.joml;

import java.nio.ByteBuffer;

public interface Vector3fc {
    float x();
    float y();
    float z();

    default ByteBuffer get(ByteBuffer buf) { return buf; }
    default ByteBuffer get(int offset, ByteBuffer buf) { return buf; }
    default float length() { return (float) java.lang.Math.sqrt(x()*x() + y()*y() + z()*z()); }
    default float lengthSquared() { return x()*x() + y()*y() + z()*z(); }
    default float distance(Vector3fc v) {
        float dx = x() - v.x(), dy = y() - v.y(), dz = z() - v.z();
        return (float) java.lang.Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    default float distanceSquared(Vector3fc v) {
        float dx = x() - v.x(), dy = y() - v.y(), dz = z() - v.z();
        return dx*dx + dy*dy + dz*dz;
    }
    default float dot(Vector3fc v) { return x()*v.x() + y()*v.y() + z()*v.z(); }
    default float dot(float x, float y, float z) { return x()*x + y()*y + z()*z; }
    default Vector3f normalize(Vector3f dest) { return dest; }
    default Vector3f cross(Vector3fc v, Vector3f dest) { return dest; }
    default Vector3f add(Vector3fc v, Vector3f dest) { return dest; }
    default Vector3f sub(Vector3fc v, Vector3f dest) { return dest; }
    default Vector3f mul(float s, Vector3f dest) { return dest; }
    default Vector3f mul(Vector3fc v, Vector3f dest) { return dest; }
    default Vector3f negate(Vector3f dest) { return dest; }
    default Vector3f lerp(Vector3fc v, float t, Vector3f dest) { return dest; }
    default Vector3f get(Vector3f dest) { return dest; }
    default float angle(Vector3fc v) { return 0; }
    default float angleCos(Vector3fc v) { return 1; }
}
