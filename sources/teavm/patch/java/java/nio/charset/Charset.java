package java.nio.charset;
public abstract class Charset {
    private final String name;
    protected Charset(String canonicalName, String[] aliases) { this.name = canonicalName; }
    public static Charset forName(String charsetName) { return StandardCharsets.UTF_8; }
    public static Charset defaultCharset() { return StandardCharsets.UTF_8; }
    public String name() { return name; }
}