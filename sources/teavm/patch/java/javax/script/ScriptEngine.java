package javax.script;

import java.io.Reader;

/**
 * Stub ScriptEngine for TeaVM (browser-side). All operations are no-ops
 * since browser JS does not run an embedded JSR-223 engine. Log4j2's
 * ScriptManager only checks for the presence of a ScriptEngine and
 * bails out gracefully if eval() returns null.
 */
public interface ScriptEngine {
    Object eval(String script) throws ScriptException;
    Object eval(Reader reader) throws ScriptException;
    Object eval(String script, ScriptContext context) throws ScriptException;
    Object eval(Reader reader, ScriptContext context) throws ScriptException;
    void put(String key, Object value);
    Object get(String key);
    Bindings getBindings(int scope);
    void setBindings(Bindings bindings, int scope);
    Bindings createBindings();
    ScriptContext getContext();
    void setContext(ScriptContext context);
    ScriptEngineFactory getFactory();
}
