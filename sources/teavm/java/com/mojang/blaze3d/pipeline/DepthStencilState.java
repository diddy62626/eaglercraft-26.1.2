package com.mojang.blaze3d.pipeline;

public class DepthStencilState {
    public boolean depthTestEnabled = true;
    public boolean depthWriteEnabled = true;
    public String depthFunction = "LESS";
    public boolean stencilEnabled = false;
}
