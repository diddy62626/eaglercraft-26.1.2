package net.lax1dude.eaglercraft.v2_6.teavm_plugin;

import org.teavm.model.*;
import org.teavm.model.instructions.*;

/**
 * Replaces FileSystemProvider.installedProviders() with a method body
 * that returns a list containing our JarFileSystemProvider.
 *
 * TeaVM's ServiceLoader mechanism (META-INF/services) doesn't reliably
 * discover our JarFileSystemProvider at compile time. Instead of fighting
 * with ServiceLoader, we directly patch the installedProviders() method
 * at the IR level to return a singleton list with our provider.
 *
 * This fixes MC's Util.<clinit> which throws
 * "No jar file system provider found" if no provider with scheme "jar"
 * is found in the installed providers list.
 */
public class FileSystemProviderTransformer implements ClassHolderTransformer {

    private static final String JAR_FSP_CLASS =
        "net.lax1dude.eaglercraft.v2_6.patch.JarFileSystemProvider";
    private static final String FSP_CLASS =
        "java.nio.file.spi.FileSystemProvider";

    @Override
    public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
        if (!cls.getName().equals(FSP_CLASS)) return;

        // Find the installedProviders() static method
        MethodDescriptor desc = new MethodDescriptor("installedProviders",
            ValueType.object("java.util.List"));
        MethodHolder m = cls.getMethod(desc);
        if (m == null) {
            // Method doesn't exist — can't patch
            return;
        }

        // Create a new program that returns a list with JarFileSystemProvider
        Program program = new Program();

        // Variables for a static method with no params:
        // var 0: JarFileSystemProvider instance
        // var 1: the list to return
        Variable providerVar = program.createVariable();
        Variable listVar = program.createVariable();

        BasicBlock block = program.createBasicBlock();

        // 1. Construct new JarFileSystemProvider
        ConstructInstruction construct = new ConstructInstruction();
        construct.setType(JAR_FSP_CLASS);
        construct.setReceiver(providerVar);
        block.add(construct);

        // 2. Call JarFileSystemProvider.<init>()
        InvokeInstruction initCall = new InvokeInstruction();
        initCall.setType(InvocationType.SPECIAL);
        initCall.setMethod(new MethodReference(
            JAR_FSP_CLASS, "<init>", ValueType.VOID));
        initCall.setInstance(providerVar);
        block.add(initCall);

        // 3. Call Collections.singletonList(provider)
        // InvocationType has only VIRTUAL and SPECIAL; SPECIAL is used
        // for static methods (invokestatic) in TeaVM IR.
        InvokeInstruction listCall = new InvokeInstruction();
        listCall.setType(InvocationType.SPECIAL);
        listCall.setMethod(new MethodReference(
            "java.util.Collections",
            "singletonList",
            ValueType.object("java.lang.Object"),
            ValueType.object("java.util.List")));
        listCall.setArguments(new Variable[] { providerVar });
        listCall.setReceiver(listVar);
        block.add(listCall);

        // 4. Return the list
        ExitInstruction exit = new ExitInstruction();
        exit.setValueToReturn(listVar);
        block.add(exit);

        m.setProgram(program);
        // Remove ABSTRACT flag since we now have a body
        m.getModifiers().remove(ElementModifier.ABSTRACT);
    }
}
