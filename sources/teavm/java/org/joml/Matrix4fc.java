package org.joml;

import java.nio.ByteBuffer;

public interface Matrix4fc {
    float m00(); float m01(); float m02(); float m03();
    float m10(); float m11(); float m12(); float m13();
    float m20(); float m21(); float m22(); float m23();
    float m30(); float m31(); float m32(); float m33();

    default int properties() { return 0; }
    default ByteBuffer get(ByteBuffer buf) { return buf; }
    default ByteBuffer get(int offset, ByteBuffer buf) { return buf; }
    default float[] get(float[] arr) { return arr; }
    default Matrix4f get(Matrix4f dest) { return dest; }
    default Matrix4f mul(Matrix4fc right, Matrix4f dest) { return dest; }
    default Matrix4f invert(Matrix4f dest) { return dest; }
    default float determinant() { return 1.0f; }
    default Matrix4f transpose(Matrix4f dest) { return dest; }
    default Vector4f transform(Vector4f v) { return v; }
    default Vector3f transformDirection(Vector3fc v, Vector3f dest) { return dest; }
    default Vector3f transformPosition(Vector3fc v, Vector3f dest) { return dest; }
    default Vector4f transformTranspose(Vector4f v) { return v; }

    default Vector3f transformDirection(Vector3f v) { return v; }
    default Vector3f transformPosition(float x, float y, float z, Vector3f dest) { return dest; }
}
