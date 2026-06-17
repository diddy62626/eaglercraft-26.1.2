package org.joml;

public class Vector3d implements Vector3dc {
    public double x, y, z;

    public Vector3d() {}
    public Vector3d(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public Vector3d(org.joml.Vector3fc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); }
    public Vector3d(org.joml.Vector3dc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); }

    @Override public double x() { return x; }
    @Override public double y() { return y; }
    @Override public double z() { return z; }

    public Vector3d set(double x, double y, double z) { this.x = x; this.y = y; this.z = z; return this; }
    public Vector3d set(org.joml.Vector3fc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); return this; }
    public Vector3d set(org.joml.Vector3dc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); return this; }

    public Vector3d add(org.joml.Vector3d v) { x += v.x; y += v.y; z += v.z; return this; }
    public Vector3d add(org.joml.Vector3dc v) { x += v.x(); y += v.y(); z += v.z(); return this; }
    public Vector3d sub(org.joml.Vector3d v) { x -= v.x; y -= v.y; z -= v.z; return this; }
    public Vector3d sub(org.joml.Vector3dc v) { x -= v.x(); y -= v.y(); z -= v.z(); return this; }
    public Vector3d mul(double s) { x *= s; y *= s; z *= s; return this; }
    public Vector3d mul(org.joml.Vector3dc v) { x *= v.x(); y *= v.y(); z *= v.z(); return this; }
    public Vector3d div(double s) { x /= s; y /= s; z /= s; return this; }
    public Vector3d negate() { x = -x; y = -y; z = -z; return this; }
    public Vector3d normalize() { double len = length(); if (len > 0) mul(1.0/len); return this; }
    public double length() { return java.lang.Math.sqrt(x*x + y*y + z*z); }
    public double lengthSquared() { return x*x + y*y + z*z; }
    public double dot(org.joml.Vector3d v) { return x*v.x + y*v.y + z*v.z; }
    public double dot(org.joml.Vector3dc v) { return x*v.x() + y*v.y() + z*v.z(); }
    public Vector3d cross(org.joml.Vector3d v) {
        double nx = y*v.z - z*v.y;
        double ny = z*v.x - x*v.z;
        double nz = x*v.y - y*v.x;
        x = nx; y = ny; z = nz;
        return this;
    }
    public double distance(org.joml.Vector3d v) {
        double dx = x - v.x, dy = y - v.y, dz = z - v.z;
        return java.lang.Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    public double distance(org.joml.Vector3dc v) {
        double dx = x - v.x(), dy = y - v.y(), dz = z - v.z();
        return java.lang.Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    public double distanceSquared(org.joml.Vector3dc v) {
        double dx = x - v.x(), dy = y - v.y(), dz = z - v.z();
        return dx*dx + dy*dy + dz*dz;
    }
    public double distanceSquared(double x, double y, double z) {
        double dx = this.x - x, dy = this.y - y, dz = this.z - z;
        return dx*dx + dy*dy + dz*dz;
    }
    public Vector3d zero() { x = y = z = 0; return this; }
    public Vector3d absolute() { x = java.lang.Math.abs(x); y = java.lang.Math.abs(y); z = java.lang.Math.abs(z); return this; }
}
