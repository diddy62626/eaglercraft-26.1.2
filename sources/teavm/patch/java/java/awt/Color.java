package java.awt;
public class Color {
    public static final Color BLACK = new Color(0);
    public static final Color WHITE = new Color(0xFFFFFF);
    public static final Color RED = new Color(0xFF0000);
    public static final Color GREEN = new Color(0x00FF00);
    public static final Color BLUE = new Color(0x0000FF);
    private final int value;
    public Color(int rgb) { this.value = rgb; }
    public int getRGB() { return value; }
    public int getRed() { return (value >> 16) & 0xFF; }
    public int getGreen() { return (value >> 8) & 0xFF; }
    public int getBlue() { return value & 0xFF; }
}