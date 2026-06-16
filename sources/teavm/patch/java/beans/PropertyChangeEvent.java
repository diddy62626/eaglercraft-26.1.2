package java.beans;

/**
 * TeaVM stub for java.beans.PropertyChangeEvent.
 * Used by log4j2 java.beans.PropertyChangeSupport.
 */
public class PropertyChangeEvent extends java.util.EventObject {
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;
    private Object propagationId;

    public PropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
        super(source);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() { return propertyName; }
    public Object getOldValue() { return oldValue; }
    public Object getNewValue() { return newValue; }
    public void setPropagationId(Object id) { this.propagationId = id; }
    public Object getPropagationId() { return propagationId; }
}
