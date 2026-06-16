package net.lax1dude.eaglercraft.v2_6.teavm_plugin;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.Program;
import org.teavm.model.BasicBlock;
import org.teavm.model.ValueType;
import org.teavm.model.instructions.ExitInstruction;
import org.teavm.model.instructions.NullConstantInstruction;
import org.teavm.model.instructions.IntegerConstantInstruction;
import org.teavm.model.instructions.LongConstantInstruction;

import java.util.*;

/**
 * Adds missing methods to existing java.base classes.
 * Uses minimal TeaVM API to avoid version-specific issues.
 */
public class MissingMethodTransformer implements ClassHolderTransformer {

    private static final Map<String, List<MethodSpec>> METHODS = new HashMap<>();

    static {
        // Runtime
        add("java.lang.Runtime", "maxMemory", ValueType.LONG, new ValueType[0], 512L * 1024 * 1024, false);
        add("java.lang.Runtime", "addShutdownHook", ValueType.VOID, new ValueType[] { ValueType.object("java.lang.Thread") }, null, false);
        add("java.lang.Runtime", "removeShutdownHook", ValueType.INTEGER, new ValueType[] { ValueType.object("java.lang.Thread") }, 0, false);

        // System (static)
        add("java.lang.System", "getenv", ValueType.object("java.util.Map"), new ValueType[0], null, true);

        // Thread
        add("java.lang.Thread", "setContextClassLoader", ValueType.VOID, new ValueType[] { ValueType.object("java.lang.ClassLoader") }, null, false);

        // Class
        add("java.lang.Class", "getGenericInterfaces", ValueType.arrayOf(ValueType.object("java.lang.reflect.Type")), new ValueType[0], null, false);
        add("java.lang.Class", "getGenericSuperclass", ValueType.object("java.lang.reflect.Type"), new ValueType[0], null, false);
        add("java.lang.Class", "getResource", ValueType.object("java.net.URL"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);
        add("java.lang.Class", "getSigners", ValueType.arrayOf(ValueType.object("java.lang.Object")), new ValueType[0], null, false);
        add("java.lang.Class", "isAnonymousClass", ValueType.INTEGER, new ValueType[0], 0, false);

        // ClassLoader
        add("java.lang.ClassLoader", "getResource", ValueType.object("java.net.URL"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);
        add("java.lang.ClassLoader", "getResources", ValueType.object("java.util.Enumeration"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);
        add("java.lang.ClassLoader", "loadClass", ValueType.object("java.lang.Class"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);

        // Integer (static)
        add("java.lang.Integer", "parseUnsignedInt", ValueType.INTEGER, new ValueType[] { ValueType.object("java.lang.String"), ValueType.INTEGER }, 0, true);
        // Long (static)
        add("java.lang.Long", "parseUnsignedLong", ValueType.LONG, new ValueType[] { ValueType.object("java.lang.String"), ValueType.INTEGER }, 0L, true);

        // Character (static)
        add("java.lang.Character", "codePointOf", ValueType.INTEGER, new ValueType[] { ValueType.object("java.lang.String") }, 0, true);
        add("java.lang.Character", "toString", ValueType.object("java.lang.String"), new ValueType[] { ValueType.INTEGER }, null, true);

        // UUID
        add("java.util.UUID", "getMostSignificantBits", ValueType.LONG, new ValueType[0], 0L, false);
        add("java.util.UUID", "nameUUIDFromBytes", ValueType.object("java.util.UUID"), new ValueType[] { ValueType.arrayOf(ValueType.BYTE) }, null, true);

        // Date
        add("java.util.Date", "toInstant", ValueType.object("java.time.Instant"), new ValueType[0], null, false);
        // File
        add("java.io.File", "toPath", ValueType.object("java.nio.file.Path"), new ValueType[0], null, false);
        // Files (static)
        add("java.nio.file.Files", "getFileStore", ValueType.object("java.nio.file.FileStore"), new ValueType[] { ValueType.object("java.nio.file.Path") }, null, true);

        // ConcurrentHashMap (static)
        add("java.util.concurrent.ConcurrentHashMap", "newKeySet", ValueType.object("java.util.Set"), new ValueType[0], null, true);

        // StreamSupport (static)
        add("java.util.stream.StreamSupport", "intStream", ValueType.object("java.util.stream.IntStream"),
            new ValueType[] { ValueType.object("java.util.Spliterator$OfInt"), ValueType.INTEGER }, null, true);
        add("java.util.stream.StreamSupport", "longStream", ValueType.object("java.util.stream.LongStream"),
            new ValueType[] { ValueType.object("java.util.Spliterator$OfLong"), ValueType.INTEGER }, null, true);
    }

    private static void add(String className, String methodName, ValueType returnType, ValueType[] paramTypes, Object defaultValue, boolean isStatic) {
        METHODS.computeIfAbsent(className, k -> new ArrayList<>())
               .add(new MethodSpec(methodName, returnType, paramTypes, defaultValue, isStatic));
    }

    @Override
    public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
        List<MethodSpec> specs = METHODS.get(cls.getName());
        if (specs == null) return;

        for (MethodSpec spec : specs) {
            // Build signature: param types + return type (varargs)
            ValueType[] signature = new ValueType[spec.paramTypes.length + 1];
            System.arraycopy(spec.paramTypes, 0, signature, 0, spec.paramTypes.length);
            signature[spec.paramTypes.length] = spec.returnType;

            MethodDescriptor desc = new MethodDescriptor(spec.methodName, signature);
            if (cls.getMethod(desc) != null) continue;

            MethodHolder m = new MethodHolder(desc);
            Program program = createProgram(spec.returnType, spec.paramTypes.length, spec.defaultValue);
            m.setProgram(program);
            // Don't set modifiers - let default (public) apply
            cls.addMethod(m);
        }
    }

    private Program createProgram(ValueType returnType, int paramCount, Object defaultValue) {
        Program program = new Program();
        int totalVars = 1 + paramCount + 1;
        for (int i = 0; i < totalVars; i++) {
            program.createVariable();
        }
        BasicBlock block = program.createBasicBlock();
        int retValVar = 1 + paramCount;

        if (returnType == ValueType.VOID) {
            // No value
        } else if (returnType == ValueType.INTEGER || returnType == ValueType.BOOLEAN ||
                   returnType == ValueType.BYTE || returnType == ValueType.SHORT ||
                   returnType == ValueType.CHARACTER) {
            IntegerConstantInstruction insn = new IntegerConstantInstruction();
            insn.setConstant(valueToInt(defaultValue));
            insn.setReceiver(program.variableAt(retValVar));
            block.add(insn);
        } else if (returnType == ValueType.LONG) {
            LongConstantInstruction insn = new LongConstantInstruction();
            insn.setConstant(valueToLong(defaultValue));
            insn.setReceiver(program.variableAt(retValVar));
            block.add(insn);
        } else {
            // Object/array types: return null
            NullConstantInstruction insn = new NullConstantInstruction();
            insn.setReceiver(program.variableAt(retValVar));
            block.add(insn);
        }

        ExitInstruction exit = new ExitInstruction();
        exit.setValueToReturn(returnType == ValueType.VOID ? null : program.variableAt(retValVar));
        block.add(exit);
        return program;
    }

    private int valueToInt(Object v) { return v instanceof Number ? ((Number) v).intValue() : 0; }
    private long valueToLong(Object v) { return v instanceof Number ? ((Number) v).longValue() : 0L; }

    private static class MethodSpec {
        final String methodName;
        final ValueType returnType;
        final ValueType[] paramTypes;
        final Object defaultValue;
        final boolean isStatic;

        MethodSpec(String methodName, ValueType returnType, ValueType[] paramTypes, Object defaultValue, boolean isStatic) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.paramTypes = paramTypes;
            this.defaultValue = defaultValue;
            this.isStatic = isStatic;
        }
    }
}
