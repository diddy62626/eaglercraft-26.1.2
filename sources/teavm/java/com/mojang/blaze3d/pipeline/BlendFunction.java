package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.platform.DestFactor;

public class BlendFunction {
    public static final BlendFunction NONE = new BlendFunction();
    public static final BlendFunction TRANSPARENT = new BlendFunction();
    public static final BlendFunction TRANSLUCENT = new BlendFunction();
    public static final BlendFunction ADD = new BlendFunction();
    public static final BlendFunction ADDITIVE = new BlendFunction();
    public static final BlendFunction SUBTRACT = new BlendFunction();
    public static final BlendFunction MULTIPLY = new BlendFunction();
    public static final BlendFunction SCREEN = new BlendFunction();
    public static final BlendFunction OVERLAY = new BlendFunction();
    public static final BlendFunction GLINT = new BlendFunction();

    public BlendFunction() {}

    public BlendFunction(SourceFactor srcColor, DestFactor dstColor, SourceFactor srcAlpha, DestFactor dstAlpha) {
        // Store factors for WebGL blend configuration
    }
}
