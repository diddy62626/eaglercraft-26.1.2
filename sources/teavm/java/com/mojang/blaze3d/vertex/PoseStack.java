package com.mojang.blaze3d.vertex;

/**
 * EaglerCraft 26.1.2 browser override for com.mojang.blaze3d.vertex.PoseStack.
 * Extends systems.PoseStack and overrides last() with covariant return type.
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

    public static class Pose extends com.mojang.blaze3d.systems.PoseStack.Pose {
        public Pose() {
            super();
        }

        public Pose(com.mojang.blaze3d.systems.PoseStack.Pose parent) {
            super(parent);
        }

        @Override
        public org.joml.Matrix4f pose() {
            return new org.joml.Matrix4f();
        }

        public org.joml.Matrix3f normal() {
            return new org.joml.Matrix3f();
        }
    }
}
