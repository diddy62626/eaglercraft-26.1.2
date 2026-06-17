package java.nio.charset;
public class CodingErrorAction {
    public static final CodingErrorAction IGNORE = new CodingErrorAction();
    public static final CodingErrorAction REPLACE = new CodingErrorAction();
    public static final CodingErrorAction REPORT = new CodingErrorAction();
}