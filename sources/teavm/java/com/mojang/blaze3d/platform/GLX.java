package com.mojang.blaze3d.platform;

import org.teavm.jso.JSBody;

import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;

/**
 * EaglerCraft 26.1.2 browser override for GLX.
 * WebGL2 already supports all the GL features that Minecraft's GLX checks for,
 * so most capability queries return true.
 */
public class GLX {

        /** Whether GLX has been initialized. */
        private static boolean initialized = false;

        public GLX() {
        }

        /**
         * Initializes the GL extension system. No-op in browser since
         * WebGL2 context creation already verifies feature support.
         */
        public static void initialize() {
                if (!initialized) {
                        initialized = true;
                        ClientMain.log("[GLX] Browser WebGL2 - all GL extensions assumed supported");
                }
        }

        /**
         * Returns whether we are running on macOS. Always false in browser.
         */
        public static boolean isOnMac() {
                return false;
        }

        /**
         * Returns whether framebuffer objects are supported.
         * WebGL2 natively supports FBOs.
         */
        public static boolean isFramebufferSupported() {
                return true;
        }

        /**
         * Returns the OpenGL version string.
         * WebGL2 claims compatibility with OpenGL ES 3.0 / OpenGL 4.x feature set.
         */
        public static String getOpenGLVersion() {
                return "4.6";
        }

        /**
         * Returns whether anisotropic texture filtering is supported.
         * WebGL2 supports this via the EXT_texture_filter_anisotropic extension.
         */
        public static boolean supportsTextureFiltering() {
                return true;
        }

        /**
         * Returns whether blitting framebuffers is supported.
         * WebGL2 natively supports glBlitFramebuffer.
         */
        public static boolean supportsFramebufferBlit() {
                return true;
        }

        /**
         * Returns whether multisampled framebuffers are supported.
         * WebGL2 supports this via renderbufferStorageMultisample.
         */
        public static boolean supportsFramebufferMultisample() {
                return true;
        }

        /**
         * Returns whether instanced rendering is supported.
         * WebGL2 natively supports instanced drawing.
         */
        public static boolean supportsInstancedRendering() {
                return true;
        }

        /**
         * Returns whether uniform buffer objects are supported.
         * WebGL2 natively supports UBOs.
         */
        public static boolean supportsUniformBuffers() {
                return true;
        }

        /**
         * Returns whether vertex array objects are supported.
         * WebGL2 natively supports VAOs.
         */
        public static boolean supportsVertexArrays() {
                return true;
        }

        /**
         * Returns whether transform feedback is supported.
         * WebGL2 natively supports transform feedback.
         */
        public static boolean supportsTransformFeedback() {
                return true;
        }

        /**
         * Returns whether sampler objects are supported.
         * WebGL2 natively supports sampler objects.
         */
        public static boolean supportsSamplers() {
                return true;
        }

        /**
         * Returns whether 3D textures are supported.
         * WebGL2 natively supports 3D textures.
         */
        public static boolean supportsTexture3D() {
                return true;
        }

        /**
         * Returns whether 2D texture arrays are supported.
         * WebGL2 natively supports 2D array textures.
         */
        public static boolean supportsTexture2DArray() {
                return true;
        }

        /**
         * Returns whether multiple render targets are supported.
         * WebGL2 natively supports MRT via drawBuffers.
         */
        public static boolean supportsMultipleRenderTargets() {
                return true;
        }

        /**
         * Returns whether floating-point textures are supported.
         * WebGL2 supports this with EXT_color_buffer_float.
         */
        public static boolean supportsFloatTextures() {
                return true;
        }

        /**
         * Returns whether integer textures are supported.
         * WebGL2 natively supports integer textures.
         */
        public static boolean supportsIntegerTextures() {
                return true;
        }

        /**
         * Returns whether depth textures are supported.
         * WebGL2 natively supports depth textures.
         */
        public static boolean supportsDepthTextures() {
                return true;
        }

        /**
         * Returns whether stencil textures are supported.
         * WebGL2 supports depth-stencil textures.
         */
        public static boolean supportsStencilTextures() {
                return true;
        }

        /**
         * Returns the maximum texture size.
         */
        public static int getMaxTextureSize() {
                return getMaxTextureSize0();
        }

        @JSBody(script = "var c = document.createElement('canvas'); var gl = c.getContext('webgl2');"
                + "return gl ? gl.getParameter(gl.MAX_TEXTURE_SIZE) : 4096;")
        private static native int getMaxTextureSize0();

        /**
         * Returns the maximum number of texture units.
         */
        public static int getMaxTextureUnits() {
                return getMaxTextureUnits0();
        }

        @JSBody(script = "var c = document.createElement('canvas'); var gl = c.getContext('webgl2');"
                + "return gl ? gl.getParameter(gl.MAX_TEXTURE_IMAGE_UNITS) : 16;")
        private static native int getMaxTextureUnits0();

        /**
         * Returns whether a specific GL extension is available.
         * In WebGL2, most desktop GL extensions are natively supported.
         */
        public static boolean isExtensionAvailable(String extensionName) {
                return checkExtension0(extensionName);
        }

        @JSBody(params = "name", script = ""
                + "var c = document.createElement('canvas'); var gl = c.getContext('webgl2');"
                + "if (!gl) return false;"
                + "return !!gl.getExtension(name);")
        private static native boolean checkExtension0(String name);

        /**
         * MC 26.1.2: Returns CPU info string (used for debug overlays).
         * Browser: returns a generic string since navigator.userAgent is misleading.
         */
        public static String _getCpuInfo() {
                return "Browser";
        }

    public static int glfwBool(boolean value) { return value ? 1 : 0; }
}
