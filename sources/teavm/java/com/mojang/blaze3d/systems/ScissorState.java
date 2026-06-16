package com.mojang.blaze3d.systems;

public class ScissorState {
    public boolean enabled = false;
    public int x, y, width, height;
    public void set(int x, int y, int width, int height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }
    public void enable() { enabled = true; }
    public void disable() { enabled = false; }
}
