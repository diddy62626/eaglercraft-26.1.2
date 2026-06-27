package com.mojang.datafixers.types.templates;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;
public abstract class TypeTemplate {
    public abstract TypeFamily apply(TypeFamily family);
    public abstract Type<?> apply(int index);
    public abstract int size();
    public Type<?> toSimpleType() { return apply(0); }
    public Type<?> findFieldOrType(int index, String name, Type<?> type) { return null; }
    public TypeTemplate hmap() { return this; }
    public TypeTemplate applyO() { return this; }
}
