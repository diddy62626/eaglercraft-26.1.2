package org.lwjgl.opengl;

import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL15 class.
 * Manages buffer objects with int↔JSObject ID mapping (like GL11 textures).
 * Forwards all calls to PlatformOpenGL (WebGL2 backend).
 */
public class GL15 {

        // ================================================================
        // Internal buffer ID mapping (int <-> JSObject)
        // ================================================================

        private static final Map<Integer, JSObject> bufferMap = new HashMap<>();
        private static int nextBufferId = 1;

        public static JSObject getBufferObject(int id) {
                return id == 0 ? null : bufferMap.get(id);
        }

        // ================================================================
        // GL Constants
        // ================================================================

        // --- Buffer targets ---
        public static final int GL_ARRAY_BUFFER = 0x8892;
        public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;

        // --- Buffer usage ---
        public static final int GL_STREAM_DRAW = 0x88E0;
        public static final int GL_STATIC_DRAW = 0x88E4;
        public static final int GL_DYNAMIC_DRAW = 0x88E8;
        public static final int GL_STREAM_READ = 0x88E1;
        public static final int GL_STATIC_READ = 0x88E5;
        public static final int GL_DYNAMIC_READ = 0x88E9;
        public static final int GL_STREAM_COPY = 0x88E2;
        public static final int GL_STATIC_COPY = 0x88E6;
        public static final int GL_DYNAMIC_COPY = 0x88EA;

        // --- Buffer parameters ---
        public static final int GL_BUFFER_SIZE = 0x8764;
        public static final int GL_BUFFER_USAGE = 0x8765;
        public static final int GL_BUFFER_ACCESS = 0x88BB;
        public static final int GL_BUFFER_MAPPED = 0x88BC;

        // --- Buffer access ---
        public static final int GL_READ_ONLY = 0x88B8;
        public static final int GL_WRITE_ONLY = 0x88B9;
        public static final int GL_READ_WRITE = 0x88BA;

        // --- Query ---
        public static final int GL_QUERY_COUNTER_BITS = 0x8864;
        public static final int GL_CURRENT_QUERY = 0x8865;
        public static final int GL_QUERY_RESULT = 0x8866;
        public static final int GL_QUERY_RESULT_AVAILABLE = 0x8867;
        public static final int GL_SAMPLES_PASSED = 0x8914;

        // ================================================================
        // GL Methods
        // ================================================================

        public static int glGenBuffers() {
                JSObject buf = PlatformOpenGL._wglCreateBuffer();
                int id = nextBufferId++;
                bufferMap.put(id, buf);
                return id;
        }

        public static void glGenBuffers(int n, int[] buffers, int offset) {
                for (int i = 0; i < n; i++) {
                        buffers[offset + i] = glGenBuffers();
                }
        }

        /**
         * NIO IntBuffer version.
         */
        public static void glGenBuffers(int n, Object buffers) {
                // TODO: NIO IntBuffer shim
        }

        public static void glBindBuffer(int target, int buffer) {
                PlatformOpenGL._wglBindBuffer(target, getBufferObject(buffer));
        }

        public static void glDeleteBuffers(int buffer) {
                JSObject buf = bufferMap.remove(buffer);
                if (buf != null) {
                        PlatformOpenGL._wglDeleteBuffer(buf);
                }
        }

        public static void glDeleteBuffers(int n, int[] buffers, int offset) {
                for (int i = 0; i < n; i++) {
                        glDeleteBuffers(buffers[offset + i]);
                }
        }

        /**
         * NIO IntBuffer version.
         */
        public static void glDeleteBuffers(int n, Object buffers) {
                // TODO: NIO IntBuffer shim
        }

        // --- glBufferData overloads ---

        /**
         * Allocate an empty buffer of the given size.
         */
        public static void glBufferData(int target, long size, int usage) {
                PlatformOpenGL._wglBufferData(target, (int) size, usage);
        }

        public static void glBufferData(int target, int size, int usage) {
                PlatformOpenGL._wglBufferData(target, size, usage);
        }

        /**
         * Upload float array data.
         */
        public static void glBufferData(int target, float[] data, int usage) {
                Float32Array arr = Float32Array.create(data.length);
                for (int i = 0; i < data.length; i++) {
                        arr.set(i, data[i]);
                }
                PlatformOpenGL._wglBufferData(target, arr, usage);
        }

        /**
         * Upload int array data.
         */
        public static void glBufferData(int target, int[] data, int usage) {
                Int32Array arr = Int32Array.create(data.length);
                for (int i = 0; i < data.length; i++) {
                        arr.set(i, data[i]);
                }
                PlatformOpenGL._wglBufferData(target, arr, usage);
        }

        /**
         * Upload byte array data.
         */
        public static void glBufferData(int target, byte[] data, int usage) {
                Uint8Array arr = Uint8Array.create(data.length);
                for (int i = 0; i < data.length; i++) {
                        arr.set(i, (short)(data[i] & 0xFF));
                }
                PlatformOpenGL._wglBufferData(target, arr, usage);
        }

        /**
         * Upload from a typed array JSObject directly.
         */
        public static void glBufferData(int target, Object data, int usage) {
                if (data instanceof Float32Array) {
                        PlatformOpenGL._wglBufferData(target, (Float32Array) data, usage);
                } else if (data instanceof Int32Array) {
                        PlatformOpenGL._wglBufferData(target, (Int32Array) data, usage);
                } else if (data instanceof Uint8Array) {
                        PlatformOpenGL._wglBufferData(target, (Uint8Array) data, usage);
                } else if (data instanceof ArrayBuffer) {
                        PlatformOpenGL._wglBufferData(target, (ArrayBuffer) data, usage);
                }
                // NIO buffer types handled by shim layer if needed
        }

        // --- glBufferSubData overloads ---

        public static void glBufferSubData(int target, int offset, float[] data) {
                Float32Array arr = Float32Array.create(data.length);
                for (int i = 0; i < data.length; i++) {
                        arr.set(i, data[i]);
                }
                PlatformOpenGL._wglBufferSubData(target, offset, arr);
        }

        public static void glBufferSubData(int target, int offset, int[] data) {
                Int32Array arr = Int32Array.create(data.length);
                for (int i = 0; i < data.length; i++) {
                        arr.set(i, data[i]);
                }
                PlatformOpenGL._wglBufferSubData(target, offset, arr);
        }

        public static void glBufferSubData(int target, int offset, byte[] data) {
                Uint8Array arr = Uint8Array.create(data.length);
                for (int i = 0; i < data.length; i++) {
                        arr.set(i, (short)(data[i] & 0xFF));
                }
                PlatformOpenGL._wglBufferSubData(target, offset, arr);
        }

        /**
         * Upload sub-data from a typed array JSObject directly.
         */
        public static void glBufferSubData(int target, int offset, Object data) {
                if (data instanceof Float32Array) {
                        PlatformOpenGL._wglBufferSubData(target, offset, (Float32Array) data);
                } else if (data instanceof Int32Array) {
                        PlatformOpenGL._wglBufferSubData(target, offset, (Int32Array) data);
                } else if (data instanceof Uint8Array) {
                        PlatformOpenGL._wglBufferSubData(target, offset, (Uint8Array) data);
                } else if (data instanceof ArrayBuffer) {
                        PlatformOpenGL._wglBufferSubData(target, offset, (ArrayBuffer) data);
                }
        }

        // --- Buffer queries ---

        public static void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
                // Buffer parameter queries not directly exposed in PlatformOpenGL
                // Return 0 as a safe default; MC blaze3d rarely queries this
                params[offset] = 0;
        }

        /**
         * Single-value query convenience.
         */
        public static int glGetBufferParameteri(int target, int pname) {
                // Not directly exposed in PlatformOpenGL
                return 0;
        }

        /**
         * NIO IntBuffer version.
         */
        public static void glGetBufferParameteriv(int target, int pname, Object params) {
                // TODO: NIO IntBuffer shim
        }

        /**
         * No-op: glMapBuffer not supported in WebGL2.
         */
        public static Object glMapBuffer(int target, int access) {
                // Not supported in WebGL2
                return null;
        }

        /**
         * No-op: glUnmapBuffer not supported in WebGL2.
         */
        public static boolean glUnmapBuffer(int target) {
                // Not supported in WebGL2
                return true;
        }

        /**
         * No-op: glGetBufferSubData not supported in WebGL2.
         */
        public static void glGetBufferSubData(int target, int offset, int size, Object data) {
                // Not supported in WebGL2
        }
}
