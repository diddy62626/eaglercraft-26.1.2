package com.mojang.blaze3d.systems;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * EaglerCraft 26.1.2 browser override for PoseStack.
 * Stack of transformation poses (model-view matrix stack).
 * Used by RenderSystem.getModelViewStack() and rendering code.
 */
public class PoseStack {

	private final Deque<Pose> poseStack = new ArrayDeque<>();

	public PoseStack() {
		poseStack.push(new Pose());
	}

	/**
	 * Pushes a new pose onto the stack (copy of current).
	 */
	public void pushPose() {
		poseStack.push(new Pose(poseStack.peek()));
	}

	/**
	 * Pops the top pose off the stack.
	 */
	public void popPose() {
		if (poseStack.size() <= 1) {
			throw new IllegalStateException("Cannot pop the last pose from the stack");
		}
		poseStack.pop();
	}

	/**
	 * Returns the current (top) pose.
	 */
	public Pose last() {
		return poseStack.peek();
	}

	/**
	 * Returns true if the stack has more than one pose.
	 */
	public boolean clear() {
		return poseStack.size() > 1;
	}

	/**
	 * Translates the current pose.
	 */
	public void translate(double x, double y, double z) {
		poseStack.peek().pose.translate((float) x, (float) y, (float) z);
	}

	/**
	 * Translates the current pose.
	 */
	public void translate(float x, float y, float z) {
		poseStack.peek().pose.translate(x, y, z);
	}

	/**
	 * Scales the current pose.
	 */
	public void scale(float x, float y, float z) {
		poseStack.peek().pose.scale(x, y, z);
	}

	/**
	 * Rotates the current pose by the given angle (radians) around the given axis.
	 */
	public void mulPose(Quaternionf quaternion) {
		poseStack.peek().pose.rotate(quaternion);
	}

	/**
	 * Multiplies the current pose by the given matrix.
	 */
	public void mulPose(Matrix4f matrix) {
		poseStack.peek().pose.mul(matrix);
	}

	/**
	 * A single pose (transformation) in the stack.
	 */
	public static class Pose {
		private final Matrix4f pose;

		public Pose() {
			this.pose = new Matrix4f();
		}

		public Pose(Pose parent) {
			this.pose = new Matrix4f(parent.pose);
		}

		public Matrix4f pose() {
			return pose;
		}

		public Matrix4f copy() {
			return new Matrix4f(pose);
		}
	}
}
