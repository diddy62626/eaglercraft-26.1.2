package org.joml;

public class FrustumRayBuilder {

    public FrustumRayBuilder() {
    }

    public FrustumRayBuilder(Matrix4f m) {
        set(m);
    }

    public FrustumRayBuilder set(Matrix4f m) {
        return this;
    }

    public Vector3f origin(int corner, Vector3f dest) {
        dest.x = 0; dest.y = 0; dest.z = 0;
        return dest;
    }

    public Vector3f direction(int corner, Vector3f dest) {
        dest.x = 0; dest.y = 0; dest.z = -1;
        return dest;
    }
}
