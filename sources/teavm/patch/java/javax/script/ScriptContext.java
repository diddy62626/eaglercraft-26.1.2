package javax.script;

import java.io.Writer;
import java.io.Reader;
import java.util.List;

/**
 * Stub ScriptContext for TeaVM.
 */
public interface ScriptContext {
    public static final int ENGINE_SCOPE = 100;
    public static final int GLOBAL_SCOPE = 200;

    void setBindings(Bindings bindings, int scope);
    Bindings getBindings(int scope);
    void setAttribute(String name, Object value, int scope);
    Object getAttribute(String name, int scope);
    Object removeAttribute(String name, int scope);
    Object getAttribute(String name);
    int getAttributesScope(String name);
    Writer getWriter();
    Writer getErrorWriter();
    void setWriter(Writer writer);
    void setErrorWriter(Writer writer);
    Reader getReader();
    void setReader(Reader reader);
    List<Integer> getScopes();
}
