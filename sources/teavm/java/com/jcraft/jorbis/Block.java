package com.jcraft.jorbis;

public class Block {
    public com.jcraft.jogg.SyncState oy;
    public com.jcraft.jorbis.DspState dsp;

    public Block(com.jcraft.jorbis.DspState dsp) {
        this.dsp = dsp;
    }

    public void init(com.jcraft.jorbis.DspState dsp) { this.dsp = dsp; }
    public void clear() {}
    public int synthesis(com.jcraft.jogg.Packet packet) { return 0; }
}
