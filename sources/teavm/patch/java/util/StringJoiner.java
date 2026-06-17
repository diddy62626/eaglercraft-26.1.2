package java.util;

/**
 * TeaVM stub for java.util.StringJoiner.
 *
 * Browser-compatible implementation backed by StringBuilder.
 */
public final class StringJoiner {
    private final String prefix;
    private final String delimiter;
    private final String suffix;
    private final StringBuilder value = new StringBuilder();
    private boolean empty = true;

    public StringJoiner(CharSequence delimiter) {
        this("", delimiter, "");
    }

    public StringJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        this.prefix = prefix.toString();
        this.delimiter = delimiter.toString();
        this.suffix = suffix.toString();
    }

    public StringJoiner setEmptyValue(CharSequence emptyValue) {
        return this;
    }

    public StringJoiner add(CharSequence newElement) {
        if (!empty) {
            value.append(delimiter);
        }
        value.append(newElement);
        empty = false;
        return this;
    }

    public StringJoiner merge(StringJoiner other) {
        if (!other.empty) {
            add(other.value.toString());
        }
        return this;
    }

    public int length() {
        return value.length() + (empty ? 0 : prefix.length() + suffix.length());
    }

    public String toString() {
        if (empty) {
            return prefix + suffix;
        }
        return prefix + value.toString() + suffix;
    }
}
