package java.lang.reflect;
public class AccessibleObject {
    public void setAccessible(boolean flag) {}
    public static void setAccessible(AccessibleObject[] array, boolean flag) {}
    public boolean isAccessible() { return false; }
    public <T extends java.lang.annotation.Annotation> T getAnnotation(Class<T> annotationClass) { return null; }
    public java.lang.annotation.Annotation[] getAnnotations() { return new java.lang.annotation.Annotation[0]; }
    public java.lang.annotation.Annotation[] getDeclaredAnnotations() { return new java.lang.annotation.Annotation[0]; }
}
