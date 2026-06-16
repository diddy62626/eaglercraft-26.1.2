package java.lang.reflect;
public final class Field extends AccessibleObject {
    public Object get(Object obj) { return null; }
    public void set(Object obj, Object value) {}
    public int getInt(Object obj) { return 0; }
    public void setInt(Object obj, int value) {}
    public long getLong(Object obj) { return 0L; }
    public void setLong(Object obj, long value) {}
    public float getFloat(Object obj) { return 0f; }
    public void setFloat(Object obj, float value) {}
    public double getDouble(Object obj) { return 0.0; }
    public void setDouble(Object obj, double value) {}
    public boolean getBoolean(Object obj) { return false; }
    public String getName() { return ""; }
    public Class<?> getType() { return Object.class; }
    public Class<?> getDeclaringClass() { return Object.class; }
}