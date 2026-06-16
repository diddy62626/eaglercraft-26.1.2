package org.joml;

public class Matrix4fStack extends Matrix4f {
    public Matrix4fStack() { super(); }
    public Matrix4fStack(int initialCapacity) { super(); }
    public Matrix4fStack pushMatrix() { return this; }
    public Matrix4fStack popMatrix() { return this; }
    public int depth() { return 0; }
    public int getMaxDepth() { return 32; }
    public void clear() { identity(); }

    public Matrix4f rotationX(float angle) { return this; }
}
