package com.mojang.blaze3d.pipeline;

import java.util.Optional;

public class ColorTargetState {
    public String format = "RGBA8";
    public String blendState = "NONE";
    public String writeMask = "RGBA";
    public BlendFunction blendFunction;
    public int writeMaskBits = 0xF;

    public ColorTargetState() {}
    public ColorTargetState(BlendFunction blendFunction) {
        this.blendFunction = blendFunction;
    }
    public ColorTargetState(Optional<BlendFunction> blendFunction, int writeMaskBits) {
        this.blendFunction = blendFunction.orElse(null);
        this.writeMaskBits = writeMaskBits;
    }

    public Optional<BlendFunction> blendFunction() {
        return Optional.ofNullable(blendFunction);
    }
}
