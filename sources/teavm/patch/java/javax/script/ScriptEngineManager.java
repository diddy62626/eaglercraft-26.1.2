package javax.script;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub ScriptEngineManager. TeaVM/browser environment doesn't have
 * a real JSR-223 engine. Returns an empty engine list so Log4j2's
 * ScriptManager short-circuits without errors.
 */
public class ScriptEngineManager {
    private static final List<ScriptEngineFactory> EMPTY = new ArrayList<>();

    public ScriptEngineManager() {}
    public ScriptEngineManager(ClassLoader loader) {}

    public Object get(String key) { return null; }
    public void put(String key, Object value) {}
    public ScriptEngine getEngineByName(String shortName) { return null; }
    public ScriptEngine getEngineByExtension(String extension) { return null; }
    public ScriptEngine getEngineByMimeType(String mimeType) { return null; }
    public List<ScriptEngineFactory> getEngineFactories() { return EMPTY; }
    public void registerEngineName(String name, ScriptEngineFactory factory) {}
    public void registerEngineExtension(String extension, ScriptEngineFactory factory) {}
    public void registerEngineMimeType(String type, ScriptEngineFactory factory) {}
    public void setBindings(Bindings bindings) {}
    public Bindings getBindings() { return null; }
}
