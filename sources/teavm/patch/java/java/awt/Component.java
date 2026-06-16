package java.awt;
public abstract class Component {
    public void repaint() {}
    public void invalidate() {}
    public void validate() {}
    public void setPreferredSize(Dimension d) {}
    public Dimension getPreferredSize() { return new Dimension(); }
}