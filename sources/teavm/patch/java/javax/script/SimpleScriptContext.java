package javax.script;

import java.io.Reader;
import java.io.Writer;
import java.io.StringWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default ScriptContext impl for TeaVM.
 */
public class SimpleScriptContext implements ScriptContext {
    private Bindings engineScope;
    private Bindings globalScope;
    private Writer writer = new StringWriter();
    private Writer errorWriter = new StringWriter();
    private Reader reader = new StringReader("");

    public SimpleScriptContext() {
        engineScope = new SimpleBindings();
        globalScope = null;
    }

    @Override public void setBindings(Bindings bindings, int scope) {
        if (scope == ENGINE_SCOPE) engineScope = bindings;
        else if (scope == GLOBAL_SCOPE) globalScope = bindings;
        else throw new IllegalArgumentException("Invalid scope: " + scope);
    }

    @Override public Bindings getBindings(int scope) {
        if (scope == ENGINE_SCOPE) return engineScope;
        if (scope == GLOBAL_SCOPE) return globalScope;
        throw new IllegalArgumentException("Invalid scope: " + scope);
    }

    @Override public void setAttribute(String name, Object value, int scope) {
        Bindings b = getBindings(scope);
        if (b != null) b.put(name, value);
    }

    @Override public Object getAttribute(String name, int scope) {
        Bindings b = getBindings(scope);
        return b != null ? b.get(name) : null;
    }

    @Override public Object removeAttribute(String name, int scope) {
        Bindings b = getBindings(scope);
        return b != null ? b.remove(name) : null;
    }

    @Override public Object getAttribute(String name) {
        if (engineScope != null && engineScope.containsKey(name)) return engineScope.get(name);
        if (globalScope != null && globalScope.containsKey(name)) return globalScope.get(name);
        return null;
    }

    @Override public int getAttributesScope(String name) {
        if (engineScope != null && engineScope.containsKey(name)) return ENGINE_SCOPE;
        if (globalScope != null && globalScope.containsKey(name)) return GLOBAL_SCOPE;
        return -1;
    }

    @Override public Writer getWriter() { return writer; }
    @Override public Writer getErrorWriter() { return errorWriter; }
    @Override public void setWriter(Writer writer) { this.writer = writer; }
    @Override public void setErrorWriter(Writer writer) { this.errorWriter = writer; }
    @Override public Reader getReader() { return reader; }
    @Override public void setReader(Reader reader) { this.reader = reader; }

    @Override public List<Integer> getScopes() {
        List<Integer> scopes = new ArrayList<>();
        scopes.add(ENGINE_SCOPE);
        scopes.add(GLOBAL_SCOPE);
        return scopes;
    }
}
