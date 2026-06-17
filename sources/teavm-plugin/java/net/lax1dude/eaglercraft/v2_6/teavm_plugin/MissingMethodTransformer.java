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
        // callable(Runnable, T) returns Callable<T>
        add("java.util.concurrent.Executors", "callable", ValueType.object("java.util.concurrent.Callable"),
            new ValueType[] { ValueType.object("java.lang.Runnable"), ValueType.object("java.lang.Object") }, null, true);

        // ===== java.util.concurrent.ScheduledThreadPoolExecutor =====
        add("java.util.concurrent.ScheduledThreadPoolExecutor", "setContinueExistingPeriodicTasksAfterShutdownPolicy", ValueType.VOID,
            new ValueType[] { ValueType.BOOLEAN }, null, false);

        // ===== java.util.concurrent.CompletableFuture (instance methods return this) =====
        add("java.util.concurrent.CompletableFuture", "applyToEither", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.util.concurrent.CompletionStage"), ValueType.object("java.util.function.Function") }, null, false);
        add("java.util.concurrent.CompletableFuture", "exceptionallyComposeAsync", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.util.function.Function"), ValueType.object("java.util.concurrent.Executor") }, null, false);
        add("java.util.concurrent.CompletableFuture", "thenAcceptAsync", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.util.function.Consumer"), ValueType.object("java.util.concurrent.Executor") }, null, false);
        add("java.util.concurrent.CompletableFuture", "thenApplyAsync", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.util.function.Function"), ValueType.object("java.util.concurrent.Executor") }, null, false);
        add("java.util.concurrent.CompletableFuture", "thenCombine", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.util.concurrent.CompletionStage"), ValueType.object("java.util.function.BiFunction") }, null, false);
        add("java.util.concurrent.CompletableFuture", "thenComposeAsync", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.util.function.Function"), ValueType.object("java.util.concurrent.Executor") }, null, false);
        add("java.util.concurrent.CompletableFuture", "thenRunAsync", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.lang.Runnable"), ValueType.object("java.util.concurrent.Executor") }, null, false);

        // ===== java.util.concurrent.atomic.AtomicReferenceArray =====
        add("java.util.concurrent.atomic.AtomicReferenceArray", "lazySet", ValueType.VOID,
            new ValueType[] { ValueType.INTEGER, ValueType.object("java.lang.Object") }, null, false);

        // ===== java.util.regex.Pattern =====
        add("java.util.regex.Pattern", "asPredicate", ValueType.object("java.util.function.Predicate"),
            new ValueType[0], null, false);

        // ===== java.net.InetAddress =====
        add("java.net.InetAddress", "isAnyLocalAddress", ValueType.BOOLEAN, new ValueType[0], 0, false);
        add("java.net.InetAddress", "isLinkLocalAddress", ValueType.BOOLEAN, new ValueType[0], 0, false);
        add("java.net.InetAddress", "isMulticastAddress", ValueType.BOOLEAN, new ValueType[0], 0, false);
        add("java.net.InetAddress", "isSiteLocalAddress", ValueType.BOOLEAN, new ValueType[0], 0, false);
        // static getByAddress(String, byte[])
        add("java.net.InetAddress", "getByAddress", ValueType.object("java.net.InetAddress"),
            new ValueType[] { ValueType.object("java.lang.String"), ValueType.arrayOf(ValueType.BYTE) }, null, true);

        // ===== java.net.HttpURLConnection =====
        add("java.net.HttpURLConnection", "getContentLengthLong", ValueType.LONG, new ValueType[0], -1L, false);

        // ===== java.nio.channels.Channels (static) =====
        add("java.nio.channels.Channels", "newWriter", ValueType.object("java.io.Writer"),
            new ValueType[] { ValueType.object("java.nio.channels.WritableByteChannel"), ValueType.object("java.nio.charset.Charset") }, null, true);

        // ===== java.nio.file.FileSystem =====
        add("java.nio.file.FileSystem", "getPathMatcher", ValueType.object("java.nio.file.PathMatcher"),
            new ValueType[] { ValueType.object("java.lang.String") }, null, false);

        // ===== java.nio.file.Files (static) =====
        add("java.nio.file.Files", "createLink", ValueType.object("java.nio.file.Path"),
            new ValueType[] { ValueType.object("java.nio.file.Path"), ValueType.object("java.nio.file.Path") }, null, true);
        add("java.nio.file.Files", "setLastModifiedTime", ValueType.object("java.nio.file.Path"),
            new ValueType[] { ValueType.object("java.nio.file.Path"), ValueType.object("java.nio.file.attribute.FileTime") }, null, true);

        // ===== java.security.AccessController (static) =====
        add("java.security.AccessController", "checkPermission", ValueType.VOID,
            new ValueType[] { ValueType.object("java.security.Permission") }, null, true);

        // ===== java.io.BufferedReader =====
        add("java.io.BufferedReader", "transferTo", ValueType.LONG,
            new ValueType[] { ValueType.object("java.io.Writer") }, 0L, false);

        // ===== java.lang.ScopedValue$Carrier =====
        add("java.lang.ScopedValue$Carrier", "run", ValueType.VOID,
            new ValueType[] { ValueType.object("java.lang.Runnable") }, null, false);

        // ===== java.lang.Thread (constructor) =====
        add("java.lang.Thread", "<init>", ValueType.VOID,
            new ValueType[] { ValueType.object("java.lang.ThreadGroup"), ValueType.object("java.lang.Runnable"), ValueType.object("java.lang.String") }, null, false);

        // ===== java.lang.invoke.MethodHandle =====
        add("java.lang.invoke.MethodHandle", "bindTo", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.Object") }, null, false);
        // invokeExact has polymorphic signature - we add a few common signatures.
        // TeaVM reports each unique invokeExact signature as a separate missing method,
        // so we add the most common ones. If more are needed, they'll show up in the
        // next CI run.
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.LONG,
            new ValueType[] { ValueType.LONG }, 0L, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.VOID,
            new ValueType[] { ValueType.LONG }, null, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.VOID,
            new ValueType[] { ValueType.object("java.nio.ByteBuffer") }, null, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.object("java.nio.ByteBuffer"),
            new ValueType[] { ValueType.LONG, ValueType.INTEGER }, null, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.object("java.nio.ByteBuffer"),
            new ValueType[] { ValueType.LONG, ValueType.LONG }, null, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.object("io.netty.util.internal.CleanerJava25$CleanableDirectBufferImpl"),
            new ValueType[] { ValueType.INTEGER }, null, false);

        // ===== java.lang.invoke.MethodHandles (static) =====
        add("java.lang.invoke.MethodHandles", "lookup", ValueType.object("java.lang.invoke.MethodHandles$Lookup"), new ValueType[0], null, true);
        add("java.lang.invoke.MethodHandles", "publicLookup", ValueType.object("java.lang.invoke.MethodHandles$Lookup"), new ValueType[0], null, true);

        // ===== java.lang.invoke.MethodHandles$Lookup =====
        add("java.lang.invoke.MethodHandles$Lookup", "findVarHandle",
            ValueType.object("java.lang.invoke.VarHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.String"), ValueType.object("java.lang.Class") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "unreflectConstructor",
            ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.reflect.Constructor") }, null, false);

        // ===== java.lang.invoke.MethodType (static factories) =====
        add("java.lang.invoke.MethodType", "methodType", ValueType.object("java.lang.invoke.MethodType"),
            new ValueType[] { ValueType.object("java.lang.Class") }, null, true);
        add("java.lang.invoke.MethodType", "methodType", ValueType.object("java.lang.invoke.MethodType"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.Class") }, null, true);
        add("java.lang.invoke.MethodType", "methodType", ValueType.object("java.lang.invoke.MethodType"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.Class"), ValueType.arrayOf(ValueType.object("java.lang.Class")) }, null, true);

        // ===== java.lang.Thread (static) =====
        add("java.lang.Thread", "onSpinWait", ValueType.VOID, new ValueType[0], null, true);

        // ===== java.util.concurrent.CompletableFuture =====
        add("java.util.concurrent.CompletableFuture", "thenApplyAsync", ValueType.object("java.util.concurrent.CompletableFuture"),
            new ValueType[] { ValueType.object("java.util.function.Function") }, null, false);

        // ===== java.util.concurrent.ScheduledThreadPoolExecutor =====
        add("java.util.concurrent.ScheduledThreadPoolExecutor", "setExecuteExistingDelayedTasksAfterShutdownPolicy", ValueType.VOID,
            new ValueType[] { ValueType.BOOLEAN }, null, false);

        // ===== java.util.concurrent.TimeUnit =====
        add("java.util.concurrent.TimeUnit", "convert", ValueType.LONG,
            new ValueType[] { ValueType.object("java.time.Duration") }, 0L, false);

        // ===== java.nio.ByteBuffer =====
        add("java.nio.ByteBuffer", "slice", ValueType.object("java.nio.ByteBuffer"),
            new ValueType[] { ValueType.INTEGER, ValueType.INTEGER }, null, false);

        // ===== java.nio.file.Files (static) =====
        add("java.nio.file.Files", "getPosixFilePermissions", ValueType.object("java.util.Set"),
            new ValueType[] { ValueType.object("java.nio.file.Path"), ValueType.arrayOf(ValueType.object("java.nio.file.LinkOption")) }, null, true);
        add("java.nio.file.Files", "setPosixFilePermissions", ValueType.object("java.nio.file.Path"),
            new ValueType[] { ValueType.object("java.nio.file.Path"), ValueType.object("java.util.Set") }, null, true);

        // ===== Additional MethodHandle.invokeExact signatures =====
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.object("java.lang.Object"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.INTEGER }, null, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.object("java.lang.invoke.MethodHandles$Lookup"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.invoke.MethodHandles$Lookup") }, null, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.object("java.lang.invoke.VarHandle"),
            new ValueType[] { ValueType.object("java.lang.invoke.MethodHandles$Lookup"), ValueType.object("java.lang.Class"), ValueType.object("java.lang.String"), ValueType.object("java.lang.Class") }, null, false);

        // ===== VarHandle methods (called by Guava AbstractFutureState) =====
        // We provide Object-taking overloads in our VarHandle stub class.
        // However, TeaVM also needs explicit (3-arg) overloads matching call-site arity.
        // The plugin can add these to VarHandle since it's our stub class.
        add("java.lang.invoke.VarHandle", "compareAndSet", ValueType.BOOLEAN,
            new ValueType[] { ValueType.object("java.lang.Object"), ValueType.object("java.lang.Object"), ValueType.object("java.lang.Object") }, 0, false);
        add("java.lang.invoke.VarHandle", "getAndSet", ValueType.object("java.lang.Object"),
            new ValueType[] { ValueType.object("java.lang.Object"), ValueType.object("java.lang.Object") }, null, false);
        add("java.lang.invoke.VarHandle", "setRelease", ValueType.VOID,
            new ValueType[] { ValueType.object("java.lang.Object"), ValueType.object("java.lang.Object") }, null, false);
        add("java.lang.invoke.VarHandle", "storeStoreFence", ValueType.VOID, new ValueType[0], null, true);

        // ===== java.lang.System (static) =====
        add("java.lang.System", "exit", ValueType.VOID, new ValueType[] { ValueType.INTEGER }, null, true);

        // ===== java.lang.invoke.MethodHandle =====
        add("java.lang.invoke.MethodHandle", "asType", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.invoke.MethodType") }, null, false);

        // ===== java.lang.invoke.MethodHandles$Lookup =====
        add("java.lang.invoke.MethodHandles$Lookup", "findStatic", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.String"), ValueType.object("java.lang.invoke.MethodType") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "findVirtual", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.String"), ValueType.object("java.lang.invoke.MethodType") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "findSpecial", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.String"), ValueType.object("java.lang.invoke.MethodType"), ValueType.object("java.lang.Class") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "findGetter", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.String"), ValueType.object("java.lang.Class") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "findSetter", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.String"), ValueType.object("java.lang.Class") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "findConstructor", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.invoke.MethodType") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "unreflect", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.reflect.Method") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "unreflectGetter", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.reflect.Field") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "unreflectSetter", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.reflect.Field") }, null, false);
        add("java.lang.invoke.MethodHandles$Lookup", "privateLookupIn", ValueType.object("java.lang.invoke.MethodHandles$Lookup"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.lang.invoke.MethodHandles$Lookup") }, null, true);

        // ===== java.nio.channels.spi.SelectorProvider.openSelector returns AbstractSelector =====
        // (Note: this is an abstract method; we need to provide a default impl via plugin)
        // Actually it's already in the SelectorProvider patch as abstract. Plugin transformer can't easily fix this.
        // Let me instead make SelectorProvider.openSelector concrete in the patch.

        // ===== java.nio.file.Files (static) =====
        add("java.nio.file.Files", "getFileAttributeView", ValueType.object("java.nio.file.attribute.FileAttributeView"),
            new ValueType[] { ValueType.object("java.nio.file.Path"), ValueType.object("java.lang.Class"), ValueType.arrayOf(ValueType.object("java.nio.file.LinkOption")) }, null, true);

        // ===== java.util.Base64 (static) =====
        add("java.util.Base64", "getMimeDecoder", ValueType.object("java.util.Base64$Decoder"), new ValueType[0], null, true);
        add("java.util.Base64", "getMimeEncoder", ValueType.object("java.util.Base64$Encoder"),
            new ValueType[] { ValueType.INTEGER, ValueType.arrayOf(ValueType.BYTE) }, null, true);
        add("java.util.Base64", "getDecoder", ValueType.object("java.util.Base64$Decoder"), new ValueType[0], null, true);
        add("java.util.Base64", "getEncoder", ValueType.object("java.util.Base64$Encoder"), new ValueType[0], null, true);
        add("java.util.Base64", "getMimeEncoder", ValueType.object("java.util.Base64$Encoder"), new ValueType[0], null, true);
        add("java.util.Base64", "getMimeEncoder", ValueType.object("java.util.Base64$Encoder"),
            new ValueType[] { ValueType.INTEGER }, null, true);

        // ===== sun.misc.Unsafe.putLong(long, long) - static-offset variant =====
        add("sun.misc.Unsafe", "putLong", ValueType.VOID,
            new ValueType[] { ValueType.LONG, ValueType.LONG }, null, false);
        add("sun.misc.Unsafe", "putLong", ValueType.VOID,
            new ValueType[] { ValueType.LONG, ValueType.INTEGER }, null, false);
        add("sun.misc.Unsafe", "putLong", ValueType.VOID,
            new ValueType[] { ValueType.object("java.lang.Object"), ValueType.LONG, ValueType.LONG }, null, false);
        add("sun.misc.Unsafe", "getLong", ValueType.LONG,
            new ValueType[] { ValueType.LONG }, null, false);
        add("sun.misc.Unsafe", "getLong", ValueType.LONG,
            new ValueType[] { ValueType.object("java.lang.Object"), ValueType.LONG }, null, false);
        add("sun.misc.Unsafe", "putObject", ValueType.VOID,
            new ValueType[] { ValueType.LONG, ValueType.object("java.lang.Object") }, null, false);
        add("sun.misc.Unsafe", "getObject", ValueType.object("java.lang.Object"),
            new ValueType[] { ValueType.LONG }, null, false);
        add("sun.misc.Unsafe", "putInt", ValueType.VOID,
            new ValueType[] { ValueType.LONG, ValueType.INTEGER }, null, false);
        add("sun.misc.Unsafe", "getInt", ValueType.INTEGER,
            new ValueType[] { ValueType.LONG }, null, false);

        // ===== java.lang.invoke.MethodHandle.invoke() returns Object =====
        add("java.lang.invoke.MethodHandle", "invoke", ValueType.object("java.lang.Object"),
            new ValueType[0], null, false);
        // invokeExact(Thread) returns boolean
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.BOOLEAN,
            new ValueType[] { ValueType.object("java.lang.Thread") }, 0, false);
        // invokeExact(Class, ByteOrder) returns VarHandle
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.object("java.lang.invoke.VarHandle"),
            new ValueType[] { ValueType.object("java.lang.Class"), ValueType.object("java.nio.ByteOrder") }, null, false);

        // ===== java.lang.invoke.MethodHandles.filterArguments (static) =====
        add("java.lang.invoke.MethodHandles", "filterArguments", ValueType.object("java.lang.invoke.MethodHandle"),
            new ValueType[] { ValueType.object("java.lang.invoke.MethodHandle"), ValueType.INTEGER, ValueType.arrayOf(ValueType.object("java.lang.invoke.MethodHandle")) }, null, true);

        // ===== java.security.KeyFactory.generatePrivate / generatePublic =====
        add("java.security.KeyFactory", "generatePrivate", ValueType.object("java.security.PrivateKey"),
            new ValueType[] { ValueType.object("java.security.spec.KeySpec") }, null, false);
        add("java.security.KeyFactory", "generatePublic", ValueType.object("java.security.PublicKey"),
            new ValueType[] { ValueType.object("java.security.spec.KeySpec") }, null, false);

        // ===== Additional MethodHandle.invoke/invokeExact signatures =====
        add("java.lang.invoke.MethodHandle", "invoke", ValueType.object("java.util.List"),
            new ValueType[] { ValueType.object("java.lang.Object") }, null, false);
        add("java.lang.invoke.MethodHandle", "invokeExact", ValueType.BOOLEAN,
            new ValueType[] { ValueType.object("java.lang.Class") }, 0, false);

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
