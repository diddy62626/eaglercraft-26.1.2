package org.joml;

/**
 * EaglerCraft stub for org.joml.Vector3d.
 * Uses java.lang.Math explicitly to avoid resolution to org.joml.Math.
 */
public class Vector3d {
    public double x, y, z;

    public Vector3d() {}
    public Vector3d(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public Vector3d(org.joml.Vector3fc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); }

    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }

    public Vector3d set(double x, double y, double z) { this.x = x; this.y = y; this.z = z; return this; }
    public Vector3d set(org.joml.Vector3fc v) { this.x = v.x(); this.y = v.y(); this.z = v.z(); return this; }
    public Vector3d add(org.joml.Vector3d v) { x += v.x; y += v.y; z += v.z; return this; }
    public Vector3d sub(org.joml.Vector3d v) { x -= v.x; y -= v.y; z -= v.z; return this; }
    public Vector3d mul(double s) { x *= s; y *= s; z *= s; return this; }
    public double length() { return java.lang.Math.sqrt(x*x + y*y + z*z); }
    public double lengthSquared() { return x*x + y*y + z*z; }
    public Vector3d normalize() { double len = length(); if (len > 0) mul(1.0/len); return this; }
    public double dot(org.joml.Vector3d v) { return x*v.x + y*v.y + z*v.z; }
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
}
