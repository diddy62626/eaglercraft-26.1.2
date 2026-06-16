package com.mojang.blaze3d.vertex;

public class PoseStack extends com.mojang.blaze3d.systems.PoseStack {

    public PoseStack() {
        super();
    }

    @Override
    public Pose last() {
        return new Pose();
    }

    public void mulPose(org.joml.Quaternionfc q) {}
    public void mulPose(org.joml.Matrix4fc mat) {}
    public void setIdentity() {}
    public void scale(float x, float y, float z) {}
    public void translate(float x, float y, float z) {}
    public void pushPose() { super.pushPose(); }
    public void popPose() { super.popPose(); }
    public boolean isEmpty() { return false; }
    public void rotateAround(org.joml.Quaternionfc q, float x, float y, float z) {}

    // Pose is a standalone class (NOT extending systems.PoseStack.Pose) to allow
    // covariant return type for copy() and avoid Java's single-inheritance limitation.
    public static class Pose {
        private final org.joml.Matrix4f pose;
        private final org.joml.Matrix3f normal;

        public Pose() {
            this.pose = new org.joml.Matrix4f();
            this.normal = new org.joml.Matrix3f();
        }

        public Pose(org.joml.Matrix4f pose, org.joml.Matrix3f normal) {
            this.pose = pose;
            this.normal = normal;
        }

        public org.joml.Matrix4f pose() { return pose; }
        public org.joml.Matrix3f normal() { return normal; }

        public Pose copy() { return new Pose(new org.joml.Matrix4f(pose), new org.joml.Matrix3f(normal)); }
        public void mulPose(org.joml.Matrix4fc mat) {}
        public void scale(float x, float y, float z) {}
        public void set(Pose other) {}
        public void setIdentity() { pose.identity(); normal.identity(); }
        public org.joml.Vector3f transformNormal(org.joml.Vector3fc v, org.joml.Vector3f dest) { return dest; }
        public org.joml.Vector3f transformNormal(float x, float y, float z, org.joml.Vector3f dest) { return dest; }
        public org.joml.Matrix4f translate(float x, float y, float z) { return new org.joml.Matrix4f(); }
        public void rotate(org.joml.Quaternionfc q) {}
    }
}
