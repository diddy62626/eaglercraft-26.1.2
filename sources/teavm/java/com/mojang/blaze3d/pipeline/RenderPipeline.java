package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.shaders.UniformType;

public class RenderPipeline {
    public static Builder builder(Snippet... snippets) { return new Builder(); }

    public int getSortKey() { return 0; }
    public VertexFormat getVertexFormat() { return null; }
    public VertexFormat.Mode getVertexFormatMode() { return VertexFormat.Mode.TRIANGLES; }
    public boolean isCull() { return false; }
    public void updateSortKeySeed() {}

    public static class Builder {
        public Builder withLocation(String location) { return this; }
        public Builder withVertexShader(String shader) { return this; }
        public Builder withFragmentShader(String shader) { return this; }
        public Builder withVertexFormat(VertexFormat format, com.mojang.blaze3d.vertex.VertexFormat.Mode mode) { return this; }
        public Builder withCull(boolean cull) { return this; }
        public Builder withDepthTest(String depthTest) { return this; }
        public Builder withSampler(String sampler) { return this; }
        public Builder withUniform(String name) { return this; }
        public Builder withUniform(String name, UniformType type) { return this; }
        public RenderPipeline build() { return new RenderPipeline(); }
    }

    public interface Snippet {}
}
