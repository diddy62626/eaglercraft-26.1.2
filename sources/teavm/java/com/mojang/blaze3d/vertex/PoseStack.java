package com.mojang.blaze3d.vertex;

/**
 * EaglerCraft 26.1.2 browser override for com.mojang.blaze3d.vertex.PoseStack.
 *
 * Extends systems.PoseStack but overrides last() to return a standalone Pose
 * class (not extending systems.PoseStack.Pose) to allow covariant return type
 * for copy() and avoid Java's single-inheritance limitation.
 *
 * Note: We can't extend both systems.PoseStack (for the stack methods) AND
 * have Pose extend systems.PoseStack.Pose (for the pose methods) while also
 * overriding copy() with a different return type. So Pose is standalone.
 */
public class PoseStack extends com.mojang.blaze3d.systems.PoseStack {

    public PoseStack() {
        super();
    }

    @Override
    public Pose last() {
        return new Pose();
    }

    // Overload (not override) - parent has mulPose(Quaternionf), MC 26.1.2 calls mulPose(Quaternionfc)
    public void mulPose(org.joml.Quaternionfc q) {}
    public void mulPose(org.joml.Matrix4fc mat) {}
    public void setIdentity() {}
    public void scale(float x, float y, float z) {}
    public void translate(float x, float y, float z) {}
    public void pushPose() { super.pushPose(); }
    public void popPose() { super.popPose(); }
    public boolean isEmpty() { return false; }
    public void rotateAround(org.joml.Quaternionfc q, float x, float y, float z) {}

    /**
     * Standalone Pose class — does NOT extend systems.PoseStack.Pose.
     * This allows us to have copy() return vertex.PoseStack.Pose (covariant)
     * without clashing with the parent's copy() returning Matrix4f.
     */
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
