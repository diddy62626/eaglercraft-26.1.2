package com.mojang.text2speech;

import ca.weblite.objc.NSObject;

public class NarratorMac extends NSObject {
    public NarratorMac() {}
    public NarratorMac(long peer) { super(peer); }

    @Override
    public com.sun.jna.Pointer getPeer() { return null; }

    public void speak(String text) {}
    public void stop() {}
    public void clear() {}
    public boolean isSpeaking() { return false; }
    public void dispose() {}
}
