package org.lwjgl.system;

import java.nio.ByteBuffer;

public final class MemoryUtil {
    private MemoryUtil() {
    }

    // ========== Memory allocation/free ==========

    public static ByteBuffer memAlloc(int size) {
        return ByteBuffer.allocate(size);
    }

    public static void memFree(ByteBuffer buffer) {
        // no-op, GC handles it in browser
    }

    public static void memFree(Object buffer) {
        // no-op, overloaded version
    }

    // ========== String encoding ==========

    public static ByteBuffer memASCII(String text) {
        if (text == null) {
            return null;
        }
        byte[] bytes = new byte[text.length() + 1]; // +1 for null terminator
        for (int i = 0; i < text.length(); i++) {
            bytes[i] = (byte) text.charAt(i);
        }
        bytes[text.length()] = 0; // null terminator
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.limit(text.length());
        return buf;
    }

    public static ByteBuffer memUTF8(String text) {
        if (text == null) {
            return null;
        }
        // Simple UTF-8 encoder
        int len = utf8Length(text);
        byte[] bytes = new byte[len + 1]; // +1 for null terminator
        int pos = 0;
        for (int i = 0; i < text.length(); i++) {
            pos = encodeUTF8(text.charAt(i), bytes, pos);
        }
        bytes[len] = 0; // null terminator
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.limit(len);
        return buf;
    }

    public static ByteBuffer memASCII(String text, boolean nullTerminated) {
        if (text == null) {
            return null;
        }
        int len = text.length() + (nullTerminated ? 1 : 0);
        byte[] bytes = new byte[len];
        for (int i = 0; i < text.length(); i++) {
            bytes[i] = (byte) text.charAt(i);
        }
        if (nullTerminated) {
            bytes[text.length()] = 0;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.limit(text.length());
        return buf;
    }

    public static ByteBuffer memUTF8(String text, boolean nullTerminated) {
        if (text == null) {
            return null;
        }
        int utf8Len = utf8Length(text);
        int len = utf8Len + (nullTerminated ? 1 : 0);
        byte[] bytes = new byte[len];
        int pos = 0;
        for (int i = 0; i < text.length(); i++) {
            pos = encodeUTF8(text.charAt(i), bytes, pos);
        }
        if (nullTerminated) {
            bytes[utf8Len] = 0;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.limit(utf8Len);
        return buf;
    }

    // ========== String decoding ==========

    public static String memASCII(ByteBuffer buffer) {
        if (buffer == null) {
            return null;
        }
        int len = buffer.remaining();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            byte b = buffer.get(buffer.position() + i);
            if (b == 0) break; // stop at null terminator
            sb.append((char) (b & 0xFF));
        }
        return sb.toString();
    }

    public static String memUTF8(ByteBuffer buffer) {
        if (buffer == null) {
            return null;
        }
        int len = buffer.remaining();
        StringBuilder sb = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            int b0 = buffer.get(buffer.position() + i) & 0xFF;
            if (b0 == 0) break; // stop at null terminator
            if (b0 < 0x80) {
                sb.append((char) b0);
                i += 1;
            } else if ((b0 & 0xE0) == 0xC0) {
                int b1 = buffer.get(buffer.position() + i + 1) & 0xFF;
                sb.append((char) (((b0 & 0x1F) << 6) | (b1 & 0x3F)));
                i += 2;
            } else if ((b0 & 0xF0) == 0xE0) {
                int b1 = buffer.get(buffer.position() + i + 1) & 0xFF;
                int b2 = buffer.get(buffer.position() + i + 2) & 0xFF;
                sb.append((char) (((b0 & 0x0F) << 12) | ((b1 & 0x3F) << 6) | (b2 & 0x3F)));
                i += 3;
            } else if ((b0 & 0xF8) == 0xF0) {
                int b1 = buffer.get(buffer.position() + i + 1) & 0xFF;
                int b2 = buffer.get(buffer.position() + i + 2) & 0xFF;
                int b3 = buffer.get(buffer.position() + i + 3) & 0xFF;
                int codepoint = ((b0 & 0x07) << 18) | ((b1 & 0x3F) << 12) | ((b2 & 0x3F) << 6) | (b3 & 0x3F);
                // Surrogate pair
                codepoint -= 0x10000;
                sb.append((char) (0xD800 + (codepoint >> 10)));
                sb.append((char) (0xDC00 + (codepoint & 0x3FF)));
                i += 4;
            } else {
                sb.append('?');
                i += 1;
            }
        }
        return sb.toString();
    }

    // ========== Address ==========

    public static long memAddress(ByteBuffer buffer) {
        return 0L; // no real address in browser
    }

    public static long memAddress0(ByteBuffer buffer) {
        return 0L;
    }

    // ========== Copy/fill ==========

    public static void memCopy(ByteBuffer src, ByteBuffer dst) {
        int srcPos = src.position();
        int srcLim = src.limit();
        int len = srcLim - srcPos;
        for (int i = 0; i < len; i++) {
            dst.put(src.get(srcPos + i));
        }
    }

    public static void memCopy(long srcAddr, long dstAddr, long bytes) {
        // no-op in browser - can't do raw memory copies
    }

    public static void memSet(ByteBuffer buffer, byte value, int length) {
        for (int i = 0; i < length; i++) {
            buffer.put(i, value);
        }
    }

    public static void memSet(long addr, int value, long bytes) {
        // no-op in browser
    }

    // ========== UTF-8 helpers ==========

    private static int utf8Length(String text) {
        int len = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 0x80) {
                len += 1;
            } else if (c < 0x800) {
                len += 2;
            } else if (Character.isSurrogate(c)) {
                len += 4; // surrogate pair -> 4 byte UTF-8
                i++; // skip low surrogate
            } else {
                len += 3;
            }
        }
        return len;
    }

    private static int encodeUTF8(char c, byte[] bytes, int pos) {
        if (c < 0x80) {
            bytes[pos] = (byte) c;
            return pos + 1;
        } else if (c < 0x800) {
            bytes[pos] = (byte) (0xC0 | (c >> 6));
            bytes[pos + 1] = (byte) (0x80 | (c & 0x3F));
            return pos + 2;
        } else {
            bytes[pos] = (byte) (0xE0 | (c >> 12));
            bytes[pos + 1] = (byte) (0x80 | ((c >> 6) & 0x3F));
            bytes[pos + 2] = (byte) (0x80 | (c & 0x3F));
            return pos + 3;
        }
    }

    // ========== Additional utility methods ==========

    public static int memLengthASCII(String text) {
        return text.length();
    }

    public static int memLengthUTF8(String text) {
        return utf8Length(text);
    }

    /**
     * MC 26.1.2: Allocates a direct IntBuffer of the given capacity.
     */
    public static java.nio.IntBuffer memAllocInt(int capacity) {
        return java.nio.ByteBuffer.allocateDirect(capacity * 4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
    }

    public static org.lwjgl.system.MemoryUtil.MemoryAllocator getAllocator(boolean tracked) {
        return new org.lwjgl.system.MemoryUtil.MemoryAllocator() {
            public long malloc(long size) { return 0L; }
            public long calloc(long num, long size) { return 0L; }
            public long realloc(long ptr, long size) { return 0L; }
            public void free(long ptr) {}
            public long aligned_alloc(long alignment, long size) { return 0L; }
            public void aligned_free(long ptr) {}
        };
    }

    public static java.nio.ByteBuffer memByteBuffer(long address, int capacity) {
        return java.nio.ByteBuffer.allocateDirect(capacity);
    }
    public static void memPutByte(long address, byte value) {}
    public static void memPutShort(long address, short value) {}
    public static void memPutInt(long address, int value) {}
    public static void memPutFloat(long address, float value) {}
    public static void memPutLong(long address, long value) {}
    public static void memPutDouble(long address, double value) {}
    public static byte memGetByte(long address) { return 0; }
    public static short memGetShort(long address) { return 0; }
    public static int memGetInt(long address) { return 0; }
    public static float memGetFloat(long address) { return 0.0f; }
    public static long memGetLong(long address) { return 0L; }
    public static double memGetDouble(long address) { return 0.0; }

    interface MemoryAllocator {
        long malloc(long size);
        long calloc(long num, long size);
        long realloc(long ptr, long size);
        void free(long ptr);
        long aligned_alloc(long alignment, long size);
        void aligned_free(long ptr);
    }
}
