package java.net;
public class URISyntaxException extends Exception {
    public URISyntaxException(String input, String reason) { super(reason); }
    public URISyntaxException(String input, String reason, int index) { super(reason); }
}