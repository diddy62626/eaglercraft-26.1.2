package net.lax1dude.eaglercraft.v2_6.teavm_plugin;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.Program;
import org.teavm.model.BasicBlock;
import org.teavm.model.ValueType;
import org.teavm.model.Modifier;
import org.teavm.model.instructions.ExitInstruction;
import org.teavm.model.instructions.NullConstantInstruction;
import org.teavm.model.instructions.IntegerConstantInstruction;
import org.teavm.model.instructions.LongConstantInstruction;
import org.teavm.model.instructions.FloatConstantInstruction;
import org.teavm.model.instructions.DoubleConstantInstruction;

import java.util.*;

/**
 * Adds missing methods, constructors, and static methods to existing
 * java.base classes (and any other class TeaVM's classlib provides).
 *
 * TeaVM ignores patches via --patch-module for classes that already
 * exist in its classlib, so we use a TeaVM plugin transformer to
 * inject the missing members at IR level.
 */
public class MissingMethodTransformer implements ClassHolderTransformer {

    private static final Map<String, List<MethodSpec>> METHODS = new HashMap<>();

    static {
        // ===== java.lang.Runtime =====
        add("java.lang.Runtime", "maxMemory", ValueType.LONG, new ValueType[0], 512L * 1024 * 1024, false);
        add("java.lang.Runtime", "addShutdownHook", ValueType.VOID, new ValueType[] { ValueType.object("java.lang.Thread") }, null, false);
        add("java.lang.Runtime", "removeShutdownHook", ValueType.BOOLEAN, new ValueType[] { ValueType.object("java.lang.Thread") }, 0, false);

        // ===== java.lang.System (static) =====
        add("java.lang.System", "getenv", ValueType.object("java.util.Map"), new ValueType[0], null, true);

        // ===== java.lang.Thread =====
        add("java.lang.Thread", "setContextClassLoader", ValueType.VOID, new ValueType[] { ValueType.object("java.lang.ClassLoader") }, null, false);

        // ===== java.lang.Class =====
        add("java.lang.Class", "getGenericInterfaces", ValueType.arrayOf(ValueType.object("java.lang.reflect.Type")), new ValueType[0], null, false);
        add("java.lang.Class", "getGenericSuperclass", ValueType.object("java.lang.reflect.Type"), new ValueType[0], null, false);
        add("java.lang.Class", "getResource", ValueType.object("java.net.URL"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);
        add("java.lang.Class", "getSigners", ValueType.arrayOf(ValueType.object("java.lang.Object")), new ValueType[0], null, false);
        add("java.lang.Class", "isAnonymousClass", ValueType.BOOLEAN, new ValueType[0], 0, false);

        // ===== java.lang.ClassLoader =====
        add("java.lang.ClassLoader", "getResource", ValueType.object("java.net.URL"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);
        add("java.lang.ClassLoader", "getResources", ValueType.object("java.util.Enumeration"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);
        add("java.lang.ClassLoader", "loadClass", ValueType.object("java.lang.Class"), new ValueType[] { ValueType.object("java.lang.String") }, null, false);
        add("java.lang.ClassLoader", "getSystemResource", ValueType.object("java.net.URL"), new ValueType[] { ValueType.object("java.lang.String") }, null, true);
        add("java.lang.ClassLoader", "getSystemResources", ValueType.object("java.util.Enumeration"), new ValueType[] { ValueType.object("java.lang.String") }, null, true);
        add("java.lang.ClassLoader", "getSystemResourceAsStream", ValueType.object("java.io.InputStream"), new ValueType[] { ValueType.object("java.lang.String") }, null, true);

        // ===== java.lang.Package =====
        add("java.lang.Package", "getSpecificationTitle", ValueType.object("java.lang.String"), new ValueType[0], null, false);
        add("java.lang.Package", "getSpecificationVersion", ValueType.object("java.lang.String"), new ValueType[0], null, false);
        add("java.lang.Package", "getSpecificationVendor", ValueType.object("java.lang.String"), new ValueType[0], null, false);
        add("java.lang.Package", "getImplementationTitle", ValueType.object("java.lang.String"), new ValueType[0], null, false);
        add("java.lang.Package", "getImplementationVersion", ValueType.object("java.lang.String"), new ValueType[0], null, false);
        add("java.lang.Package", "getImplementationVendor", ValueType.object("java.lang.String"), new ValueType[0], null, false);

        // ===== java.lang.Integer / Long / Character (static) =====
        add("java.lang.Integer", "parseUnsignedInt", ValueType.INTEGER, new ValueType[] { ValueType.object("java.lang.String"), ValueType.INTEGER }, 0, true);
        add("java.lang.Long", "parseUnsignedLong", ValueType.LONG, new ValueType[] { ValueType.object("java.lang.String"), ValueType.INTEGER }, 0L, true);
        add("java.lang.Character", "codePointOf", ValueType.INTEGER, new ValueType[] { ValueType.object("java.lang.String") }, 0, true);
        add("java.lang.Character", "toString", ValueType.object("java.lang.String"), new ValueType[] { ValueType.INTEGER }, null, true);

        // ===== java.lang.StackWalker =====
        add("java.lang.StackWalker", "getInstance", ValueType.object("java.lang.StackWalker"),
            new ValueType[] { ValueType.object("java.util.Set"), ValueType.INTEGER }, null, true);

        // StackWalker$StackFrame is an interface; add getDeclaringClass
        add("java.lang.StackWalker$StackFrame", "getDeclaringClass", ValueType.object("java.lang.Class"), new ValueType[0], null, false);

        // ===== java.util.UUID (constructor + instance methods) =====
        add("java.util.UUID", "<init>", ValueType.VOID, new ValueType[] { ValueType.LONG, ValueType.LONG }, null, false);
        add("java.util.UUID", "getMostSignificantBits", ValueType.LONG, new ValueType[0], 0L, false);
        add("java.util.UUID", "getLeastSignificantBits", ValueType.LONG, new ValueType[0], 0L, false);
        add("java.util.UUID", "version", ValueType.INTEGER, new ValueType[0], 0, false);
        add("java.util.UUID", "variant", ValueType.INTEGER, new ValueType[0], 0, false);
        add("java.util.UUID", "timestamp", ValueType.LONG, new ValueType[0], 0L, false);
        add("java.util.UUID", "clockSequence", ValueType.INTEGER, new ValueType[0], 0, false);
        add("java.util.UUID", "node", ValueType.LONG, new ValueType[0], 0L, false);
        add("java.util.UUID", "nameUUIDFromBytes", ValueType.object("java.util.UUID"), new ValueType[] { ValueType.arrayOf(ValueType.BYTE) }, null, true);

        // ===== java.util.Date =====
        add("java.util.Date", "toInstant", ValueType.object("java.time.Instant"), new ValueType[0], null, false);

        // ===== java.io.File =====
        add("java.io.File", "toPath", ValueType.object("java.nio.file.Path"), new ValueType[0], null, false);

        // ===== java.nio.file.Files (static) =====
        add("java.nio.file.Files", "getFileStore", ValueType.object("java.nio.file.FileStore"), new ValueType[] { ValueType.object("java.nio.file.Path") }, null, true);

        // ===== java.util.concurrent.ConcurrentHashMap (static) =====
        add("java.util.concurrent.ConcurrentHashMap", "newKeySet", ValueType.object("java.util.concurrent.ConcurrentHashMap$KeySetView"), new ValueType[0], null, true);

        // ===== java.util.stream.StreamSupport (static) =====
        add("java.util.stream.StreamSupport", "intStream", ValueType.object("java.util.stream.IntStream"),
            new ValueType[] { ValueType.object("java.util.Spliterator$OfInt"), ValueType.BOOLEAN }, null, true);
        add("java.util.stream.StreamSupport", "longStream", ValueType.object("java.util.stream.LongStream"),
            new ValueType[] { ValueType.object("java.util.Spliterator$OfLong"), ValueType.BOOLEAN }, null, true);

        // ===== java.net.URL =====
        add("java.net.URL", "openConnection", ValueType.object("java.net.URLConnection"),
            new ValueType[] { ValueType.object("java.net.Proxy") }, null, false);

        // ===== java.nio.channels.Channels (static) =====
        add("java.nio.channels.Channels", "newChannel", ValueType.object("java.nio.channels.WritableByteChannel"),
            new ValueType[] { ValueType.object("java.io.OutputStream") }, null, true);

        // ===== java.nio.charset.Charset (static) =====
        add("java.nio.charset.Charset", "isSupported", ValueType.BOOLEAN,
            new ValueType[] { ValueType.object("java.lang.String") }, 1, true);

        // ===== java.util.Collections (static) =====
        add("java.util.Collections", "unmodifiableSortedMap", ValueType.object("java.util.SortedMap"),
            new ValueType[] { ValueType.object("java.util.SortedMap") }, null, true);

        // ===== java.util.Spliterators (static + constructor) =====
        add("java.util.Spliterators$AbstractSpliterator", "<init>", ValueType.VOID,
            new ValueType[] { ValueType.LONG, ValueType.INTEGER }, null, false);
        add("java.util.Spliterators", "iterator", ValueType.object("java.util.PrimitiveIterator$OfInt"),
            new ValueType[] { ValueType.object("java.util.Spliterator$OfInt") }, null, true);
        add("java.util.Spliterators", "iterator", ValueType.object("java.util.Iterator"),
            new ValueType[] { ValueType.object("java.util.Spliterator") }, null, true);

        // ===== java.util.concurrent.Executors (static) =====
        add("java.util.concurrent.Executors", "newScheduledThreadPool", ValueType.object("java.util.concurrent.ScheduledExecutorService"),
            new ValueType[] { ValueType.INTEGER, ValueType.object("java.util.concurrent.ThreadFactory") }, null, true);
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
            // Build signature: param types + return type
            ValueType[] signature = new ValueType[spec.paramTypes.length + 1];
            System.arraycopy(spec.paramTypes, 0, signature, 0, spec.paramTypes.length);
            signature[spec.paramTypes.length] = spec.returnType;

            MethodDescriptor desc = new MethodDescriptor(spec.methodName, signature);
            if (cls.getMethod(desc) != null) continue;

            MethodHolder m = new MethodHolder(desc);
            Program program = createProgram(spec.returnType, spec.paramTypes.length, spec.defaultValue);
            m.setProgram(program);

            // Set STATIC modifier if specified. Constructors (<init>/<clinit>)
            // don't need any extra modifier beyond what the class has.
            if (spec.isStatic) {
                m.getModifiers().add(Modifier.STATIC);
            }

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
            // No value to set
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
        } else if (returnType == ValueType.FLOAT) {
            FloatConstantInstruction insn = new FloatConstantInstruction();
            insn.setConstant((float) valueToDouble(defaultValue));
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

    private int valueToInt(Object v) { return v instanceof Number ? ((Number) v).intValue() : 0; }
    private long valueToLong(Object v) { return v instanceof Number ? ((Number) v).longValue() : 0L; }
    private double valueToDouble(Object v) { return v instanceof Number ? ((Number) v).doubleValue() : 0.0; }

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
