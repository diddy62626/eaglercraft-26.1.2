package com.mojang.blaze3d;

/**
 * EaglerCraft stub for TracyFrameCapture.
 * MC 26.1.2 has a static upload() method used to upload frame capture data.
 */
public interface TracyFrameCapture {
    void captureFrame();

    /**
     * MC 26.1.2: Uploads the current frame capture to the Tracy profiler.
     * Browser: no-op (no Tracy connection available).
     */
    static void upload() {
        // no-op in browser
    }

    static void capture(com.mojang.blaze3d.pipeline.RenderTarget target) {}
}
