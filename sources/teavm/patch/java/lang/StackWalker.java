package java.lang;

import java.util.Set;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.function.Consumer;

/**
 * Browser-compatible stub for StackWalker.
 * TeaVM doesn't provide this in its classlib.
 */
public final class StackWalker {

    public enum Option {
        RETAIN_CLASS_REFERENCE,
        SHOW_REFLECT_FRAMES,
        SHOW_HIDDEN_FRAMES
    }

    private static final StackWalker INSTANCE = new StackWalker();

    private StackWalker() {}

    public static StackWalker getInstance() { return INSTANCE; }
    public static StackWalker getInstance(Option option) { return INSTANCE; }
    public static StackWalker getInstance(Set<Option> options) { return INSTANCE; }

    public <T> T walk(Function<? super Stream<StackFrame>, ? extends T> function) {
        return function.apply(Stream.empty());
    }

    public void forEach(Consumer<? super StackFrame> action) {}

    public Class<?> getCallerClass() { return null; }

    public interface StackFrame {
        String getClassName();
        String getMethodName();
        String getFileName();
        int getLineNumber();
        boolean isNativeMethod();
        StackTraceElement toStackTraceElement();
    }
}
