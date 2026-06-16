package org.joml;

public interface Quaternionfc {
    float x();
    float y();
    float z();
    float w();

    default float length() { return (float) java.lang.Math.sqrt(x()*x() + y()*y() + z()*z() + w()*w()); }
    default float lengthSquared() { return x()*x() + y()*y() + z()*z() + w()*w(); }
    default float dot(Quaternionfc q) { return x()*q.x() + y()*q.y() + z()*q.z() + w()*q.w(); }
    default float angle() { return 0; }
    default Vector3f get(Vector3f dest) { return dest; }
    default Quaternionf normalize(Quaternionf dest) { return dest; }
    default Quaternionf conjugate(Quaternionf dest) { return dest; }
    default Quaternionf invert(Quaternionf dest) { return dest; }
    default Quaternionf mul(Quaternionfc q, Quaternionf dest) { return dest; }
    default Quaternionf rotateX(float angle, Quaternionf dest) { return dest; }
    default Quaternionf rotateY(float angle, Quaternionf dest) { return dest; }
    default Quaternionf rotateZ(float angle, Quaternionf dest) { return dest; }
    default Quaternionf rotateXYZ(float x, float y, float z, Quaternionf dest) { return dest; }
    default Quaternionf rotationXYZ(float x, float y, float z) { return null; }
    default Quaternionf rotationYXZ(float y, float x, float z) { return null; }
    default Quaternionf rotationZYX(float z, float y, float x) { return null; }
}
