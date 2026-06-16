package javax.swing;
public class JFrame extends java.awt.Frame implements WindowConstants {
    public static final int EXIT_ON_CLOSE = 3;
    private int defaultCloseOperation = HIDE_ON_CLOSE;
    public JFrame() {}
    public JFrame(String title) { super(title); }
    public void setDefaultCloseOperation(int operation) { defaultCloseOperation = operation; }
    public int getDefaultCloseOperation() { return defaultCloseOperation; }
    public java.awt.Container getContentPane() { return new java.awt.Container(); }
}