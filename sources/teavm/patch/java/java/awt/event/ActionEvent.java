package java.awt.event;
public class ActionEvent extends java.util.EventObject {
    public ActionEvent(Object source, int id, String command) { super(source); }
    public String getActionCommand() { return ""; }
}