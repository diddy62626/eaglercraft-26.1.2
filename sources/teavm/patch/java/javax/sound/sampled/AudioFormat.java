package javax.sound.sampled;
public class AudioFormat {
    private final float sampleRate; private final int sampleSizeInBits; private final int channels;
    public AudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) { this.sampleRate = sampleRate; this.sampleSizeInBits = sampleSizeInBits; this.channels = channels; }
    public float getSampleRate() { return sampleRate; }
    public int getSampleSizeInBits() { return sampleSizeInBits; }
    public int getChannels() { return channels; }
    public static class Encoding { public static final Encoding PCM_SIGNED = new Encoding(); public static final Encoding PCM_UNSIGNED = new Encoding(); }
}