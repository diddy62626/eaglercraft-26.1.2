package org.lwjgl.opengl;

import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Int32Array;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL30 class.
 * Provides framebuffer, renderbuffer, VAO, and draw buffer support.
 * Forwards all calls to PlatformOpenGL (WebGL2 backend).
 */
public class GL30 {

        // ================================================================
        // Internal FBO/RBO/VAO ID mapping (int <-> JSObject)
        // ================================================================

        private static final Map<Integer, JSObject> framebufferMap = new HashMap<>();
        private static int nextFramebufferId = 1;

        private static final Map<Integer, JSObject> renderbufferMap = new HashMap<>();
        private static int nextRenderbufferId = 1;

        private static final Map<Integer, JSObject> vertexArrayMap = new HashMap<>();
        private static int nextVertexArrayId = 1;

        public static JSObject getFramebufferObject(int id) {
                return id == 0 ? null : framebufferMap.get(id);
        }

        public static JSObject getRenderbufferObject(int id) {
                return id == 0 ? null : renderbufferMap.get(id);
        }

        public static JSObject getVertexArrayObject(int id) {
                return id == 0 ? null : vertexArrayMap.get(id);
        }

        // ================================================================
        // GL Constants
        // ================================================================

        // --- Framebuffer ---
        public static final int GL_FRAMEBUFFER = 0x8D40;
        public static final int GL_READ_FRAMEBUFFER = 0x8CA8;
        public static final int GL_DRAW_FRAMEBUFFER = 0x8CA9;
        public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;
        public static final int GL_FRAMEBUFFER_UNDEFINED = 0x8219;
        public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6;
        public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7;
        public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9;
        public static final int GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD;
        public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56;

        // --- Renderbuffer ---
        public static final int GL_RENDERBUFFER = 0x8D41;
        public static final int GL_RENDERBUFFER_WIDTH = 0x8D42;
        public static final int GL_RENDERBUFFER_HEIGHT = 0x8D43;
        public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44;
        public static final int GL_RENDERBUFFER_RED_SIZE = 0x8D50;
        public static final int GL_RENDERBUFFER_GREEN_SIZE = 0x8D51;
        public static final int GL_RENDERBUFFER_BLUE_SIZE = 0x8D52;
        public static final int GL_RENDERBUFFER_ALPHA_SIZE = 0x8D53;
        public static final int GL_RENDERBUFFER_DEPTH_SIZE = 0x8D54;
        public static final int GL_RENDERBUFFER_STENCIL_SIZE = 0x8D55;
        public static final int GL_RENDERBUFFER_SAMPLES = 0x8CAB;

        // --- Attachments ---
        public static final int GL_COLOR_ATTACHMENT0 = 0x8CE0;
        public static final int GL_COLOR_ATTACHMENT1 = 0x8CE1;
        public static final int GL_COLOR_ATTACHMENT2 = 0x8CE2;
        public static final int GL_COLOR_ATTACHMENT3 = 0x8CE3;
        public static final int GL_COLOR_ATTACHMENT4 = 0x8CE4;
        public static final int GL_COLOR_ATTACHMENT5 = 0x8CE5;
        public static final int GL_COLOR_ATTACHMENT6 = 0x8CE6;
        public static final int GL_COLOR_ATTACHMENT7 = 0x8CE7;
        public static final int GL_COLOR_ATTACHMENT8 = 0x8CE8;
        public static final int GL_COLOR_ATTACHMENT9 = 0x8CE9;
        public static final int GL_COLOR_ATTACHMENT10 = 0x8CEA;
        public static final int GL_COLOR_ATTACHMENT11 = 0x8CEB;
        public static final int GL_COLOR_ATTACHMENT12 = 0x8CEC;
        public static final int GL_COLOR_ATTACHMENT13 = 0x8CED;
        public static final int GL_COLOR_ATTACHMENT14 = 0x8CEE;
        public static final int GL_COLOR_ATTACHMENT15 = 0x8CEF;
        public static final int GL_DEPTH_ATTACHMENT = 0x8D00;
        public static final int GL_STENCIL_ATTACHMENT = 0x8D20;
        public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;

        // --- Internal formats ---
        public static final int GL_R8 = 0x8229;
        public static final int GL_R16F = 0x822D;
        public static final int GL_R32F = 0x822E;
        public static final int GL_RG8 = 0x822B;
        public static final int GL_RG16F = 0x822F;
        public static final int GL_RG32F = 0x8230;
        public static final int GL_RGB8 = 0x8051;
        public static final int GL_RGB16F = 0x881B;
        public static final int GL_RGB32F = 0x8815;
        public static final int GL_RGBA8 = 0x8058;
        public static final int GL_RGBA16F = 0x881A;
        public static final int GL_RGBA32F = 0x8814;
        public static final int GL_SRGB8_ALPHA8 = 0x8C43;
        public static final int GL_DEPTH_COMPONENT16 = 0x81A5;
        public static final int GL_DEPTH_COMPONENT24 = 0x81A6;
        public static final int GL_DEPTH_COMPONENT32F = 0x8CAC;
        public static final int GL_DEPTH24_STENCIL8 = 0x88F0;
        public static final int GL_DEPTH32F_STENCIL8 = 0x8CAD;
        public static final int GL_R11F_G11F_B10F = 0x8C3A;
        public static final int GL_STENCIL_INDEX8 = 0x8D48;

        // --- Pixel types ---
        public static final int GL_HALF_FLOAT = 0x140B;
        public static final int GL_UNSIGNED_INT_24_8 = 0x84FA;
        public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8DAD;

        // --- Draw buffers ---
        public static final int GL_MAX_DRAW_BUFFERS = 0x8824;
        public static final int GL_DRAW_BUFFER0 = 0x8825;
        public static final int GL_DRAW_BUFFER1 = 0x8826;
        public static final int GL_DRAW_BUFFER2 = 0x8827;
        public static final int GL_DRAW_BUFFER3 = 0x8828;

        // --- VAO ---
        public static final int GL_VERTEX_ARRAY_BINDING = 0x85B5;

        // --- Transform feedback ---
        public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 0x8C8E;
        public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8F47;
        public static final int GL_INTERLEAVED_ATTRIBS = 0x8C8C;
        public static final int GL_SEPARATE_ATTRIBS = 0x8C8D;
        public static final int GL_RASTERIZER_DISCARD = 0x8C89;

        // --- Blit ---
        public static final int GL_NEAREST = 0x2600;
        public static final int GL_LINEAR = 0x2601;

        // --- Comparison modes ---
        public static final int GL_COMPARE_REF_TO_TEXTURE = 0x884E;

        // ================================================================
        // Framebuffer Methods
        // ================================================================

        public static int glGenFramebuffers() {
                JSObject fb = PlatformOpenGL._wglCreateFramebuffer();
                int id = nextFramebufferId++;
                framebufferMap.put(id, fb);
                return id;
        }

        public static void glGenFramebuffers(int n, int[] framebuffers, int offset) {
                for (int i = 0; i < n; i++) {
                        framebuffers[offset + i] = glGenFramebuffers();
                }
        }

        public static void glBindFramebuffer(int target, int framebuffer) {
                PlatformOpenGL._wglBindFramebuffer(target, getFramebufferObject(framebuffer));
        }

        public static void glDeleteFramebuffers(int framebuffer) {
                JSObject fb = framebufferMap.remove(framebuffer);
                if (fb != null) {
                        PlatformOpenGL._wglDeleteFramebuffer(fb);
                }
        }

        public static void glDeleteFramebuffers(int n, int[] framebuffers, int offset) {
                for (int i = 0; i < n; i++) {
                        glDeleteFramebuffers(framebuffers[offset + i]);
                }
        }

        public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
                PlatformOpenGL._wglFramebufferTexture2D(target, attachment, textarget, GL11.getTextureObject(texture), level);
        }

        public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
                PlatformOpenGL._wglFramebufferRenderbuffer(target, attachment, renderbuffertarget, getRenderbufferObject(renderbuffer));
        }

        public static int glCheckFramebufferStatus(int target) {
                return PlatformOpenGL._wglCheckFramebufferStatus(target);
        }

        /**
         * No-op: glBlitFramebuffer not exposed as a simple shim call.
         * Use PlatformOpenGL._wglBlitFramebuffer directly if needed.
         */
        public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1,
                        int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
                PlatformOpenGL._wglBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
        }

        // ================================================================
        // Renderbuffer Methods
        // ================================================================

        public static int glGenRenderbuffers() {
                JSObject rb = PlatformOpenGL._wglCreateRenderbuffer();
                int id = nextRenderbufferId++;
                renderbufferMap.put(id, rb);
                return id;
        }

        public static void glGenRenderbuffers(int n, int[] renderbuffers, int offset) {
                for (int i = 0; i < n; i++) {
                        renderbuffers[offset + i] = glGenRenderbuffers();
                }
        }

        public static void glBindRenderbuffer(int target, int renderbuffer) {
                PlatformOpenGL._wglBindRenderbuffer(target, getRenderbufferObject(renderbuffer));
        }

        public static void glDeleteRenderbuffers(int renderbuffer) {
                JSObject rb = renderbufferMap.remove(renderbuffer);
                if (rb != null) {
                        PlatformOpenGL._wglDeleteRenderbuffer(rb);
                }
        }

        public static void glDeleteRenderbuffers(int n, int[] renderbuffers, int offset) {
                for (int i = 0; i < n; i++) {
                        glDeleteRenderbuffers(renderbuffers[offset + i]);
                }
        }

        public static void glRenderbufferStorage(int target, int internalformat, int width, int height) {
                PlatformOpenGL._wglRenderbufferStorage(target, internalformat, width, height);
        }

        public static void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
                PlatformOpenGL._wglRenderbufferStorageMultisample(target, samples, internalformat, width, height);
        }

        // ================================================================
        // Draw Buffers
        // ================================================================

        public static void glDrawBuffers(int n, int[] buffers, int offset) {
                Int32Array arr = Int32Array.create(n);
                for (int i = 0; i < n; i++) {
                        arr.set(i, buffers[offset + i]);
                }
                PlatformOpenGL._wglDrawBuffers(arr);
        }

        public static void glDrawBuffers(int n, Int32Array buffers) {
                PlatformOpenGL._wglDrawBuffers(buffers);
        }

        public static void glReadBuffer(int mode) {
                PlatformOpenGL._wglReadBuffer(mode);
        }

        // ================================================================
        // Vertex Array Objects
        // ================================================================

        public static int glGenVertexArrays() {
                JSObject vao = PlatformOpenGL._wglCreateVertexArray();
                int id = nextVertexArrayId++;
                vertexArrayMap.put(id, vao);
                return id;
        }

        public static void glGenVertexArrays(int n, int[] arrays, int offset) {
                for (int i = 0; i < n; i++) {
                        arrays[offset + i] = glGenVertexArrays();
                }
        }

        public static void glBindVertexArray(int array) {
                PlatformOpenGL._wglBindVertexArray(getVertexArrayObject(array));
        }

        public static void glDeleteVertexArrays(int array) {
                JSObject vao = vertexArrayMap.remove(array);
                if (vao != null) {
                        PlatformOpenGL._wglDeleteVertexArray(vao);
                }
        }

        public static void glDeleteVertexArrays(int n, int[] arrays, int offset) {
                for (int i = 0; i < n; i++) {
                        glDeleteVertexArrays(arrays[offset + i]);
                }
        }

        // ================================================================
        // Misc Methods
        // ================================================================

        /**
         * No-op: glCopyTexSubImage2D is not directly in PlatformOpenGL (only 3D variant exists).
         */
        public static void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset,
                        int x, int y, int width, int height) {
                // Not directly exposed as _wglCopyTexSubImage2D; use _wglCopyTexSubImage3D with zoffset=0 if needed
        }

        /**
         * No-op: glGenerateMipmap is in GL11, not GL30 in this shim.
         * This method exists for API completeness.
         */
        public static void glGenerateMipmap(int target) {
                PlatformOpenGL._wglGenerateMipmap(target);
        }

        /**
         * No-op: glBeginConditionalRender not supported in WebGL2.
         */
        public static void glBeginConditionalRender(int id, int mode) {
                // Not supported in WebGL2
        }

        /**
         * No-op: glEndConditionalRender not supported in WebGL2.
         */
        public static void glEndConditionalRender() {
                // Not supported in WebGL2
        }

        /**
         * No-op: glClampColor not supported in WebGL2.
         */
        public static void glClampColor(int target, int clamp) {
                // Not supported in WebGL2
        }

        /**
         * Vertex attrib integer pointer – WebGL2: vertexAttribIPointer
         */
        public static void glVertexAttribIPointer(int index, int size, int type, int stride, long offset) {
                PlatformOpenGL._wglVertexAttribIPointer(index, size, type, stride, (int) offset);
        }

        public static void glVertexAttribIPointer(int index, int size, int type, int stride, int offset) {
                PlatformOpenGL._wglVertexAttribIPointer(index, size, type, stride, offset);
        }
}
