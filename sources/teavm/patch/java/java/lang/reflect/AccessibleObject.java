package java.lang.reflect;
public class AccessibleObject {
    public void setAccessible(boolean flag) {}
    public static void setAccessible(AccessibleObject[] array, boolean flag) {}
    public boolean isAccessible() { return false; }
    public Annotation getAnnotation(Class<? extends Annotation> annotationClass) { return null; }
    public Annotation[] getAnnotations() { return new Annotation[0]; }
    public Annotation[] getDeclaredAnnotations() { return new Annotation[0]; }
}