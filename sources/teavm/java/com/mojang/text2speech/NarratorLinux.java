package com.mojang.text2speech;

import com.sun.jna.Pointer;

/**
 * EaglerCraft shadow stub for NarratorLinux.
 * Replaces JNA-based native TTS with no-op stub.
 * Browser doesn't have Flite TTS — the Web Speech API would be used instead.
 */
public class NarratorLinux {
    private boolean available = false;

    public NarratorLinux() {
        // No Flite library in browser
    }

    public void say(String text) {
        // No-op — browser TTS handled differently
    }

    public void clear() {}
    public void destroy() {}
    public boolean isSpeaking() { return false; }
    public boolean isReady() { return false; }

    // Inner FliteLibrary stub (was JNA interface with native methods)
    public interface FliteLibrary {
        int flite_init();
        com.sun.jna.Pointer flite_synth_text(String text, com.sun.jna.Pointer voice);
        com.sun.jna.Pointer utt_wave(com.sun.jna.Pointer utterance);
        void play_wave(com.sun.jna.Pointer wave);
        void cst_wave_rescale(com.sun.jna.Pointer wave, int samplingRate);
        void delete_utterance(com.sun.jna.Pointer utterance);

        interface CmuUsKal16 {
            com.sun.jna.Pointer register_cmu_us_kal16(String voicePath);
        }
    }
}
