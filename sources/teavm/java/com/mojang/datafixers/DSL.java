package com.mojang.datafixers;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.families.TypeFamily;
import java.util.List;
import java.util.Collections;
public class DSL {
    public interface TypeReference { String typeName(); }
    public static class ConstTypeTemplate extends TypeTemplate {
        private final Type<?> type;
        public ConstTypeTemplate(Type<?> type) { this.type = type; }
        public TypeFamily apply(TypeFamily family) { return family; }
        public Type<?> apply(int index) { return type; }
        public int size() { return 1; }
    }
    public static class AndTypeTemplate extends TypeTemplate {
        private final TypeTemplate t1, t2;
        public AndTypeTemplate(TypeTemplate t1, TypeTemplate t2) { this.t1 = t1; this.t2 = t2; }
        public TypeFamily apply(TypeFamily family) { return family; }
        public Type<?> apply(int index) { return t1.apply(index); }
        public int size() { return Math.max(t1.size(), t2.size()); }
    }
    public static class EmptyTypeTemplate extends TypeTemplate {
        public TypeFamily apply(TypeFamily family) { return family; }
        public Type<?> apply(int index) { return null; }
        public int size() { return 0; }
    }
    private static final TypeTemplate EMPTY = new EmptyTypeTemplate();
    public static TypeTemplate constType(Type<?> type) { return new ConstTypeTemplate(type); }
    public static TypeTemplate and(TypeTemplate t1, TypeTemplate t2) { return new AndTypeTemplate(t1, t2); }
    public static TypeTemplate compoundList(TypeTemplate t1, TypeTemplate t2) { return and(t1, t2); }
    public static TypeTemplate compoundList(Type<?> t1, Type<?> t2) { return and(constType(t1), constType(t2)); }
    public static TypeTemplate remainder() { return EMPTY; }
    public static Type<?> string() { return null; }
    // Stub methods called by DFU's Schema.<init> via buildTypes()
    public static TypeTemplate check(String name, int version, TypeTemplate template) { return template; }
    public static TypeTemplate named(String name, TypeTemplate template) { return template; }
    public static TypeTemplate or(TypeTemplate t1, TypeTemplate t2) { return and(t1, t2); }
    public int size() { return 0; }
    public Object get(int index) { return null; }
    public List<?> subList(int from, int to) { return Collections.emptyList(); }
}
