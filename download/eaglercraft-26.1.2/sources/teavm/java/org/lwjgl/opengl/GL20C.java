package org.lwjgl.opengl;

/**
 * TeaVM-compatible stub for LWJGL's GL20C class (Core profile OpenGL 2.0).
 * Delegates to GL20 methods. WebGL2 supports the core profile subset.
 */
public class GL20C {

    // Constants (same as GL20)
    public static final int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;
    public static final int GL_CURRENT_PROGRAM = 0x8648;
    public static final int GL_SHADER_TYPE = 0x8B4F;
    public static final int GL_DELETE_STATUS = 0x8B80;
    public static final int GL_LINK_STATUS = 0x8B82;
    public static final int GL_VALIDATE_STATUS = 0x8B83;
    public static final int GL_COMPILE_STATUS = 0x8B81;
    public static final int GL_INFO_LOG_LENGTH = 0x8B84;
    public static final int GL_ATTACHED_SHADERS = 0x8B85;
    public static final int GL_ACTIVE_UNIFORMS = 0x8B86;
    public static final int GL_ACTIVE_ATTRIBUTES = 0x8B89;

    public static int glCreateShader(int type) {
        return GL20.glCreateShader(type);
    }

    public static void glShaderSource(int shader, String source) {
        GL20.glShaderSource(shader, source);
    }

    public static void glCompileShader(int shader) {
        GL20.glCompileShader(shader);
    }

    public static int glCreateProgram() {
        return GL20.glCreateProgram();
    }

    public static void glAttachShader(int program, int shader) {
        GL20.glAttachShader(program, shader);
    }

    public static void glLinkProgram(int program) {
        GL20.glLinkProgram(program);
    }

    public static void glUseProgram(int program) {
        GL20.glUseProgram(program);
    }

    public static void glDeleteShader(int shader) {
        GL20.glDeleteShader(shader);
    }

    public static void glDeleteProgram(int program) {
        GL20.glDeleteProgram(program);
    }

    public static int glGetShaderi(int shader, int pname) {
        return GL20.glGetShaderi(shader, pname);
    }

    public static int glGetProgrami(int program, int pname) {
        return GL20.glGetProgrami(program, pname);
    }

    public static String glGetShaderInfoLog(int shader) {
        return GL20.glGetShaderInfoLog(shader);
    }

    public static String glGetProgramInfoLog(int program) {
        return GL20.glGetProgramInfoLog(program);
    }

    public static int glGetUniformLocation(int program, String name) {
        return GL20.glGetUniformLocation(program, name);
    }

    public static void glUniform1i(int location, int v0) {
        GL20.glUniform1i(location, v0);
    }

    public static void glUniform1f(int location, float v0) {
        GL20.glUniform1f(location, v0);
    }

    public static void glUniform2f(int location, float v0, float v1) {
        GL20.glUniform2f(location, v0, v1);
    }

    public static void glUniform3f(int location, float v0, float v1, float v2) {
        GL20.glUniform3f(location, v0, v1, v2);
    }

    public static void glUniform4f(int location, float v0, float v1, float v2, float v3) {
        GL20.glUniform4f(location, v0, v1, v2, v3);
    }

    public static void glUniformMatrix4fv(int location, boolean transpose, java.nio.FloatBuffer value) {
        GL20.glUniformMatrix4fv(location, transpose, value);
    }

    public static void glBindAttribLocation(int program, int index, String name) {
        GL20.glBindAttribLocation(program, index, name);
    }

    public static int glGetAttribLocation(int program, String name) {
        return GL20.glGetAttribLocation(program, name);
    }

    public static void glEnableVertexAttribArray(int index) {
        GL20.glEnableVertexAttribArray(index);
    }

    public static void glDisableVertexAttribArray(int index) {
        GL20.glDisableVertexAttribArray(index);
    }

    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long offset) {
        GL20.glVertexAttribPointer(index, size, type, normalized, stride, offset);
    }

    public static void glVertexAttribIPointer(int index, int size, int type, int stride, long offset) {
        GL30.glVertexAttribIPointer(index, size, type, stride, offset);
    }

    public static void glDrawBuffers(java.nio.IntBuffer buffers) {
        GL20.glDrawBuffers(buffers);
    }
}
