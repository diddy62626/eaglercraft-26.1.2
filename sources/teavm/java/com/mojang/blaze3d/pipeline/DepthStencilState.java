package com.mojang.blaze3d.pipeline;

public class DepthStencilState {
    public static final DepthStencilState DEFAULT = new DepthStencilState();
    public static final DepthStencilState NO_DEPTH = new DepthStencilState();

    public boolean depthTestEnabled = true;
    public boolean depthWriteEnabled = true;
    public String depthFunction = "LESS";
    public boolean stencilEnabled = false;
    public float stencilFrontRef = 0;
    public float stencilBackRef = 0;

    public DepthStencilState() {}

    public DepthStencilState(com.mojang.blaze3d.platform.CompareOp depthFunc, boolean depthWrite) {
        this.depthTestEnabled = depthFunc != null;
        this.depthWriteEnabled = depthWrite;
        this.depthFunction = depthFunc != null ? depthFunc.name() : "LESS";
    }

    public DepthStencilState(com.mojang.blaze3d.platform.CompareOp depthFunc, boolean depthWrite, float stencilFrontRef, float stencilBackRef) {
        this(depthFunc, depthWrite);
        this.stencilFrontRef = stencilFrontRef;
        this.stencilBackRef = stencilBackRef;
        this.stencilEnabled = true;
    }
}
