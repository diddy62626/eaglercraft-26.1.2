package org.joml;

public interface Vector3dc {
    double x();
    double y();
    double z();

    default double length() { return java.lang.Math.sqrt(x()*x() + y()*y() + z()*z()); }
    default double lengthSquared() { return x()*x() + y()*y() + z()*z(); }
    default double distance(Vector3dc v) {
        double dx = x() - v.x(), dy = y() - v.y(), dz = z() - v.z();
        return java.lang.Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    default double distanceSquared(Vector3dc v) {
        double dx = x() - v.x(), dy = y() - v.y(), dz = z() - v.z();
        return dx*dx + dy*dy + dz*dz;
    }
    default double distanceSquared(double x, double y, double z) {
        double dx = this.x() - x, dy = this.y() - y, dz = this.z() - z;
        return dx*dx + dy*dy + dz*dz;
    }
    default double dot(Vector3dc v) { return x()*v.x() + y()*v.y() + z()*v.z(); }
    default Vector3d add(Vector3dc v, Vector3d dest) { return dest; }
    default Vector3d sub(Vector3dc v, Vector3d dest) { return dest; }
}
