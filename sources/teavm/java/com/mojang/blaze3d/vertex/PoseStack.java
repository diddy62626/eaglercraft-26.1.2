package com.mojang.blaze3d.vertex;

/**
 * EaglerCraft 26.1.2 browser override for com.mojang.blaze3d.vertex.PoseStack.
 * Re-exports PoseStack from the systems package, which is where our actual
 * implementation lives. MC code imports PoseStack from the vertex package.
 */
public class PoseStack extends com.mojang.blaze3d.systems.PoseStack {

	public PoseStack() {
		super();
	}

    public Pose last() { return new Pose(); }
    public void mulPose(org.joml.Quaternionfc q) {}

    public static class Pose {
        public org.joml.Matrix4f pose() { return new org.joml.Matrix4f(); }
        public org.joml.Matrix3f normal() { return new org.joml.Matrix3f(); }
    }
}
