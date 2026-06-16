package org.joml;

public class Matrix3x2fStack extends Matrix3x2f {
    public Matrix3x2fStack() { super(); }
    public Matrix3x2fStack(int size) { super(); }
    public Matrix3x2fStack pushMatrix() { return this; }
    public Matrix3x2fStack popMatrix() { return this; }
    public int depth() { return 0; }
    public int getMaxDepth() { return 32; }
    public void clear() { identity(); }

    @Override
    public Matrix3x2fStack invert(Matrix3x2f dest) { return this; }
    @Override
    public Matrix3x2fStack invert() { return this; }
}
