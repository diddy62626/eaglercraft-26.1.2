package com.mojang.blaze3d.vertex;

/**
 * EaglerCraft 26.1.2 browser override for com.mojang.blaze3d.vertex.PoseStack.
 * Re-exports PoseStack from the systems package, which is where our actual
 * implementation lives. MC code imports PoseStack from the vertex package.
 *
 * NOTE: last() and mulPose() are inherited from systems.PoseStack — do not
 * redeclare them here (would cause return-type conflict with the parent's
 * Pose inner class).
 */
public class PoseStack extends com.mojang.blaze3d.systems.PoseStack {

	public PoseStack() {
		super();
	}
}
