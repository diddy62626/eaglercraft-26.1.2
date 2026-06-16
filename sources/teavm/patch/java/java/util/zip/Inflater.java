package java.util.zip;
public class Inflater {
    public Inflater() {}
    public Inflater(boolean nowrap) {}
    public void setInput(byte[] input) {}
    public void setInput(byte[] input, int off, int len) {}
    public int inflate(byte[] output) throws DataFormatException { return 0; }
    public int inflate(byte[] output, int off, int len) throws DataFormatException { return 0; }
    public boolean finished() { return true; }
    public int getRemaining() { return 0; }
    public void reset() {}
    public void end() {}
}