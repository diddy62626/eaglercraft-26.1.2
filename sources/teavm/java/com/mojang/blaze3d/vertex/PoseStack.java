package com.mojang.blaze3d.vertex;

/**
 * EaglerCraft 26.1.2 browser override for com.mojang.blaze3d.vertex.PoseStack.
 *
 * STANDALONE class — does NOT extend systems.PoseStack.
 * This allows Pose to be a standalone class with copy() returning
 * vertex.PoseStack.Pose (covariant return type, no parent clash).
 *
 * All stack methods (pushPose, popPose, etc.) are re-implemented here.
 */
public class PoseStack {

    private final java.util.Deque<Pose> poseStack = new java.util.ArrayDeque<>();

    public PoseStack() {
        poseStack.push(new Pose());
    }

    public Pose last() {
        return poseStack.peek();
    }

    public void pushPose() {
        poseStack.push(new Pose(poseStack.peek()));
    }

    public void popPose() {
        if (poseStack.size() <= 1) {
            throw new IllegalStateException("Cannot pop the last pose from the stack");
        }
        poseStack.pop();
    }

    public void mulPose(org.joml.Quaternionfc q) {}
    public void mulPose(org.joml.Matrix4fc mat) {}
    public void setIdentity() { poseStack.peek().setIdentity(); }
    public void scale(float x, float y, float z) {}
    public void translate(float x, float y, float z) {
        poseStack.peek().translate(x, y, z);
    }
    public boolean isEmpty() { return poseStack.size() <= 1; }
    public void rotateAround(org.joml.Quaternionfc q, float x, float y, float z) {}
    public void clear() {
        poseStack.clear();
        poseStack.push(new Pose());
    }

    /**
     * Standalone Pose class — does NOT extend systems.PoseStack.Pose.
     * This allows copy() to return vertex.PoseStack.Pose.
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

        public Pose(Pose parent) {
            this.pose = new org.joml.Matrix4f(parent.pose);
            this.normal = new org.joml.Matrix3f(parent.normal);
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

    public void translate(double x, double y, double z) { translate((float)x, (float)y, (float)z); }
}
