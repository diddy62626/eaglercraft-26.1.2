package com.mojang.datafixers.schemas;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.HashMap;
public class Schema {
    protected final int versionKey;
    protected final Map<String, TypeTemplate> typeTemplates = new HashMap<>();
    public Schema(int versionKey, Schema parent) {
        this.versionKey = versionKey;
    }
    public int getVersionKey() { return versionKey; }
    public TypeTemplate getTemplate(String name) { return typeTemplates.get(name); }
    public void registerTemplate(String name, TypeTemplate template) { typeTemplates.put(name, template); }
    public Map<String, TypeTemplate> getTypeTemplates() { return typeTemplates; }
    public com.mojang.datafixers.types.Type<?> getChoiceType(com.mojang.datafixers.DSL.TypeReference type, String name) { return null; }
    public com.mojang.datafixers.types.Type<?> getType(com.mojang.datafixers.DSL.TypeReference type) { return null; }
}
