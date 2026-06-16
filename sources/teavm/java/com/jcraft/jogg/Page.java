package com.jcraft.jogg;

public class Page {
    public byte[] header;
    public int header_len;
    public byte[] body;
    public int body_len;

    public int eos() { return 0; }
    public long granulepos() { return 0L; }

    public int serialno() { return 0; }
}
