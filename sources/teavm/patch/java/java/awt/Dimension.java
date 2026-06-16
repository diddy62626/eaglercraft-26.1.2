package java.awt;
public class Dimension {
    public int width;
    public int height;
    public Dimension() { this(0, 0); }
    public Dimension(int width, int height) { this.width = width; this.height = height; }
    public Dimension(Dimension d) { this(d.width, d.height); }
}