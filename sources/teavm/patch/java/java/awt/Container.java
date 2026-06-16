package java.awt;
public class Container extends Component {
    public void add(Component comp) {}
    public void add(Component comp, Object constraints) {}
    public void setLayout(LayoutManager mgr) {}
    public Component add(String name, Component comp) { return comp; }
}