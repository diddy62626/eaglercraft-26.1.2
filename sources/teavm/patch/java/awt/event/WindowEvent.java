package java.awt.event;
import java.awt.Window;
public class WindowEvent extends java.util.EventObject {
    public WindowEvent(Window source, int id) { super(source); }
    public Window getWindow() { return (Window) source; }
}