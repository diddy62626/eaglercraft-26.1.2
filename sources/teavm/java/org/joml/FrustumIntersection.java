package org.joml;

public class FrustumIntersection {

    public FrustumIntersection() {
    }

    public FrustumIntersection(Matrix4f m) {
        set(m);
    }

    public FrustumIntersection set(Matrix4f m) {
        // In a browser, we don't need real frustum culling; just return stubs
        return this;
    }

    public boolean intersectsSphere(float x, float y, float z, float r) {
        return true;
    }

    public boolean intersectsSphere(Vector3f center, float radius) {
        return true;
    }

    public boolean intersectsAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return true;
    }

    public boolean intersectsAab(Vector3f min, Vector3f max) {
        return true;
    }

    public boolean intersectsPoint(float x, float y, float z) {
        return true;
    }

    public boolean intersectsPoint(Vector3f point) {
        return true;
    }

    public int intersectAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) { return 1; }
    public FrustumIntersection set(org.joml.Matrix4fc m) { return this; }
    public boolean testPoint(float x, float y, float z) { return true; }
}
