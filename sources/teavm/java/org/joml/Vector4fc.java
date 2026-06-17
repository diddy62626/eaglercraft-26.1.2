package org.joml;

public interface Vector4fc {
    float x();
    float y();
    float z();
    float w();
    default float length() { return (float) java.lang.Math.sqrt(x()*x() + y()*y() + z()*z() + w()*w()); }
    default float lengthSquared() { return x()*x() + y()*y() + z()*z() + w()*w(); }

    default java.nio.ByteBuffer get(java.nio.ByteBuffer buf) { return buf; }
    default java.nio.ByteBuffer get(int offset, java.nio.ByteBuffer buf) { return buf; }
}
