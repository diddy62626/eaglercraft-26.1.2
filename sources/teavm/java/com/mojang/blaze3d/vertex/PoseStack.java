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

    public static class Pose extends com.mojang.blaze3d.systems.PoseStack.Pose {
        public Pose() { super(); }
        public Pose(com.mojang.blaze3d.systems.PoseStack.Pose parent) { super(parent); }

        @Override
        public org.joml.Matrix4f pose() { return new org.joml.Matrix4f(); }

        public org.joml.Matrix3f normal() { return new org.joml.Matrix3f(); }

        public com.mojang.blaze3d.vertex.PoseStack.Pose copy() { return new com.mojang.blaze3d.vertex.PoseStack.Pose(); }
        public void mulPose(org.joml.Matrix4fc mat) {}
        public void scale(float x, float y, float z) {}
        public void set(com.mojang.blaze3d.vertex.PoseStack.Pose other) {}
        public void setIdentity() {}
        public org.joml.Vector3f transformNormal(org.joml.Vector3fc v, org.joml.Vector3f dest) { return dest; }
        public org.joml.Matrix4f translate(float x, float y, float z) { return new org.joml.Matrix4f(); }
    }
}
