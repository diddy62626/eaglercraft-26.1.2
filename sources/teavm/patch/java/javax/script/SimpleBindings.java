package javax.script;

import java.util.HashMap;

/**
 * Simple HashMap-based Bindings impl.
 */
public class SimpleBindings extends HashMap<String, Object> implements Bindings {
    public SimpleBindings() { super(); }
    public SimpleBindings(java.util.Map<String, Object> m) { super(m); }
}
