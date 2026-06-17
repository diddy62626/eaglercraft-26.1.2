package com.mojang.blaze3d.pipeline;

import java.util.Optional;

public class ColorTargetState {
    public String format = "RGBA8";
    public String blendState = "NONE";
    public String writeMask = "RGBA";
    public BlendFunction blendFunction;

    public ColorTargetState() {}
    public ColorTargetState(BlendFunction blendFunction) {
        this.blendFunction = blendFunction;
    }

    public Optional<BlendFunction> blendFunction() {
        return Optional.ofNullable(blendFunction);
    }
}
