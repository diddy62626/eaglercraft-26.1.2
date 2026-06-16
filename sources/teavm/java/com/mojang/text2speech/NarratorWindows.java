package com.mojang.text2speech;

import com.sun.jna.Pointer;

public class NarratorWindows extends com.sun.jna.platform.win32.COM.Unknown {
    public NarratorWindows() {}

    @Override
    public Pointer getPointer() { return null; }

    public void speak(String text) {}
    public void stop() {}
    public void clear() {}
    public boolean isSpeaking() { return false; }
    public void dispose() {}
}
