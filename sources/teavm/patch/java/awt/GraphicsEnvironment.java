package java.awt;
public class GraphicsEnvironment {
    private static final GraphicsEnvironment INSTANCE = new GraphicsEnvironment();
    public static GraphicsEnvironment getLocalGraphicsEnvironment() { return INSTANCE; }
    public boolean isHeadless() { return true; }
}