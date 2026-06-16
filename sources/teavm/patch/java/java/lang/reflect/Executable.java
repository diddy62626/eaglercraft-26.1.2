package java.lang.reflect;
public abstract class Executable extends AccessibleObject {
    public abstract Class<?>[] getParameterTypes();
    public abstract int getParameterCount();
    public abstract Annotation[][] getParameterAnnotations();
}