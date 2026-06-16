package net.lax1dude.eaglercraft.v2_6.teavm_plugin;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.Program;
import org.teavm.model.BasicBlock;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;
import org.teavm.model.instructions.ExitInstruction;
import org.teavm.model.instructions.NullConstantInstruction;
import org.teavm.model.instructions.IntegerConstantInstruction;
import org.teavm.model.instructions.LongConstantInstruction;
import org.teavm.model.instructions.FloatConstantInstruction;
import org.teavm.model.instructions.DoubleConstantInstruction;
import org.teavm.model.instructions.BooleanConstantInstruction;
import org.teavm.model.ElementModifier;

import java.util.*;

/**
 * Adds missing methods to existing java.base classes.
 */
public class MissingMethodTransformer implements ClassHolderTransformer {

    private static final Map<String, List<MethodSpec>> METHODS = new HashMap<>();

    static {
        add("java.lang.Runtime", "maxMemory", ValueType.LONG, new ValueType[0], 512L * 1024 * 1024);
        add("java.lang.Runtime", "addShutdownHook", ValueType.VOID, new ValueType[] { ValueType.object("java.lang.Thread") }, null);
        add("java.lang.Runtime", "removeShutdownHook", ValueType.BOOLEAN, new ValueType[] { ValueType.object("java.lang.Thread") }, false);

        add("java.lang.System", "getenv", ValueType.object("java.util.Map"), new ValueType[0], null);

        add("java.lang.Thread", "setContextClassLoader", ValueType.VOID, new ValueType[] { ValueType.object("java.lang.ClassLoader") }, null);

        add("java.lang.Class", "getGenericInterfaces", ValueType.arrayOf(ValueType.object("java.lang.reflect.Type")), new ValueType[0], null);
        add("java.lang.Class", "getGenericSuperclass", ValueType.object("java.lang.reflect.Type"), new ValueType[0], null);
        add("java.lang.Class", "getResource", ValueType.object("java.net.URL"), new ValueType[] { ValueType.object("java.lang.String") }, null);
        add("java.lang.Class", "getSigners", ValueType.arrayOf(ValueType.object("java.lang.Object")), new ValueType[0], null);
        add("java.lang.Class", "isAnonymousClass", ValueType.BOOLEAN, new ValueType[0], false);

        add("java.lang.ClassLoader", "getResource", ValueType.object("java.net.URL"), new ValueType[] { ValueType.object("java.lang.String") }, null);
        add("java.lang.ClassLoader", "getResources", ValueType.object("java.util.Enumeration"), new ValueType[] { ValueType.object("java.lang.String") }, null);
        add("java.lang.ClassLoader", "loadClass", ValueType.object("java.lang.Class"), new ValueType[] { ValueType.object("java.lang.String") }, null);

        add("java.lang.Integer", "parseUnsignedInt", ValueType.INTEGER, new ValueType[] { ValueType.object("java.lang.String"), ValueType.INTEGER }, 0);
        add("java.lang.Long", "parseUnsignedLong", ValueType.LONG, new ValueType[] { ValueType.object("java.lang.String"), ValueType.INTEGER }, 0L);

        add("java.lang.Character", "codePointOf", ValueType.INTEGER, new ValueType[] { ValueType.object("java.lang.String") }, 0);
        add("java.lang.Character", "toString", ValueType.object("java.lang.String"), new ValueType[] { ValueType.INTEGER }, null);

        add("java.util.UUID", "getMostSignificantBits", ValueType.LONG, new ValueType[0], 0L);
        add("java.util.UUID", "nameUUIDFromBytes", ValueType.object("java.util.UUID"), new ValueType[] { ValueType.arrayOf(ValueType.BYTE) }, null);

        add("java.util.Date", "toInstant", ValueType.object("java.time.Instant"), new ValueType[0], null);
        add("java.io.File", "toPath", ValueType.object("java.nio.file.Path"), new ValueType[0], null);
        add("java.nio.file.Files", "getFileStore", ValueType.object("java.nio.file.FileStore"), new ValueType[] { ValueType.object("java.nio.file.Path") }, null);

        add("java.util.concurrent.ConcurrentHashMap", "newKeySet", ValueType.object("java.util.Set"), new ValueType[0], null);

        add("java.util.stream.StreamSupport", "intStream", ValueType.object("java.util.stream.IntStream"),
            new ValueType[] { ValueType.object("java.util.Spliterator$OfInt"), ValueType.BOOLEAN }, null);
        add("java.util.stream.StreamSupport", "longStream", ValueType.object("java.util.stream.LongStream"),
            new ValueType[] { ValueType.object("java.util.Spliterator$OfLong"), ValueType.BOOLEAN }, null);
    }

    private static void add(String className, String methodName, ValueType returnType, ValueType[] paramTypes, Object defaultValue) {
        METHODS.computeIfAbsent(className, k -> new ArrayList<>())
               .add(new MethodSpec(methodName, returnType, paramTypes, defaultValue));
    }

    @Override
    public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
        List<MethodSpec> specs = METHODS.get(cls.getName());
        if (specs == null) return;

        for (MethodSpec spec : specs) {
            // Build signature: param types + return type
            ValueType[] signature = new ValueType[spec.paramTypes.length + 1];
            System.arraycopy(spec.paramTypes, 0, signature, 0, spec.paramTypes.length);
            signature[spec.paramTypes.length] = spec.returnType;

            MethodDescriptor desc = new MethodDescriptor(spec.methodName, signature);
            if (cls.getMethod(desc) != null) continue;

            MethodHolder m = new MethodHolder(desc);
            Program program = createProgram(spec.returnType, spec.paramTypes.length, spec.defaultValue);
            m.setProgram(program);
            m.getModifiers().add(ElementModifier.PUBLIC);

            // Add STATIC modifier for static methods (System, UUID static, StreamSupport, Files, ConcurrentHashMap)
            if (isStaticMethod(cls.getName(), spec.methodName)) {
                m.getModifiers().add(ElementModifier.STATIC);
            }
            cls.addMethod(m);
        }
    }

    private boolean isStaticMethod(String className, String methodName) {
        // System.getenv is static
        // UUID.nameUUIDFromBytes is static
        // ConcurrentHashMap.newKeySet is static
        // StreamSupport.* is static
        // Files.* is static
        // Integer.parseUnsignedInt is static
        // Long.parseUnsignedLong is static
        // Character.codePointOf and toString(int) are static
        return (className.equals("java.lang.System") && methodName.equals("getenv")) ||
               (className.equals("java.util.UUID") && methodName.equals("nameUUIDFromBytes")) ||
               (className.equals("java.util.concurrent.ConcurrentHashMap") && methodName.equals("newKeySet")) ||
               className.equals("java.util.stream.StreamSupport") ||
               className.equals("java.nio.file.Files") ||
               (className.equals("java.lang.Integer") && methodName.equals("parseUnsignedInt")) ||
               (className.equals("java.lang.Long") && methodName.equals("parseUnsignedLong")) ||
               (className.equals("java.lang.Character") && (methodName.equals("codePointOf") || methodName.equals("toString")));
    }

    private Program createProgram(ValueType returnType, int paramCount, Object defaultValue) {
        Program program = new Program();
        // Variable 0 = this (or unused for static)
        // Variables 1..N = params
        // Variable N+1 = return value
        int totalVars = 1 + paramCount + 1;
        for (int i = 0; i < totalVars; i++) {
            program.createVariable();
        }
        BasicBlock block = program.createBasicBlock();
        int retValVar = 1 + paramCount;

        if (returnType == ValueType.VOID) {
            // No value to return
        } else if (returnType == ValueType.BOOLEAN) {
            BooleanConstantInstruction insn = new BooleanConstantInstruction();
            insn.setConstant(valueToBool(defaultValue));
            insn.setReceiver(program.variableAt(retValVar));
            block.add(insn);
        } else if (returnType == ValueType.INTEGER) {
            IntegerConstantInstruction insn = new IntegerConstantInstruction();
            insn.setConstant(valueToInt(defaultValue));
            insn.setReceiver(program.variableAt(retValVar));
            block.add(insn);
        } else if (returnType == ValueType.LONG) {
            LongConstantInstruction insn = new LongConstantInstruction();
            insn.setConstant(valueToLong(defaultValue));
            insn.setReceiver(program.variableAt(retValVar));
            block.add(insn);
        } else if (returnType == ValueType.FLOAT) {
            FloatConstantInstruction insn = new FloatConstantInstruction();
            insn.setConstant(valueToFloat(defaultValue));
            insn.setReceiver(program.variableAt(retValVar));
            block.add(insn);
        } else if (returnType == ValueType.DOUBLE) {
            DoubleConstantInstruction insn = new DoubleConstantInstruction();
            insn.setConstant(valueToDouble(defaultValue));
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

    private boolean valueToBool(Object v) { return v instanceof Boolean ? (Boolean) v : false; }
    private int valueToInt(Object v) { return v instanceof Number ? ((Number) v).intValue() : 0; }
    private long valueToLong(Object v) { return v instanceof Number ? ((Number) v).longValue() : 0L; }
    private float valueToFloat(Object v) { return v instanceof Number ? ((Number) v).floatValue() : 0f; }
    private double valueToDouble(Object v) { return v instanceof Number ? ((Number) v).doubleValue() : 0.0; }

    private static class MethodSpec {
        final String methodName;
        final ValueType returnType;
        final ValueType[] paramTypes;
        final Object defaultValue;

        MethodSpec(String methodName, ValueType returnType, ValueType[] paramTypes, Object defaultValue) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.paramTypes = paramTypes;
            this.defaultValue = defaultValue;
        }
    }
}
