package java.util;
public class MissingResourceException extends RuntimeException {
    public MissingResourceException(String s, String className, String key) { super(s); }
}