package org.lwjgl.opengl;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL21 class.
 * Provides uniform block binding methods used by MC's blaze3d.
 */
public class GL21 {

        // ================================================================
        // GL Constants
        // ================================================================

        public static final int GL_UNIFORM_BUFFER = 0x8A11;
        public static final int GL_UNIFORM_BUFFER_BINDING = 0x8A28;
        public static final int GL_UNIFORM_BUFFER_START = 0x8A29;
        public static final int GL_UNIFORM_BUFFER_SIZE = 0x8A2A;
        public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 0x8A2B;
        public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8A2D;
        public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 0x8A2E;
        public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 0x8A2F;
        public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 0x8A33;
        public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8A31;
        public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8A33;
        public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8A34;

        // ================================================================
        // GL Methods
        // ================================================================

        public static int glGetUniformBlockIndex(int program, String uniformBlockName) {
                return PlatformOpenGL._wglGetUniformBlockIndex(GL20.getProgramObject(program), uniformBlockName);
        }

        public static void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
                PlatformOpenGL._wglUniformBlockBinding(GL20.getProgramObject(program), uniformBlockIndex, uniformBlockBinding);
        }

        /**
         * Query active uniform block parameter.
         * Returns 0 as default; PlatformOpenGL does not expose this directly.
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
}
