package javax.script;

/**
 * Stub ScriptException for TeaVM.
 */
public class ScriptException extends Exception {
    private String fileName;
    private int lineNumber;
    private int columnNumber;

    public ScriptException(String s) { super(s); }
    public ScriptException(Exception e) { super(e); }
    public ScriptException(String message, String fileName, int lineNumber) {
        super(message);
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }
    public ScriptException(String message, String fileName, int lineNumber, int columnNumber) {
        super(message);
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() { return lineNumber; }
    public int getColumnNumber() { return columnNumber; }
    public String getFileName() { return fileName; }
}
