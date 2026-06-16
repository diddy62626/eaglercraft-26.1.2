package java.awt;
public class Font {
    private final String name;
    private final int style;
    private final int size;
    public static final int PLAIN = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public Font(String name, int style, int size) { this.name = name; this.style = style; this.size = size; }
    public String getName() { return name; }
    public int getStyle() { return style; }
    public int getSize() { return size; }
}