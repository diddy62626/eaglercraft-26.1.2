package com.jcraft.jorbis;

public class DspState {
    public com.jcraft.jorbis.Info vi;
    public void init() {}
    public void clear() {}
    public int synthesis_init() { return 0; }
    public int synthesis_blockin(com.jcraft.jorbis.Block block) { return 0; }
    public int synthesis_pcmout(float[][][] floats, int[] indices) { return 0; }

    public int synthesis_init(com.jcraft.jorbis.Info info) { this.vi = info; return 0; }
    public int synthesis_read(int samples) { return 0; }
}
