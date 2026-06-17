package com.mojang.blaze3d.pipeline;

public class ColorTargetState {
    public String format = "RGBA8";
    public String blendState = "NONE";
    public String writeMask = "RGBA";
    public BlendFunction blendFunction;

    public ColorTargetState() {}
    public ColorTargetState(BlendFunction blendFunction) {
        this.blendFunction = blendFunction;
    }
}
