package org.joml;

/**
 * EaglerCraft stub for org.joml.Matrix4fc interface.
 */
public interface Matrix4fc {
    float m00();
    float m01();
    float m02();
    float m03();
    float m10();
    float m11();
    float m12();
    float m13();
    float m20();
    float m21();
    float m22();
    float m23();
    float m30();
    float m31();
    float m32();
    float m33();

    default float getRowColumn(int row, int column) {
        switch (row * 4 + column) {
            case 0: return m00();
            case 1: return m01();
            case 2: return m02();
            case 3: return m03();
            case 4: return m10();
            case 5: return m11();
            case 6: return m12();
            case 7: return m13();
            case 8: return m20();
            case 9: return m21();
            case 10: return m22();
            case 11: return m23();
            case 12: return m30();
            case 13: return m31();
            case 14: return m32();
            case 15: return m33();
            default: throw new IndexOutOfBoundsException();
        }
    }
}
