package javax.script;

import java.util.List;

/**
 * Stub ScriptEngineFactory for TeaVM.
 */
public interface ScriptEngineFactory {
    String getEngineName();
    String getEngineVersion();
    List<String> getExtensions();
    List<String> getMimeTypes();
    List<String> getNames();
    String getLanguageName();
    String getLanguageVersion();
    Object getParameter(String key);
    String getMethodCallSyntax(String obj, String m, String... args);
    String getOutputStatement(String toDisplay);
    String getProgram(String... statements);
    ScriptEngine getScriptEngine();
}
