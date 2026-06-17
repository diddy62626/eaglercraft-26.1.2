package com.mojang.blaze3d.pipeline;

import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.JSObject;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext;

/**
 * EaglerCraft 26.1.2 browser override for RenderTarget.
 * Frame buffer object wrapper that manages WebGL2 framebuffer, color texture,
 * and depth/stencil renderbuffer.
 *
 * <p>In vanilla MC, this wraps GL framebuffer objects. In the browser,
 * we use WebGL2's framebuffer API directly.</p>
 */
public class RenderTarget {

        /** Framebuffer width. */
        protected int width;

        /** Framebuffer height. */
        protected int height;

        /** Whether this target has a depth buffer. */
        protected boolean useDepth;

        /** Whether this target has a stencil buffer. */
        protected boolean useStencil;

        /** Whether this target uses multisampling. */
        protected boolean useMultisample;

        /** The WebGL2 framebuffer JSObject. */
        protected JSObject frameBuffer;

        /** The color texture JSObject. */
        protected JSObject colorTexture;

        /** The depth/stencil renderbuffer JSObject. */
        protected JSObject depthStencilBuffer;

        /** The clear color. */
        protected float clearColorR = 0.0f;
        protected float clearColorG = 0.0f;
        protected float clearColorB = 0.0f;
        protected float clearColorA = 0.0f;

        /** The integer ID for this framebuffer (for MC compatibility). */
        protected int frameBufferId;

        /** The integer ID for the color texture. */
        protected int colorTextureId;

        /** Framebuffer ID mapping. */
        private static final Map<Integer, JSObject> fbMap = new HashMap<>();
        private static final Map<Integer, JSObject> texMap = new HashMap<>();
        private static int nextFBId = 1;
        private static int nextTexId = 1;

        public RenderTarget(int width, int height, boolean useDepth, boolean useStencil) {
                this.width = width;
                this.height = height;
                this.useDepth = useDepth;
                this.useStencil = useStencil;
                this.useMultisample = false;
        }

        /**
         * Creates the framebuffer, color texture, and depth/stencil buffers.
         */
        public void create(int width, int height, boolean useDepth, boolean useStencil) {
                this.width = width;
                this.height = height;
                this.useDepth = useDepth;
                this.useStencil = useStencil;

                WebGL2RenderingContext gl = ClientMain.getWebGL2();
                if (gl == null) return;

                // Delete old resources if any
                destroyBuffers();

                // Create framebuffer
                frameBuffer = gl.createFramebuffer();
                gl.bindFramebuffer(WebGL2RenderingContext.FRAMEBUFFER, frameBuffer);

                // Create color texture
                colorTexture = gl.createTexture();
                gl.bindTexture(WebGL2RenderingContext.TEXTURE_2D, colorTexture);
                gl.texImage2D(WebGL2RenderingContext.TEXTURE_2D, 0, WebGL2RenderingContext.RGBA8,
                        width, height, 0, WebGL2RenderingContext.RGBA, WebGL2RenderingContext.UNSIGNED_BYTE, null);
                gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D, WebGL2RenderingContext.TEXTURE_MIN_FILTER,
                        WebGL2RenderingContext.NEAREST);
                gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D, WebGL2RenderingContext.TEXTURE_MAG_FILTER,
                        WebGL2RenderingContext.NEAREST);
                gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D, WebGL2RenderingContext.TEXTURE_WRAP_S,
                        WebGL2RenderingContext.CLAMP_TO_EDGE);
                gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D, WebGL2RenderingContext.TEXTURE_WRAP_T,
                        WebGL2RenderingContext.CLAMP_TO_EDGE);
                gl.framebufferTexture2D(WebGL2RenderingContext.FRAMEBUFFER, WebGL2RenderingContext.COLOR_ATTACHMENT0,
                        WebGL2RenderingContext.TEXTURE_2D, colorTexture, 0);

                // Create depth/stencil renderbuffer if needed
                if (useDepth || useStencil) {
                        depthStencilBuffer = gl.createRenderbuffer();
                        gl.bindRenderbuffer(WebGL2RenderingContext.RENDERBUFFER, depthStencilBuffer);
                        if (useDepth && useStencil) {
                                gl.renderbufferStorage(WebGL2RenderingContext.RENDERBUFFER,
                                        WebGL2RenderingContext.DEPTH24_STENCIL8, width, height);
                                gl.framebufferRenderbuffer(WebGL2RenderingContext.FRAMEBUFFER,
                                        WebGL2RenderingContext.DEPTH_STENCIL_ATTACHMENT,
                                        WebGL2RenderingContext.RENDERBUFFER, depthStencilBuffer);
                        } else if (useDepth) {
                                gl.renderbufferStorage(WebGL2RenderingContext.RENDERBUFFER,
                                        WebGL2RenderingContext.DEPTH_COMPONENT16, width, height);
                                gl.framebufferRenderbuffer(WebGL2RenderingContext.FRAMEBUFFER,
                                        WebGL2RenderingContext.DEPTH_ATTACHMENT,
                                        WebGL2RenderingContext.RENDERBUFFER, depthStencilBuffer);
                        } else {
                                gl.renderbufferStorage(WebGL2RenderingContext.RENDERBUFFER,
                                        WebGL2RenderingContext.STENCIL_INDEX8, width, height);
                                gl.framebufferRenderbuffer(WebGL2RenderingContext.FRAMEBUFFER,
                                        WebGL2RenderingContext.STENCIL_ATTACHMENT,
                                        WebGL2RenderingContext.RENDERBUFFER, depthStencilBuffer);
                        }
                }

                // Check framebuffer status
                int status = gl.checkFramebufferStatus(WebGL2RenderingContext.FRAMEBUFFER);
                if (status != WebGL2RenderingContext.FRAMEBUFFER_COMPLETE) {
                        ClientMain.error("[RenderTarget] Framebuffer incomplete: " + status);
                }

                // Unbind
                gl.bindTexture(WebGL2RenderingContext.TEXTURE_2D, null);
                gl.bindRenderbuffer(WebGL2RenderingContext.RENDERBUFFER, null);
                gl.bindFramebuffer(WebGL2RenderingContext.FRAMEBUFFER, null);

                // Assign integer IDs for MC compatibility
                frameBufferId = nextFBId++;
                colorTextureId = nextTexId++;
                fbMap.put(frameBufferId, frameBuffer);
                texMap.put(colorTextureId, colorTexture);
        }

        /**
         * Creates buffers with the current settings.
         */
        public void createBuffers(int width, int height, boolean useDepth) {
                create(width, height, useDepth, useStencil);
        }

        /**
         * Destroys all WebGL resources for this render target.
         */
        public void destroyBuffers() {
                WebGL2RenderingContext gl = ClientMain.getWebGL2();
                if (gl == null) return;

                if (frameBuffer != null) {
                        gl.deleteFramebuffer(frameBuffer);
                        fbMap.remove(frameBufferId);
                        frameBuffer = null;
                }
                if (colorTexture != null) {
                        gl.deleteTexture(colorTexture);
                        texMap.remove(colorTextureId);
                        colorTexture = null;
                }
                if (depthStencilBuffer != null) {
                        gl.deleteRenderbuffer(depthStencilBuffer);
                        depthStencilBuffer = null;
                }
        }

        /**
         * Resizes the render target.
         */
        public void resize(int width, int height) {
                create(width, height, useDepth, useStencil);
        }

        // ========== Binding ==========

        /**
         * Binds this framebuffer for writing (drawing).
         */
        public void bindWrite(boolean setViewport) {
                WebGL2RenderingContext gl = ClientMain.getWebGL2();
                if (gl == null) return;

                gl.bindFramebuffer(WebGL2RenderingContext.FRAMEBUFFER, frameBuffer);
                if (setViewport) {
                        gl.viewport(0, 0, width, height);
                }
        }

        /**
         * Binds this framebuffer for reading.
         */
        public void bindRead() {
                WebGL2RenderingContext gl = ClientMain.getWebGL2();
                if (gl == null) return;

                gl.bindFramebuffer(WebGL2RenderingContext.READ_FRAMEBUFFER, frameBuffer);
        }

        /**
         * Unbinds this framebuffer (binds the default framebuffer).
         */
        public void unbind() {
                WebGL2RenderingContext gl = ClientMain.getWebGL2();
                if (gl == null) return;

                gl.bindFramebuffer(WebGL2RenderingContext.FRAMEBUFFER, null);
        }

        /**
         * Returns whether this framebuffer is currently bound.
         */
        public boolean isBound() {
                return true; // Simplified - could check actual GL state
        }

        // ========== Clear Color ==========

        public void setClearColor(float r, float g, float b, float a) {
                clearColorR = r;
                clearColorG = g;
                clearColorB = b;
                clearColorA = a;
        }

        public float getClearColorR() {
                return clearColorR;
        }

        public float getClearColorG() {
                return clearColorG;
        }

        public float getClearColorB() {
                return clearColorB;
        }

        public float getClearColorA() {
                return clearColorA;
        }

        // ========== Accessors ==========

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }

        public int getFrameBufferId() {
                return frameBufferId;
        }

        public int getColorTextureId() {
                return colorTextureId;
        }

        public JSObject getFrameBufferObject() {
                return frameBuffer;
        }

        public JSObject getColorTextureObject() {
                return colorTexture;
        }

        public boolean isUseDepth() {
                return useDepth;
        }

        public boolean isUseStencil() {
                return useStencil;
        }

        /**
         * Binds the color texture to the given texture unit.
         */
        public void bindTexture(int textureUnit) {
                WebGL2RenderingContext gl = ClientMain.getWebGL2();
                if (gl == null) return;
                gl.activeTexture(WebGL2RenderingContext.TEXTURE0 + textureUnit);
                gl.bindTexture(WebGL2RenderingContext.TEXTURE_2D, colorTexture);
        }

        /**
         * Gets a framebuffer JSObject by integer ID.
         */
        public static JSObject getFramebufferObject(int id) {
                return fbMap.get(id);
        }

        /**
         * Gets a texture JSObject by integer ID.
         */
        public static JSObject getTextureObject(int id) {
                return texMap.get(id);
        }

        /**
         * MC 26.1.2: Blits the render target to the screen.
         * Browser: WebGL2 framebuffer is automatically presented by the browser.
         */
        public void blitToScreen() {
                // no-op in browser - WebGL2 swap buffers handled by browser
        }

    public com.mojang.blaze3d.textures.GpuTexture getColorTexture() { return new com.mojang.blaze3d.textures.GpuTexture() {}; }
    public com.mojang.blaze3d.textures.GpuTexture getDepthTexture() { return new com.mojang.blaze3d.textures.GpuTexture() {}; }

    public RenderTarget(String name, boolean useDepth) {
        // Browser stub
    }

    public com.mojang.blaze3d.textures.GpuTextureView getColorTextureView() {
        return new com.mojang.blaze3d.textures.GpuTextureView(new com.mojang.blaze3d.textures.GpuTexture() {});
    }

    public void blitAndBlendToTexture(com.mojang.blaze3d.textures.GpuTextureView view) {}
    public com.mojang.blaze3d.textures.GpuTextureView getDepthTextureView() {
        return new com.mojang.blaze3d.textures.GpuTextureView(new com.mojang.blaze3d.textures.GpuTexture() {});
    }

    public void copyDepthFrom(com.mojang.blaze3d.pipeline.RenderTarget source) {}
}
