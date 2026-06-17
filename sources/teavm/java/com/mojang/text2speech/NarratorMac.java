package com.mojang.text2speech;

import ca.weblite.objc.NSObject;

/**
 * EaglerCraft stub for NarratorMac.
 *
 * The real NarratorMac extends ca.weblite.objc.NSObject to call macOS
 * NSSpeechSynthesizer. In the browser there's no Cocoa, so this is a
 * no-op stub that just inherits NSObject's API.
 *
 * Note: We don't override getPeer() because NSObject.getPeer() returns
 * long and we can't change the return type to com.sun.jna.Pointer.
 */
public class NarratorMac extends NSObject {
    public NarratorMac() {}
    public NarratorMac(long peer) { super(peer); }

    public void speak(String text) {}
    public void stop() {}
    public void clear() {}
    public boolean isSpeaking() { return false; }
    public void dispose() {}
}
