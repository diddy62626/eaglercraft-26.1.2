package java.lang.invoke;
public final class LambdaMetafactory {
    public static CallSite metafactory(MethodHandles.Lookup caller, String invokedName,
            MethodType invokedType, MethodType samMethodType,
            MethodHandle implMethod, MethodType instantiatedMethodType) {
        return null;
    }
    public static CallSite altMetafactory(MethodHandles.Lookup caller, String invokedName,
            MethodType invokedType, Object... args) {
        return null;
    }
}