package java.awt;
public abstract class Graphics {
    public abstract void dispose();
    public void setColor(Color c) {}
    public void fillRect(int x, int y, int width, int height) {}
    public void drawString(String str, int x, int y) {}
}