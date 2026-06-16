package java.awt;
public class Frame extends Window {
    public static final int ICONIFIED = 1;
    public static final int NORMAL = 0;
    private String title;
    public Frame() {}
    public Frame(String title) { this.title = title; }
    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }
    public int getExtendedState() { return NORMAL; }
}