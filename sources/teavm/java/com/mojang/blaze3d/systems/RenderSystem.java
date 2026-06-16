package com.mojang.blaze3d.systems;

import org.joml.Matrix4f;
import org.teavm.jso.JSObject;

import org.teavm.jso.JSBody;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext;
import com.mojang.blaze3d.platform.TextureUtil;

/**
 * EaglerCraft 26.1.2 browser override for RenderSystem.
 * Central rendering state manager that controls shader parameters, matrices,
 * blending, depth, and other GL state.
 *
 * <p>In the browser, there is only one thread, so all thread assertions are no-ops.
 * GL state changes are forwarded to WebGL2 via PlatformOpenGL.</p>
 */
public class RenderSystem {

        // ========== Thread Management (no-ops in browser) ==========

        private static boolean isOnRenderThread = true;
        private static boolean isOnGameThread = true;

        /**
         * Asserts we are on the render thread. No-op in browser (single thread).
         */
        public static void assertOnRenderThread() {
                // no-op - browser is single-threaded
        }

        /**
         * Asserts we are NOT on the render thread. No-op in browser.
         */
        public static void assertNotOnRenderThread() {
                // no-op
        }

        /**
         * Asserts we are on the game thread. No-op in browser.
         */
        public static void assertOnGameThread() {
                // no-op
        }

        /**
         * Asserts we are on the game thread or render thread. No-op in browser.
         */
        public static void assertOnGameThreadOrInitThread() {
                // no-op
        }

        public static boolean isOnRenderThread() {
                return true;
        }

        public static boolean isOnGameThread() {
                return true;
        }

        // ========== Shader Color ==========

        private static float shaderColorR = 1.0f;
        private static float shaderColorG = 1.0f;
        private static float shaderColorB = 1.0f;
        private static float shaderColorA = 1.0f;

        public static void setShaderColor(float r, float g, float b, float a) {
                shaderColorR = r;
                shaderColorG = g;
                shaderColorB = b;
                shaderColorA = a;
        }

        public static float getShaderColorR() {
                return shaderColorR;
        }

        public static float getShaderColorG() {
                return shaderColorG;
        }

        public static float getShaderColorB() {
                return shaderColorB;
        }

        public static float getShaderColorA() {
                return shaderColorA;
        }

        // ========== Shader Fog ==========

        private static float shaderFogColorR = 0.0f;
        private static float shaderFogColorG = 0.0f;
        private static float shaderFogColorB = 0.0f;
        private static float shaderFogColorA = 1.0f;
        private static float shaderFogStart = -1000.0f;
        private static float shaderFogEnd = 1000.0f;
        private static int shaderFogShape = 0; // 0 = linear, 1 = cylindrical, 2 = spherical
        private static boolean shaderFogEnabled = false;

        public static void setShaderFogColor(float r, float g, float b, float a) {
                shaderFogColorR = r;
                shaderFogColorG = g;
                shaderFogColorB = b;
                shaderFogColorA = a;
        }

        public static void setShaderFogStart(float start) {
                shaderFogStart = start;
        }

        public static void setShaderFogEnd(float end) {
                shaderFogEnd = end;
        }

        public static void setShaderFogShape(int shape) {
                shaderFogShape = shape;
        }

        public static void enableShaderFog() {
                shaderFogEnabled = true;
        }

        public static void disableShaderFog() {
                shaderFogEnabled = false;
        }

        public static float getShaderFogColorR() {
                return shaderFogColorR;
        }

        public static float getShaderFogColorG() {
                return shaderFogColorG;
        }

        public static float getShaderFogColorB() {
                return shaderFogColorB;
        }

        public static float getShaderFogColorA() {
                return shaderFogColorA;
        }

        public static float getShaderFogStart() {
                return shaderFogStart;
        }

        public static float getShaderFogEnd() {
                return shaderFogEnd;
        }

        public static int getShaderFogShape() {
                return shaderFogShape;
        }

        public static boolean isShaderFogEnabled() {
                return shaderFogEnabled;
        }

        // ========== Shader Game Time ==========

        private static float shaderGameTime = 0.0f;

        public static void setShaderGameTime(float time) {
                shaderGameTime = time;
        }

        public static float getShaderGameTime() {
                return shaderGameTime;
        }

        // ========== Matrices ==========

        private static final Matrix4f projectionMatrix = new Matrix4f();
        private static final Matrix4f modelViewMatrix = new Matrix4f();
        private static final Matrix4f modelViewProjectionMatrix = new Matrix4f();
        private static boolean modelViewProjectionMatrixDirty = true;

        private static int viewportX = 0;
        private static int viewportY = 0;
        private static int viewportWidth = 0;
        private static int viewportHeight = 0;

        public static Matrix4f getProjectionMatrix() {
                return projectionMatrix;
        }

        public static void setProjectionMatrix(Matrix4f matrix) {
                projectionMatrix.set(matrix);
                modelViewProjectionMatrixDirty = true;
        }

        public static Matrix4f getModelViewMatrix() {
                return modelViewMatrix;
        }

        public static void setModelViewMatrix(Matrix4f matrix) {
                modelViewMatrix.set(matrix);
                modelViewProjectionMatrixDirty = true;
        }

        public static Matrix4f getModelViewProjectionMatrix() {
                if (modelViewProjectionMatrixDirty) {
                        modelViewProjectionMatrix.set(projectionMatrix);
                        modelViewProjectionMatrix.mul(modelViewMatrix);
                        modelViewProjectionMatrixDirty = false;
                }
                return modelViewProjectionMatrix;
        }

        // ========== ModelViewStack ==========

        private static final PoseStack modelViewStack = new PoseStack();

        public static PoseStack getModelViewStack() {
                return modelViewStack;
        }

        public static void applyModelViewMatrix() {
                setModelViewMatrix(modelViewStack.last().pose());
        }

        // ========== Viewport ==========

        public static void viewport(int x, int y, int width, int height) {
                viewportX = x;
                viewportY = y;
                viewportWidth = width;
                viewportHeight = height;
                PlatformOpenGL._wglViewport(x, y, width, height);
        }

        public static int getViewportX() {
                return viewportX;
        }

        public static int getViewportY() {
                return viewportY;
        }

        public static int getViewportWidth() {
                return viewportWidth;
        }

        public static int getViewportHeight() {
                return viewportHeight;
        }

        // ========== Scissor ==========

        public static void scissor(int x, int y, int width, int height) {
                PlatformOpenGL._wglScissor(x, y, width, height);
        }

        // ========== Blending ==========

        private static boolean blendEnabled = false;

        public static void enableBlend() {
                blendEnabled = true;
                PlatformOpenGL._wglEnable(WebGL2RenderingContext.BLEND);
        }

        public static void disableBlend() {
                blendEnabled = false;
                PlatformOpenGL._wglDisable(WebGL2RenderingContext.BLEND);
        }

        public static boolean isBlendEnabled() {
                return blendEnabled;
        }

        public static void blendFunc(int srcFactor, int dstFactor) {
                PlatformOpenGL._wglBlendFunc(srcFactor, dstFactor);
        }

        public static void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
                PlatformOpenGL._wglBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
        }

        public static void defaultBlendFunc() {
                PlatformOpenGL._wglBlendFuncSeparate(
                        WebGL2RenderingContext.SRC_ALPHA,
                        WebGL2RenderingContext.ONE_MINUS_SRC_ALPHA,
                        WebGL2RenderingContext.ONE,
                        WebGL2RenderingContext.ONE_MINUS_SRC_ALPHA);
        }

        // ========== Depth ==========

        private static boolean depthTestEnabled = false;
        private static boolean depthMask = true;

        public static void enableDepthTest() {
                depthTestEnabled = true;
                PlatformOpenGL._wglEnable(WebGL2RenderingContext.DEPTH_TEST);
        }

        public static void disableDepthTest() {
                depthTestEnabled = false;
                PlatformOpenGL._wglDisable(WebGL2RenderingContext.DEPTH_TEST);
        }

        public static boolean isDepthTestEnabled() {
                return depthTestEnabled;
        }

        public static void depthFunc(int func) {
                PlatformOpenGL._wglDepthFunc(func);
        }

        public static void depthMask(boolean mask) {
                depthMask = mask;
                PlatformOpenGL._wglDepthMask(mask);
        }

        public static boolean isDepthMask() {
                return depthMask;
        }

        // ========== Stencil ==========

        private static boolean stencilTestEnabled = false;

        public static void enableStencilTest() {
                stencilTestEnabled = true;
                PlatformOpenGL._wglEnable(WebGL2RenderingContext.STENCIL_TEST);
        }

        public static void disableStencilTest() {
                stencilTestEnabled = false;
                PlatformOpenGL._wglDisable(WebGL2RenderingContext.STENCIL_TEST);
        }

        public static boolean isStencilTestEnabled() {
                return stencilTestEnabled;
        }

        public static void stencilFunc(int func, int ref, int mask) {
                PlatformOpenGL._wglStencilFunc(func, ref, mask);
        }

        public static void stencilOp(int sfail, int dpfail, int dppass) {
                PlatformOpenGL._wglStencilOp(sfail, dpfail, dppass);
        }

        public static void stencilMask(int mask) {
                PlatformOpenGL._wglStencilMask(mask);
        }

        // ========== Culling ==========

        private static boolean cullEnabled = false;
        private static int cullFace = WebGL2RenderingContext.BACK;

        public static void enableCull() {
                cullEnabled = true;
                PlatformOpenGL._wglEnable(WebGL2RenderingContext.CULL_FACE);
        }

        public static void disableCull() {
                cullEnabled = false;
                PlatformOpenGL._wglDisable(WebGL2RenderingContext.CULL_FACE);
        }

        public static boolean isCullEnabled() {
                return cullEnabled;
        }

        public static void cullFace(int mode) {
                cullFace = mode;
                PlatformOpenGL._wglCullFace(mode);
        }

        // ========== Polygon Mode / Offset ==========

        public static void polygonMode(int face, int mode) {
                // WebGL2 doesn't support glPolygonMode, no-op
        }

        public static void polygonOffset(float factor, float units) {
                PlatformOpenGL._wglPolygonOffset(factor, units);
        }

        public static void enablePolygonOffset() {
                PlatformOpenGL._wglEnable(WebGL2RenderingContext.POLYGON_OFFSET_FILL);
        }

        public static void disablePolygonOffset() {
                PlatformOpenGL._wglDisable(WebGL2RenderingContext.POLYGON_OFFSET_FILL);
        }

        // ========== Color Logic Op ==========

        public static void enableColorLogicOp() {
                // WebGL2 doesn't support logic ops, no-op
        }

        public static void disableColorLogicOp() {
                // no-op
        }

        public static void logicOp(int op) {
                // no-op
        }

        // ========== Color Mask ==========

        public static void colorMask(boolean r, boolean g, boolean b, boolean a) {
                PlatformOpenGL._wglColorMask(r, g, b, a);
        }

        // ========== Texture Binding ==========

        private static int activeTexture = 0;

        public static void activeTexture(int texture) {
                activeTexture = texture - WebGL2RenderingContext.TEXTURE0;
                PlatformOpenGL._wglActiveTexture(texture);
        }

        public static int getActiveTexture() {
                return activeTexture;
        }

        public static void bindTextureForSetup(int texture) {
                JSObject texObj = TextureUtil.getTextureObject(texture);
                if (texObj != null) {
                        PlatformOpenGL._wglBindTexture(WebGL2RenderingContext.TEXTURE_2D, texObj);
                }
        }

        // ========== Line Width ==========

        public static void lineWidth(float width) {
                PlatformOpenGL._wglLineWidth(width);
        }

        // ========== Clear ==========

        public static void clearColor(float r, float g, float b, float a) {
                PlatformOpenGL._wglClearColor(r, g, b, a);
        }

        public static void clearDepth(double depth) {
                PlatformOpenGL._wglClearDepth(depth);
        }

        public static void clearStencil(int stencil) {
                PlatformOpenGL._wglClearStencil(stencil);
        }

        public static void clear(int mask, boolean getError) {
                PlatformOpenGL._wglClear(mask);
        }

        public static void clear(int mask) {
                clear(mask, true);
        }

        // ========== Frame Flip ==========

        /**
         * Flips the frame (swaps buffers and processes events).
         * In the browser, WebGL auto-swaps so we just flush.
         */
        public static void flipFrame(int framerateLimit) {
                PlatformOpenGL._wglFlush();
        }

        // ========== Max Supported Texture Size ==========

        private static int maxSupportTextureSize = -1;

        public static int maxSupportTextureSize() {
                if (maxSupportTextureSize <= 0) {
                        maxSupportTextureSize = PlatformOpenGL._wglGetInteger(WebGL2RenderingContext.MAX_TEXTURE_SIZE);
                        if (maxSupportTextureSize <= 0) {
                                maxSupportTextureSize = 4096;
                        }
                }
                return maxSupportTextureSize;
        }

        // ========== Shader Access ==========

        private static JSObject shader = null;

        public static void setShader(JSObject shader) {
                RenderSystem.shader = shader;
        }

        public static JSObject getShader() {
                return shader;
        }

        // ========== Draw Buffer ==========

        public static void drawBuffers(int[] buffers) {
                // Forward to WebGL2 drawBuffers
                WebGL2RenderingContext gl = ClientMain.getWebGL2();
                if (gl != null && buffers != null && buffers.length > 0) {
                        drawBuffersJS(buffers);
                }
        }

        @JSBody(params = { "buffers" }, script = ""
                + "var gl = document.querySelector('canvas').getContext('webgl2');"
                + "if (gl) { gl.drawBuffers(buffers); }")
        private static native void drawBuffersJS(int[] buffers);

        // ========== Pixel Store ==========

        public static void pixelStore(int pname, int param) {
                PlatformOpenGL._wglPixelStorei(pname, param);
        }

        // ========== Read Pixels ==========

        public static void readPixels(int x, int y, int width, int height, int format, int type, long pixels) {
                // Pixel readback in browser - handled differently
        }

        // ========== Front Face ==========

        public static void frontFace(int winding) {
                PlatformOpenGL._wglFrontFace(winding);
        }

        // ========== String Info ==========

        public static String getApiDescription() {
                return "WebGL2 (EaglerCraft 26.1.2)";
        }
}
