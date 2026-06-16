package org.lwjgl.opengl;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int32Array;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL20 class.
 * Forwards shader/program/uniform calls to PlatformOpenGL (WebGL2 backend).
 */
public class GL20 {

        // ================================================================
        // GL Constants
        // ================================================================

        // --- Shader types ---
        public static final int GL_VERTEX_SHADER = 0x8B31;
        public static final int GL_FRAGMENT_SHADER = 0x8B30;
        public static final int GL_GEOMETRY_SHADER = 0x8DD9;

        // --- Shader/Program parameters ---
        public static final int GL_COMPILE_STATUS = 0x8B81;
        public static final int GL_LINK_STATUS = 0x8B82;
        public static final int GL_VALIDATE_STATUS = 0x8B83;
        public static final int GL_INFO_LOG_LENGTH = 0x8B84;
        public static final int GL_ATTACHED_SHADERS = 0x8B85;
        public static final int GL_ACTIVE_UNIFORMS = 0x8B86;
        public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87;
        public static final int GL_ACTIVE_ATTRIBUTES = 0x8B89;
        public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A;
        public static final int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;
        public static final int GL_CURRENT_PROGRAM = 0x8B8D;
        public static final int GL_SHADER_TYPE = 0x8B4F;
        public static final int GL_DELETE_STATUS = 0x8B80;
        public static final int GL_SHADER_SOURCE_LENGTH = 0x8B88;

        // --- Uniform types ---
        public static final int GL_FLOAT_VEC2 = 0x8B50;
        public static final int GL_FLOAT_VEC3 = 0x8B51;
        public static final int GL_FLOAT_VEC4 = 0x8B52;
        public static final int GL_INT_VEC2 = 0x8B53;
        public static final int GL_INT_VEC3 = 0x8B54;
        public static final int GL_INT_VEC4 = 0x8B55;
        public static final int GL_BOOL = 0x8B56;
        public static final int GL_BOOL_VEC2 = 0x8B57;
        public static final int GL_BOOL_VEC3 = 0x8B58;
        public static final int GL_BOOL_VEC4 = 0x8B59;
        public static final int GL_FLOAT_MAT2 = 0x8B5A;
        public static final int GL_FLOAT_MAT3 = 0x8B5B;
        public static final int GL_FLOAT_MAT4 = 0x8B5C;
        public static final int GL_SAMPLER_2D = 0x8B5E;
        public static final int GL_SAMPLER_CUBE = 0x8B60;

        // --- Blend ---
        public static final int GL_BLEND_EQUATION = 0x8009;
        public static final int GL_BLEND_EQUATION_ALPHA = 0x883D;
        public static final int GL_BLEND_EQUATION_RGB = 0x8009;
        public static final int GL_BLEND_DST_ALPHA = 0x80CA;
        public static final int GL_BLEND_SRC_ALPHA = 0x80CB;
        public static final int GL_BLEND_DST_RGB = 0x80C8;
        public static final int GL_BLEND_SRC_RGB = 0x80C9;

        // --- Draw buffers ---
        public static final int GL_MAX_DRAW_BUFFERS = 0x8824;
        public static final int GL_DRAW_BUFFER0 = 0x8825;
        public static final int GL_DRAW_BUFFER1 = 0x8826;

        // --- Stencil back ---
        public static final int GL_STENCIL_BACK_FUNC = 0x8800;
        public static final int GL_STENCIL_BACK_FAIL = 0x8801;
        public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
        public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
        public static final int GL_STENCIL_BACK_REF = 0x8CA3;
        public static final int GL_STENCIL_BACK_VALUE_MASK = 0x8CA4;
        public static final int GL_STENCIL_BACK_WRITEMASK = 0x8CA5;

        // ================================================================
        // Shader / Program Management
        // ================================================================

        // --- Program ID mapping (int <-> JSObject) ---
        private static final java.util.Map<Integer, JSObject> programMap = new java.util.HashMap<>();
        private static int nextProgramId = 1;

        private static final java.util.Map<Integer, JSObject> shaderMap = new java.util.HashMap<>();
        private static int nextShaderId = 1;

        public static JSObject getProgramObject(int id) {
                return id == 0 ? null : programMap.get(id);
        }

        public static JSObject getShaderObject(int id) {
                return id == 0 ? null : shaderMap.get(id);
        }

        // --- Shader methods ---

        public static int glCreateShader(int type) {
                JSObject shader = PlatformOpenGL._wglCreateShader(type);
                int id = nextShaderId++;
                shaderMap.put(id, shader);
                return id;
        }

        public static void glShaderSource(int shader, String source) {
                PlatformOpenGL._wglShaderSource(getShaderObject(shader), source);
        }

        public static void glCompileShader(int shader) {
                PlatformOpenGL._wglCompileShader(getShaderObject(shader));
        }

        public static void glDeleteShader(int shader) {
                JSObject obj = shaderMap.remove(shader);
                if (obj != null) {
                        PlatformOpenGL._wglDeleteShader(obj);
                }
        }

        public static int glGetShaderi(int shader, int pname) {
                JSObject obj = getShaderObject(shader);
                if (obj == null) return 0;
                switch (pname) {
                        case GL_COMPILE_STATUS:
                                return PlatformOpenGL._wglGetShaderCompiled(obj) ? GL11.GL_TRUE : GL11.GL_FALSE;
                        case GL_DELETE_STATUS:
                                return 0; // Not easily queryable
                        case GL_SHADER_TYPE:
                        case GL_INFO_LOG_LENGTH:
                        case GL_SHADER_SOURCE_LENGTH:
                        default:
                                return 0;
                }
        }

        public static String glGetShaderInfoLog(int shader) {
                return PlatformOpenGL._wglGetShaderInfoLog(getShaderObject(shader));
        }

        // --- Program methods ---

        public static int glCreateProgram() {
                JSObject program = PlatformOpenGL._wglCreateProgram();
                int id = nextProgramId++;
                programMap.put(id, program);
                return id;
        }

        public static void glAttachShader(int program, int shader) {
                PlatformOpenGL._wglAttachShader(getProgramObject(program), getShaderObject(shader));
        }

        public static void glDetachShader(int program, int shader) {
                PlatformOpenGL._wglDetachShader(getProgramObject(program), getShaderObject(shader));
        }

        public static void glLinkProgram(int program) {
                PlatformOpenGL._wglLinkProgram(getProgramObject(program));
        }

        public static void glUseProgram(int program) {
                PlatformOpenGL._wglUseProgram(getProgramObject(program));
        }

        public static void glDeleteProgram(int program) {
                JSObject obj = programMap.remove(program);
                if (obj != null) {
                        PlatformOpenGL._wglDeleteProgram(obj);
                }
        }

        public static void glValidateProgram(int program) {
                PlatformOpenGL._wglValidateProgram(getProgramObject(program));
        }

        public static int glGetProgrami(int program, int pname) {
                JSObject obj = getProgramObject(program);
                if (obj == null) return 0;
                switch (pname) {
                        case GL_LINK_STATUS:
                                return PlatformOpenGL._wglGetProgramLinked(obj) ? GL11.GL_TRUE : GL11.GL_FALSE;
                        case GL_VALIDATE_STATUS:
                                return GL11.GL_TRUE; // Assume valid
                        case GL_ATTACHED_SHADERS:
                        case GL_ACTIVE_UNIFORMS:
                        case GL_ACTIVE_ATTRIBUTES:
                        case GL_INFO_LOG_LENGTH:
                        default:
                                return 0;
                }
        }

        public static String glGetProgramInfoLog(int program) {
                return PlatformOpenGL._wglGetProgramInfoLog(getProgramObject(program));
        }

        // --- Uniform / Attribute locations ---
        // Use JSObject-based location mapping

        private static final java.util.Map<Integer, JSObject> uniformLocMap = new java.util.HashMap<>();
        private static int nextUniformLocId = 1;

        public static int glGetUniformLocation(int program, String name) {
                JSObject loc = PlatformOpenGL._wglGetUniformLocation(getProgramObject(program), name);
                if (loc == null) return -1;
                int id = nextUniformLocId++;
                uniformLocMap.put(id, loc);
                return id;
        }

        public static int glGetAttribLocation(int program, String name) {
                return PlatformOpenGL._wglGetAttribLocation(getProgramObject(program), name);
        }

        private static JSObject getUniformLoc(int location) {
                return location <= 0 ? null : uniformLocMap.get(location);
        }

        // --- Vertex attrib ---

        public static void glEnableVertexAttribArray(int index) {
                PlatformOpenGL._wglEnableVertexAttribArray(index);
        }

        public static void glDisableVertexAttribArray(int index) {
                PlatformOpenGL._wglDisableVertexAttribArray(index);
        }

        public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset) {
                PlatformOpenGL._wglVertexAttribPointer(index, size, type, normalized, stride, offset);
        }

        public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long offset) {
                PlatformOpenGL._wglVertexAttribPointer(index, size, type, normalized, stride, (int) offset);
        }

        // --- Uniform scalar ---

        public static void glUniform1f(int location, float x) {
                PlatformOpenGL._wglUniform1f(getUniformLoc(location), x);
        }

        public static void glUniform2f(int location, float x, float y) {
                PlatformOpenGL._wglUniform2f(getUniformLoc(location), x, y);
        }

        public static void glUniform3f(int location, float x, float y, float z) {
                PlatformOpenGL._wglUniform3f(getUniformLoc(location), x, y, z);
        }

        public static void glUniform4f(int location, float x, float y, float z, float w) {
                PlatformOpenGL._wglUniform4f(getUniformLoc(location), x, y, z, w);
        }

        public static void glUniform1i(int location, int x) {
                PlatformOpenGL._wglUniform1i(getUniformLoc(location), x);
        }

        public static void glUniform2i(int location, int x, int y) {
                PlatformOpenGL._wglUniform2i(getUniformLoc(location), x, y);
        }

        public static void glUniform3i(int location, int x, int y, int z) {
                PlatformOpenGL._wglUniform3i(getUniformLoc(location), x, y, z);
        }

        public static void glUniform4i(int location, int x, int y, int z, int w) {
                PlatformOpenGL._wglUniform4i(getUniformLoc(location), x, y, z, w);
        }

        // --- Uniform vector ---

        public static void glUniform1fv(int location, Float32Array v) {
                PlatformOpenGL._wglUniform1fv(getUniformLoc(location), v);
        }

        public static void glUniform2fv(int location, Float32Array v) {
                PlatformOpenGL._wglUniform2fv(getUniformLoc(location), v);
        }

        public static void glUniform3fv(int location, Float32Array v) {
                PlatformOpenGL._wglUniform3fv(getUniformLoc(location), v);
        }

        public static void glUniform4fv(int location, Float32Array v) {
                PlatformOpenGL._wglUniform4fv(getUniformLoc(location), v);
        }

        public static void glUniform1iv(int location, Int32Array v) {
                PlatformOpenGL._wglUniform1iv(getUniformLoc(location), v);
        }

        public static void glUniform2iv(int location, Int32Array v) {
                PlatformOpenGL._wglUniform2iv(getUniformLoc(location), v);
        }

        public static void glUniform3iv(int location, Int32Array v) {
                PlatformOpenGL._wglUniform3iv(getUniformLoc(location), v);
        }

        public static void glUniform4iv(int location, Int32Array v) {
                PlatformOpenGL._wglUniform4iv(getUniformLoc(location), v);
        }

        // --- Uniform vector (Java array) ---

        public static void glUniform1fv(int location, float[] v, int offset) {
                Float32Array arr = Float32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform1fv(getUniformLoc(location), arr);
        }

        public static void glUniform2fv(int location, float[] v, int offset) {
                Float32Array arr = Float32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform2fv(getUniformLoc(location), arr);
        }

        public static void glUniform3fv(int location, float[] v, int offset) {
                Float32Array arr = Float32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform3fv(getUniformLoc(location), arr);
        }

        public static void glUniform4fv(int location, float[] v, int offset) {
                Float32Array arr = Float32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform4fv(getUniformLoc(location), arr);
        }

        public static void glUniform1iv(int location, int[] v, int offset) {
                Int32Array arr = Int32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform1iv(getUniformLoc(location), arr);
        }

        public static void glUniform2iv(int location, int[] v, int offset) {
                Int32Array arr = Int32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform2iv(getUniformLoc(location), arr);
        }

        public static void glUniform3iv(int location, int[] v, int offset) {
                Int32Array arr = Int32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform3iv(getUniformLoc(location), arr);
        }

        public static void glUniform4iv(int location, int[] v, int offset) {
                Int32Array arr = Int32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniform4iv(getUniformLoc(location), arr);
        }

        // --- NIO buffer versions of uniform vectors ---

        public static void glUniform1fv(int location, Object v) {
                // TODO: NIO FloatBuffer shim
        }

        public static void glUniform2fv(int location, Object v) {
                // TODO: NIO FloatBuffer shim
        }

        public static void glUniform3fv(int location, Object v) {
                // TODO: NIO FloatBuffer shim
        }

        public static void glUniform4fv(int location, Object v) {
                // TODO: NIO FloatBuffer shim
        }

        public static void glUniform1iv(int location, Object v) {
                // TODO: NIO IntBuffer shim
        }

        public static void glUniform2iv(int location, Object v) {
                // TODO: NIO IntBuffer shim
        }

        public static void glUniform3iv(int location, Object v) {
                // TODO: NIO IntBuffer shim
        }

        public static void glUniform4iv(int location, Object v) {
                // TODO: NIO IntBuffer shim
        }

        // --- Uniform matrix ---

        public static void glUniformMatrix3fv(int location, boolean transpose, Float32Array v) {
                PlatformOpenGL._wglUniformMatrix3fv(getUniformLoc(location), transpose, v);
        }

        public static void glUniformMatrix4fv(int location, boolean transpose, Float32Array v) {
                PlatformOpenGL._wglUniformMatrix4fv(getUniformLoc(location), transpose, v);
        }

        public static void glUniformMatrix3fv(int location, boolean transpose, float[] v, int offset) {
                Float32Array arr = Float32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniformMatrix3fv(getUniformLoc(location), transpose, arr);
        }

        public static void glUniformMatrix4fv(int location, boolean transpose, float[] v, int offset) {
                Float32Array arr = Float32Array.create(v.length - offset);
                for (int i = 0; i < arr.getLength(); i++) arr.set(i, v[offset + i]);
                PlatformOpenGL._wglUniformMatrix4fv(getUniformLoc(location), transpose, arr);
        }

        public static void glUniformMatrix3fv(int location, boolean transpose, Object v) {
                // TODO: NIO FloatBuffer shim
        }

        public static void glUniformMatrix4fv(int location, boolean transpose, Object v) {
                // TODO: NIO FloatBuffer shim
        }

        // --- Vertex attrib values ---

        public static void glVertexAttrib1f(int index, float x) {
                PlatformOpenGL._wglVertexAttrib1f(index, x);
        }

        public static void glVertexAttrib2f(int index, float x, float y) {
                PlatformOpenGL._wglVertexAttrib2f(index, x, y);
        }

        public static void glVertexAttrib3f(int index, float x, float y, float z) {
                PlatformOpenGL._wglVertexAttrib3f(index, x, y, z);
        }

        public static void glVertexAttrib4f(int index, float x, float y, float z, float w) {
                PlatformOpenGL._wglVertexAttrib4f(index, x, y, z, w);
        }

        // --- Vertex attrib queries ---

        public static void glGetVertexAttribfv(int index, int pname, float[] params, int offset) {
                // Not directly exposed in PlatformOpenGL; return 0s
                if (params != null && params.length > offset) {
                        params[offset] = 0;
                }
        }

        public static void glGetVertexAttribfv(int index, int pname, Object params) {
                // TODO: NIO FloatBuffer shim
        }

        public static void glGetVertexAttribiv(int index, int pname, int[] params, int offset) {
                // Not directly exposed in PlatformOpenGL; return 0s
                if (params != null && params.length > offset) {
                        params[offset] = 0;
                }
        }

        public static void glGetVertexAttribiv(int index, int pname, Object params) {
                // TODO: NIO IntBuffer shim
        }

        // --- Bind attrib location ---

        public static void glBindAttribLocation(int program, int index, String name) {
                // bindAttribLocation not exposed in PlatformOpenGL; use layout qualifiers in shaders instead
        }

        // --- Draw buffers ---

        public static void glDrawBuffers(java.nio.IntBuffer buffers) {
                int count = buffers.remaining();
                Int32Array arr = Int32Array.create(count);
                for (int i = 0; i < count; i++) {
                        arr.set(i, buffers.get());
                }
                PlatformOpenGL._wglDrawBuffers(arr);
        }

        // --- Get active uniform/attrib ---

        public static String glGetActiveUniform(int program, int index, int bufSize, int[] length, int[] size, int[] type, byte[] name) {
                // Not directly exposed; return empty string
                return "";
        }

        public static String glGetActiveAttrib(int program, int index, int bufSize, int[] length, int[] size, int[] type, byte[] name) {
                // Not directly exposed; return empty string
                return "";
        }
}
