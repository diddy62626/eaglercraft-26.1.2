package com.mojang.blaze3d.systems;

public class ScissorState {
    public boolean enabled = false;
    public int x, y, width, height;

    public void set(int x, int y, int width, int height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }
    public void enable() { enabled = true; }
    public void disable() { enabled = false; }
    public boolean enabled() { return enabled; }
    public int x() { return x; }
    public int y() { return y; }
    public int width() { return width; }
    public int height() { return height; }
}
