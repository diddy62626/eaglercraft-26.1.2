package net.lax1dude.eaglercraft.v2_6.teavm_plugin;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.Program;
import org.teavm.model.BasicBlock;
import org.teavm.model.ValueType;
import org.teavm.model.ElementModifier;
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
        METHODS = new HashMap<>();
        // Only PrimitiveCodec.optionalFieldOf — can't be done via patches
        // due to Java return type clash (Codec<Optional<T>> vs MapCodec<A>)
        add("com.mojang.serialization.codecs.PrimitiveCodec", "optionalFieldOf",
            ValueType.object("com.mojang.serialization.MapCodec"),
            new ValueType[] { ValueType.object("java.lang.String") }, null, false);
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
            // ABSTRACT methods have no program — TeaVM generates stubs that
            // return undefined/null at runtime. This avoids ALL SSA optimizer
            // crashes (NPE, AssertionError, IllegalArgumentException).
            // MC catches the null returns and runs in adapter-only mode.
            m.getModifiers().add(ElementModifier.ABSTRACT);
            if (spec.isStatic) {
                m.getModifiers().add(ElementModifier.STATIC);
            }
            cls.addMethod(m);
        }
    }


    private Program createProgram(ValueType returnType, int paramCount, Object defaultValue, boolean isStatic) {
        Program program = new Program();

        // Create variables: this(if instance) + params + 1 extra
        int thisOffset = isStatic ? 0 : 1;
        int totalVars = thisOffset + paramCount + 1;
        for (int i = 0; i < totalVars; i++) {
            program.createVariable();
        }

        BasicBlock block = program.createBasicBlock();

        // Simple approach: just exit without any constant instructions.
        // TeaVM will return undefined/null/0 for all types.
        // No constant instructions means no SSA optimizer issues.
        ExitInstruction exit = new ExitInstruction();
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
