package net.lax1dude.eaglercraft.v2_6.teavm_plugin;

import org.teavm.model.*;
import org.teavm.model.instructions.*;
import org.teavm.vm.spi.TeaVMHost;

import java.util.*;

/**
 * Transformer that adds missing methods to existing java.base classes.
 *
 * Each entry specifies:
 * - className: the fully-qualified class name (e.g., "java.lang.Runtime")
 * - methodName: the method name
 * - returnType: the return type (using ValueType.parse)
 * - paramTypes: the parameter types
 * - defaultValue: a default return value (Long, Integer, Boolean, Float, Double, null)
 *
 * The transformer adds the method if it doesn't exist. The method body just
 * returns the default value (or is a no-op for void).
 */
public class MissingMethodTransformer implements ClassHolderTransformer {

    /** All missing methods to add, indexed by class name. */
    private static final Map<String, List<MethodSpec>> METHODS = new HashMap<>();

    static {
        // ===== java.lang.Runtime =====
        add("java.lang.Runtime", "maxMemory", "long", new String[0], 512L * 1024 * 1024);
        add("java.lang.Runtime", "addShutdownHook", "void", new String[] {"java.lang.Thread"}, null);
        add("java.lang.Runtime", "removeShutdownHook", "boolean", new String[] {"java.lang.Thread"}, false);

        // ===== java.lang.System =====
        add("java.lang.System", "getenv", "java.util.Map", new String[0], null);

        // ===== java.lang.Thread =====
        add("java.lang.Thread", "setContextClassLoader", "void", new String[] {"java.lang.ClassLoader"}, null);

        // ===== java.lang.Class =====
        add("java.lang.Class", "getGenericInterfaces", "[Ljava.lang.reflect.Type;", new String[0], null);
        add("java.lang.Class", "getGenericSuperclass", "java.lang.reflect.Type", new String[0], null);
        add("java.lang.Class", "getResource", "java.net.URL", new String[] {"java.lang.String"}, null);
        add("java.lang.Class", "getSigners", "[Ljava.lang.Object;", new String[0], null);
        add("java.lang.Class", "isAnonymousClass", "boolean", new String[0], false);

        // ===== java.lang.ClassLoader =====
        add("java.lang.ClassLoader", "getResource", "java.net.URL", new String[] {"java.lang.String"}, null);
        add("java.lang.ClassLoader", "getResources", "java.util.Enumeration", new String[] {"java.lang.String"}, null);
        add("java.lang.ClassLoader", "loadClass", "java.lang.Class", new String[] {"java.lang.String"}, null);

        // ===== java.lang.Integer =====
        add("java.lang.Integer", "parseUnsignedInt", "int", new String[] {"java.lang.String", "int"}, 0);

        // ===== java.lang.Long =====
        add("java.lang.Long", "parseUnsignedLong", "long", new String[] {"java.lang.String", "int"}, 0L);

        // ===== java.lang.Character =====
        add("java.lang.Character", "codePointOf", "int", new String[] {"java.lang.String"}, 0);
        add("java.lang.Character", "toString", "java.lang.String", new String[] {"int"}, null);

        // ===== java.util.UUID =====
        add("java.util.UUID", "getMostSignificantBits", "long", new String[0], 0L);
        add("java.util.UUID", "nameUUIDFromBytes", "java.util.UUID", new String[] {"[B"}, null);
        // UUID constructor (long, long) - skip for now, complex

        // ===== java.util.Date =====
        add("java.util.Date", "toInstant", "java.time.Instant", new String[0], null);

        // ===== java.io.File =====
        add("java.io.File", "toPath", "java.nio.file.Path", new String[0], null);

        // ===== java.nio.file.Files =====
        add("java.nio.file.Files", "getFileStore", "java.nio.file.FileStore", new String[] {"java.nio.file.Path"}, null);

        // ===== java.util.concurrent.ConcurrentHashMap =====
        add("java.util.concurrent.ConcurrentHashMap", "newKeySet", "java.util.Set", new String[0], null);

        // ===== java.util.stream.StreamSupport =====
        add("java.util.stream.StreamSupport", "intStream", "java.util.stream.IntStream",
            new String[] {"java.util.Spliterator$OfInt", "boolean"}, null);
        add("java.util.stream.StreamSupport", "longStream", "java.util.stream.LongStream",
            new String[] {"java.util.Spliterator$OfLong", "boolean"}, null);

        // ===== java.lang.StackWalker =====
        // StackWalker.getInstance(Set, int) - complex, skip
        // StackWalker$StackFrame.getDeclaringClass() - inner class, complex
    }

    private static void add(String className, String methodName, String returnType, String[] paramTypes, Object defaultValue) {
        METHODS.computeIfAbsent(className, k -> new ArrayList<>())
               .add(new MethodSpec(methodName, returnType, paramTypes, defaultValue));
    }

    @Override
    public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
        List<MethodSpec> specs = METHODS.get(cls.getName());
        if (specs == null) return;

        for (MethodSpec spec : specs) {
            ValueType returnType = ValueType.parse(spec.returnType);
            if (returnType == null) {
                // Could not parse, skip
                continue;
            }
            ValueType[] params = new ValueType[spec.paramTypes.length];
            boolean valid = true;
            for (int i = 0; i < spec.paramTypes.length; i++) {
                params[i] = ValueType.parse(spec.paramTypes[i]);
                if (params[i] == null) { valid = false; break; }
            }
            if (!valid) continue;

            MethodDescriptor desc = new MethodDescriptor(spec.methodName, params, returnType);
            if (cls.getMethod(desc) != null) continue;

            MethodHolder m = new MethodHolder(desc);
            Program program = createProgram(returnType, params, spec.defaultValue);
            m.setProgram(program);
            m.getModifiers().add(org.teavm.model.Modifier.PUBLIC);
            if (cls.getName().equals("java.lang.System") || cls.getName().equals("java.util.UUID") ||
                cls.getName().equals("java.util.concurrent.ConcurrentHashMap") ||
                cls.getName().equals("java.util.stream.StreamSupport") ||
                cls.getName().equals("java.nio.file.Files")) {
                m.getModifiers().add(org.teavm.model.Modifier.STATIC);
            }
            cls.addMethod(m);
        }
    }

    private Program createProgram(ValueType returnType, ValueType[] params, Object defaultValue) {
        Program program = new Program();
        // Variable 0 = this (for instance methods) or unused (for static)
        // Variables 1..N = params
        // Variable N+1 = return value
        int totalVars = 1 + params.length + 1;
        for (int i = 0; i < totalVars; i++) {
            program.createVariable();
        }
        BasicBlock block = program.createBasicBlock();
        int retValVar = 1 + params.length;

        if (returnType == ValueType.VOID) {
            // No value to return
        } else if (isPrimitive(returnType)) {
            emitPrimitiveConstant(program, block, retValVar, returnType, defaultValue);
        } else {
            // Return null for object types
            NullConstantInstruction nullInsn = new NullConstantInstruction();
            nullInsn.setReceiver(program.variableAt(retValVar));
            block.add(nullInsn);
        }

        ExitInstruction exit = new ExitInstruction();
        exit.setValueToReturn(returnType == ValueType.VOID ? null : program.variableAt(retValVar));
        block.add(exit);
        return program;
    }

    private boolean isPrimitive(ValueType type) {
        return type == ValueType.BOOLEAN || type == ValueType.BYTE || type == ValueType.SHORT ||
               type == ValueType.INTEGER || type == ValueType.LONG || type == ValueType.FLOAT ||
               type == ValueType.DOUBLE || type == ValueType.CHACTER;
    }

    private void emitPrimitiveConstant(Program program, BasicBlock block, int var, ValueType type, Object value) {
        if (type == ValueType.BOOLEAN) {
            BooleanConstantInstruction insn = new BooleanConstantInstruction();
            insn.setConstant(value instanceof Boolean ? (Boolean) value : false);
            insn.setReceiver(program.variableAt(var));
            block.add(insn);
        } else if (type == ValueType.INTEGER) {
            IntegerConstantInstruction insn = new IntegerConstantInstruction();
            insn.setConstant(value instanceof Number ? ((Number) value).intValue() : 0);
            insn.setReceiver(program.variableAt(var));
            block.add(insn);
        } else if (type == ValueType.LONG) {
            LongConstantInstruction insn = new LongConstantInstruction();
            insn.setConstant(value instanceof Number ? ((Number) value).longValue() : 0L);
            insn.setReceiver(program.variableAt(var));
            block.add(insn);
        } else if (type == ValueType.FLOAT) {
            FloatConstantInstruction insn = new FloatConstantInstruction();
            insn.setConstant(value instanceof Number ? ((Number) value).floatValue() : 0f);
            insn.setReceiver(program.variableAt(var));
            block.add(insn);
        } else if (type == ValueType.DOUBLE) {
            DoubleConstantInstruction insn = new DoubleConstantInstruction();
            insn.setConstant(value instanceof Number ? ((Number) value).doubleValue() : 0.0);
            insn.setReceiver(program.variableAt(var));
            block.add(insn);
        } else {
            // Default: emit 0 int
            IntegerConstantInstruction insn = new IntegerConstantInstruction();
            insn.setConstant(0);
            insn.setReceiver(program.variableAt(var));
            block.add(insn);
        }
    }

    private static class MethodSpec {
        final String methodName;
        final String returnType;
        final String[] paramTypes;
        final Object defaultValue;

        MethodSpec(String methodName, String returnType, String[] paramTypes, Object defaultValue) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.paramTypes = paramTypes;
            this.defaultValue = defaultValue;
        }
    }
}
