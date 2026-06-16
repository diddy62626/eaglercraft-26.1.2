package com.mojang.blaze3d.systems;

public class RenderSystem {
    public static class AutoStorageIndexBuffer {
        public void upload(int vertexCount) {}
        public boolean has(int vertexCount) { return false; }
        public int indexBufferObject() { return 0; }
    }
}
