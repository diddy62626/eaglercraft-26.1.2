package java.time.format;
public class DateTimeParseException extends java.time.DateTimeException {
    public DateTimeParseException(String message, CharSequence parsedData, int errorIndex) { super(message); }
}