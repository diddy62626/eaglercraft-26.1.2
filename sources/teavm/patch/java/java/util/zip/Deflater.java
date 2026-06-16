package java.util.zip;
public class Deflater {
    public static final int DEFLATED = 8;
    public static final int NO_COMPRESSION = 0;
    public static final int BEST_SPEED = 1;
    public static final int BEST_COMPRESSION = 9;
    public static final int DEFAULT_COMPRESSION = -1;
    public Deflater() {}
    public Deflater(int level) {}
    public void setInput(byte[] input) {}
    public void setInput(byte[] input, int off, int len) {}
    public int deflate(byte[] output) { return 0; }
    public int deflate(byte[] output, int off, int len) { return 0; }
    public void finish() {}
    public boolean finished() { return true; }
    public void reset() {}
    public void end() {}
}