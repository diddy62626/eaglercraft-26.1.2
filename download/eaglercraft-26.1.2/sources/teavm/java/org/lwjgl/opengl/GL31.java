package org.lwjgl.opengl;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL31 class.
 * Provides uniform buffer object (UBO) support used by MC's blaze3d.
 */
public class GL31 {

        // ================================================================
        // GL Constants
        // ================================================================

        public static final int GL_UNIFORM_BUFFER = 0x8A11;
        public static final int GL_UNIFORM_BUFFER_BINDING = 0x8A28;
        public static final int GL_UNIFORM_BUFFER_START = 0x8A29;
        public static final int GL_UNIFORM_BUFFER_SIZE = 0x8A2A;
        public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 0x8A2B;
        public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 0x8A2C;
        public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8A2D;
        public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 0x8A2E;
        public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 0x8A2F;
        public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 0x8A33;
        public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8A31;
        public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8A33;
        public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8A34;

        // --- Uniform block parameters ---
        public static final int GL_UNIFORM_BLOCK_BINDING = 0x8A3F;
        public static final int GL_UNIFORM_BLOCK_DATA_SIZE = 0x8A40;
        public static final int GL_UNIFORM_BLOCK_NAME_LENGTH = 0x8A41;
        public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 0x8A42;
        public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 0x8A43;
        public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 0x8A44;
        public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 0x8A46;
        public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 0x8A45;

        // --- Uniform parameters ---
        public static final int GL_UNIFORM_TYPE = 0x8A37;
        public static final int GL_UNIFORM_SIZE = 0x8A38;
        public static final int GL_UNIFORM_NAME_LENGTH = 0x8A39;
        public static final int GL_UNIFORM_BLOCK_INDEX = 0x8A3A;
        public static final int GL_UNIFORM_OFFSET = 0x8A3B;
        public static final int GL_UNIFORM_ARRAY_STRIDE = 0x8A3C;
        public static final int GL_UNIFORM_MATRIX_STRIDE = 0x8A3D;
        public static final int GL_UNIFORM_IS_ROW_MAJOR = 0x8A3E;

        // --- Texture buffer ---
        public static final int GL_TEXTURE_BUFFER = 0x8C2A;
        public static final int GL_MAX_TEXTURE_BUFFER_SIZE = 0x8C2B;
        public static final int GL_TEXTURE_BINDING_BUFFER = 0x8C2C;
        public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING = 0x8C2D;

        // --- Redsnorm ---
        public static final int GL_R8_SNORM = 0x8F94;
        public static final int GL_RG8_SNORM = 0x8F95;
        public static final int GL_RGB8_SNORM = 0x8F96;
        public static final int GL_RGBA8_SNORM = 0x8F97;

        // --- Copy buffer ---
        public static final int GL_COPY_READ_BUFFER = 0x8F36;
        public static final int GL_COPY_WRITE_BUFFER = 0x8F37;

        // ================================================================
        // GL Methods
        // ================================================================

        public static int glGetUniformBlockIndex(int program, String uniformBlockName) {
                return PlatformOpenGL._wglGetUniformBlockIndex(GL20.getProgramObject(program), uniformBlockName);
        }

        public static void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
                PlatformOpenGL._wglUniformBlockBinding(GL20.getProgramObject(program), uniformBlockIndex, uniformBlockBinding);
        }

        public static void glBindBufferRange(int target, int index, int buffer, int offset, int size) {
                PlatformOpenGL._wglBindBufferRange(target, index, GL15.getBufferObject(buffer), offset, size);
        }

        public static void glBindBufferBase(int target, int index, int buffer) {
                PlatformOpenGL._wglBindBufferBase(target, index, GL15.getBufferObject(buffer));
        }

        /**
         * Query active uniform block parameter.
         * Returns 0 as default; not directly exposed in PlatformOpenGL.
         */
        public static void glGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, int[] params, int offset) {
                // Not directly exposed in PlatformOpenGL
                if (params != null && params.length > offset) {
                        params[offset] = 0;
                }
        }

        /**
         * NIO IntBuffer version.
         */
        public static void glGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, Object params) {
                // TODO: NIO IntBuffer shim
        }

        /**
         * Query active uniform parameters.
         * Returns 0s as default; not directly exposed in PlatformOpenGL.
         */
        public static void glGetActiveUniformsiv(int program, int uniformCount, int[] uniformIndices, int uniformIndicesOffset,
                        int pname, int[] params, int paramsOffset) {
                // Not directly exposed in PlatformOpenGL
                for (int i = 0; i < uniformCount && (paramsOffset + i) < params.length; i++) {
                        params[paramsOffset + i] = 0;
                }
        }

        /**
         * NIO IntBuffer version.
         */
        public static void glGetActiveUniformsiv(int program, int uniformCount, Object uniformIndices, int pname, Object params) {
                // TODO: NIO IntBuffer shim
        }

        /**
         * Copy buffer sub-data.
         */
        public static void glCopyBufferSubData(int readTarget, int writeTarget, int readOffset, int writeOffset, int size) {
                PlatformOpenGL._wglCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
        }
}
