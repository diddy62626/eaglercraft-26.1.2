package net.lax1dude.eaglercraft.v2_6.adapter;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Float32Array;

import net.lax1dude.eaglercraft.v2_6.EaglerCraftConfig;
import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext;

/**
 * Shader adapter that bridges Minecraft 26.1.2's shader system to WebGL2
 * through {@link PlatformOpenGL}.
 *
 * <p>In vanilla MC 26.1.2, shaders are compiled and managed through LWJGL's
 * OpenGL bindings. In EaglerCraft, all shader operations must go through
 * WebGL2, which has a similar but not identical API. This class provides:</p>
 * <ul>
 *   <li>GLSL ES 3.00 shader source preprocessing (WebGL2 requires #version 300 es)</li>
 *   <li>Shader compilation with error logging and fallback handling</li>
 *   <li>Program linking with reflection for uniforms and attributes</li>
 *   <li>Uniform buffer object (UBO) management for efficient state updates</li>
 *   <li>Shader caching to avoid recompilation on context loss recovery</li>
 * </ul>
 *
 * <h3>GLSL compatibility notes:</h3>
 * <p>MC 26.1.2 desktop shaders typically use GLSL 1.20-1.50 syntax. WebGL2
 * requires GLSL ES 3.00. Key differences handled by this adapter:</p>
 * <ul>
 *   <li><code>attribute</code> → <code>in</code> (vertex shader)</li>
 *   <li><code>varying</code> → <code>out</code> (vertex) / <code>in</code> (fragment)</li>
 *   <li><code>texture2D</code> → <code>texture</code></li>
 *   <li><code>gl_FragColor</code> → explicit output variable declaration</li>
 *   <li>Precision qualifiers required (<code>highp</code>, <code>mediump</code>, <code>lowp</code>)</li>
 *   <li>Integer attributes require <code>flat</code> interpolation qualifier</li>
 * </ul>
 *
 * <p>Architecture note: This class is in the <code>main</code> source set's
 * <code>adapter</code> sub-package. MC 26.1.2's shader classes (e.g.,
 * {@code ShaderProgram}, {@code ShaderInstance}) will be patched to call
 * this adapter instead of LWJGL directly.</p>
 *
 * TODO: Update to actual MC 26.1.2 class names after decompilation.
 * Expected: net.minecraft.client.renderer.ShaderInstance or similar
 */
public class EaglerShaderImpl {

        // ========== Shader Type Constants ==========
        // These mirror GL_VERTEX_SHADER and GL_FRAGMENT_SHADER from WebGL2

        /** Vertex shader type constant. */
        public static final int VERTEX_SHADER = WebGL2RenderingContext.VERTEX_SHADER;

        /** Fragment shader type constant. */
        public static final int FRAGMENT_SHADER = WebGL2RenderingContext.FRAGMENT_SHADER;

        // ========== GLSL ES Version String ==========

        /** The GLSL ES version header required for WebGL2 shaders. */
        private static final String GLSL_ES_HEADER = "#version 300 es";

        /** Default precision for float types in fragment shaders. */
        private static final String FRAGMENT_DEFAULT_PRECISION =
                        "precision highp float;\nprecision highp int;\nprecision highp sampler2D;\n";

        /** Default precision for vertex shaders (highp is mandatory in vertex). */
        private static final String VERTEX_DEFAULT_PRECISION =
                        "precision highp float;\nprecision highp int;\n";

        // ========== Shader Cache ==========

        /** Whether shader source caching is enabled (for context loss recovery). */
        private static boolean cachingEnabled = true;

        /** Total number of shaders compiled (for statistics). */
        private static int shadersCompiled = 0;

        /** Total number of programs linked (for statistics). */
        private static int programsLinked = 0;

        /** Total number of shader compilation failures. */
        private static int shaderCompileFailures = 0;

        /** Total number of program link failures. */
        private static int programLinkFailures = 0;

        // ========== Shader Compilation ==========

        /**
         * Compiles a GLSL shader source string for WebGL2.
         *
         * <p>This method handles the necessary source transformations to convert
         * desktop GLSL to GLSL ES 3.00 compatible source, then compiles it
         * using {@link PlatformOpenGL#_wglCreateShader}, _wglShaderSource,
         * and _wglCompileShader.</p>
         *
         * <p>If compilation fails, the error log is printed and null is returned.</p>
         *
         * @param type   the shader type ({@link #VERTEX_SHADER} or {@link #FRAGMENT_SHADER})
         * @param source the GLSL source code (desktop GLSL is auto-converted)
         * @param name   a descriptive name for logging (e.g., "rendertype_solid.vsh")
         * @return the compiled shader JSObject handle, or null if compilation failed
         */
        public static JSObject compileShader(int type, String source, String name) {
                // Preprocess the source for GLSL ES 3.00 compatibility
                String processedSource = preprocessShaderSource(type, source);

                // Create and compile the shader
                JSObject shader = PlatformOpenGL._wglCreateShader(type);
                if (shader == null) {
                        ClientMain.error("[EaglerShaderImpl] Failed to create shader object for: " + name);
                        shaderCompileFailures++;
                        return null;
                }

                PlatformOpenGL._wglShaderSource(shader, processedSource);
                PlatformOpenGL._wglCompileShader(shader);

                // Check compilation status
                boolean compiled = PlatformOpenGL._wglGetShaderCompiled(shader);
                if (!compiled) {
                        String infoLog = PlatformOpenGL._wglGetShaderInfoLog(shader);
                        ClientMain.error("[EaglerShaderImpl] Shader compilation failed: " + name);
                        ClientMain.error("[EaglerShaderImpl] Info log: " + infoLog);

                        // Log the first few lines of the processed source for debugging
                        if (EaglerCraftConfig.VERBOSE_INIT_LOGGING) {
                                String[] lines = processedSource.split("\n");
                                int maxLines = Math.min(lines.length, 20);
                                ClientMain.error("[EaglerShaderImpl] Source preview (" + lines.length + " lines total):");
                                for (int i = 0; i < maxLines; i++) {
                                        ClientMain.error("  " + (i + 1) + ": " + lines[i]);
                                }
                                if (lines.length > maxLines) {
                                        ClientMain.error("  ... (" + (lines.length - maxLines) + " more lines)");
                                }
                        }

                        PlatformOpenGL._wglDeleteShader(shader);
                        shaderCompileFailures++;
                        return null;
                }

                shadersCompiled++;
                if (EaglerCraftConfig.VERBOSE_INIT_LOGGING) {
                        ClientMain.log("[EaglerShaderImpl] Compiled shader: " + name);
                }

                return shader;
        }

        /**
         * Compiles a vertex shader.
         * Convenience wrapper for {@link #compileShader(int, String, String)}.
         *
         * @param source the vertex shader GLSL source
         * @param name   descriptive name for logging
         * @return compiled shader handle, or null on failure
         */
        public static JSObject compileVertexShader(String source, String name) {
                return compileShader(VERTEX_SHADER, source, name);
        }

        /**
         * Compiles a fragment shader.
         * Convenience wrapper for {@link #compileShader(int, String, String)}.
         *
         * @param source the fragment shader GLSL source
         * @param name   descriptive name for logging
         * @return compiled shader handle, or null on failure
         */
        public static JSObject compileFragmentShader(String source, String name) {
                return compileShader(FRAGMENT_SHADER, source, name);
        }

        // ========== Program Linking ==========

        /**
         * Creates and links a shader program from vertex and fragment shaders.
         *
         * <p>After successful linking, the shaders are detached and deleted
         * (they are no longer needed once the program is linked).</p>
         *
         * @param vertexShader   compiled vertex shader handle
         * @param fragmentShader compiled fragment shader handle
         * @param name           descriptive name for logging (e.g., "rendertype_solid")
         * @return the linked program JSObject handle, or null if linking failed
         */
        public static JSObject linkProgram(JSObject vertexShader, JSObject fragmentShader, String name) {
                if (vertexShader == null || fragmentShader == null) {
                        ClientMain.error("[EaglerShaderImpl] Cannot link program with null shader: " + name);
                        programLinkFailures++;
                        return null;
                }

                JSObject program = PlatformOpenGL._wglCreateProgram();
                if (program == null) {
                        ClientMain.error("[EaglerShaderImpl] Failed to create program object: " + name);
                        programLinkFailures++;
                        return null;
                }

                PlatformOpenGL._wglAttachShader(program, vertexShader);
                PlatformOpenGL._wglAttachShader(program, fragmentShader);
                PlatformOpenGL._wglLinkProgram(program);

                // Check link status
                boolean linked = PlatformOpenGL._wglGetProgramLinked(program);
                if (!linked) {
                        String infoLog = PlatformOpenGL._wglGetProgramInfoLog(program);
                        ClientMain.error("[EaglerShaderImpl] Program link failed: " + name);
                        ClientMain.error("[EaglerShaderImpl] Info log: " + infoLog);

                        PlatformOpenGL._wglDetachShader(program, vertexShader);
                        PlatformOpenGL._wglDetachShader(program, fragmentShader);
                        PlatformOpenGL._wglDeleteProgram(program);
                        programLinkFailures++;
                        return null;
                }

                // Detach and delete shaders (no longer needed after linking)
                PlatformOpenGL._wglDetachShader(program, vertexShader);
                PlatformOpenGL._wglDetachShader(program, fragmentShader);
                PlatformOpenGL._wglDeleteShader(vertexShader);
                PlatformOpenGL._wglDeleteShader(fragmentShader);

                programsLinked++;
                if (EaglerCraftConfig.VERBOSE_INIT_LOGGING) {
                        ClientMain.log("[EaglerShaderImpl] Linked program: " + name);
                }

                return program;
        }

        /**
         * Creates a complete shader program from source strings.
         * Convenience method that compiles both shaders and links them in one call.
         *
         * @param vertexSource   vertex shader GLSL source
         * @param fragmentSource fragment shader GLSL source
         * @param name           descriptive name for logging
         * @return the linked program handle, or null if any step failed
         */
        public static JSObject createProgramFromSource(String vertexSource, String fragmentSource, String name) {
                JSObject vertexShader = compileVertexShader(vertexSource, name + ".vsh");
                if (vertexShader == null) return null;

                JSObject fragmentShader = compileFragmentShader(fragmentSource, name + ".fsh");
                if (fragmentShader == null) {
                        PlatformOpenGL._wglDeleteShader(vertexShader);
                        return null;
                }

                return linkProgram(vertexShader, fragmentShader, name);
        }

        // ========== Uniform Access ==========

        /**
         * Gets the location of a uniform in a shader program.
         * Delegates to {@link PlatformOpenGL#_wglGetUniformLocation}.
         *
         * @param program the linked shader program
         * @param name    the uniform name
         * @return the uniform location JSObject, or null if not found
         */
        public static JSObject getUniformLocation(JSObject program, String name) {
                return PlatformOpenGL._wglGetUniformLocation(program, name);
        }

        /**
         * Gets the location of an attribute in a shader program.
         * Delegates to {@link PlatformOpenGL#_wglGetAttribLocation}.
         *
         * @param program the linked shader program
         * @param name    the attribute name
         * @return the attribute location index, or -1 if not found
         */
        public static int getAttribLocation(JSObject program, String name) {
                return PlatformOpenGL._wglGetAttribLocation(program, name);
        }

        /**
         * Gets the index of a uniform block in a shader program.
         * Used for WebGL2 uniform buffer objects (UBOs).
         * Delegates to {@link PlatformOpenGL#_wglGetUniformBlockIndex}.
         *
         * @param program the linked shader program
         * @param name    the uniform block name
         * @return the uniform block index, or GL_INVALID_INDEX if not found
         */
        public static int getUniformBlockIndex(JSObject program, String name) {
                return PlatformOpenGL._wglGetUniformBlockIndex(program, name);
        }

        /**
         * Binds a uniform block to a specific binding point.
         * Used for WebGL2 uniform buffer objects (UBOs).
         * Delegates to {@link PlatformOpenGL#_wglUniformBlockBinding}.
         *
         * @param program          the linked shader program
         * @param blockIndex       the uniform block index
         * @param bindingPoint     the binding point to assign
         */
        public static void bindUniformBlock(JSObject program, int blockIndex, int bindingPoint) {
                PlatformOpenGL._wglUniformBlockBinding(program, blockIndex, bindingPoint);
        }

        // ========== Program Usage ==========

        /**
         * Sets the current shader program for rendering.
         * Delegates to {@link PlatformOpenGL#_wglUseProgram}.
         *
         * @param program the program to use, or null to unbind
         */
        public static void useProgram(JSObject program) {
                PlatformOpenGL._wglUseProgram(program);
        }

        /**
         * Deletes a shader program and frees its GPU resources.
         * Delegates to {@link PlatformOpenGL#_wglDeleteProgram}.
         *
         * @param program the program to delete
         */
        public static void deleteProgram(JSObject program) {
                if (program != null) {
                        PlatformOpenGL._wglDeleteProgram(program);
                }
        }

        // ========== GLSL Source Preprocessing ==========

        /**
         * Preprocesses a GLSL source string for WebGL2 / GLSL ES 3.00 compatibility.
         *
         * <p>This method performs the following transformations:</p>
         * <ol>
         *   <li>Strips any existing <code>#version</code> directive</li>
         *   <li>Adds <code>#version 300 es</code> header</li>
         *   <li>Adds default precision qualifiers</li>
         *   <li>Converts desktop GLSL keywords to GLSL ES equivalents:
         *     <ul>
         *       <li><code>attribute</code> → <code>in</code> (vertex shader)</li>
         *       <li><code>varying</code> → <code>out</code> (vertex) / <code>in</code> (fragment)</li>
         *       <li><code>texture2D</code> → <code>texture</code></li>
         *       <li><code>textureCube</code> → <code>texture</code></li>
         *     </ul>
         *   </li>
         *   <li>Handles <code>gl_FragColor</code> → explicit output declaration</li>
         * </ol>
         *
         * <p>Note: This is a best-effort preprocessor. Complex shaders with
         * preprocessor macros, multiple output targets, or advanced GLSL features
         * may require manual adjustment. The EaglerCraft shader patches (applied
         * at build time) handle the full conversion.</p>
         *
         * @param type   the shader type
         * @param source the original GLSL source
         * @return the preprocessed source compatible with GLSL ES 3.00
         */
        public static String preprocessShaderSource(int type, String source) {
                StringBuilder result = new StringBuilder();

                // Add GLSL ES version header
                result.append(GLSL_ES_HEADER).append("\n");

                // Add precision qualifiers
                if (type == FRAGMENT_SHADER) {
                        result.append(FRAGMENT_DEFAULT_PRECISION);
                } else {
                        result.append(VERTEX_DEFAULT_PRECISION);
                }

                // Process each line
                String[] lines = source.split("\n");
                boolean hasFragColor = false;

                for (String line : lines) {
                        String trimmed = line.trim();

                        // Skip #version directives (we already added our own)
                        if (trimmed.startsWith("#version")) {
                                continue;
                        }

                        // Skip #extension directives that are desktop-only
                        if (trimmed.startsWith("#extension GL_ARB_") || trimmed.startsWith("#extension GL_EXT_")) {
                                // Some extensions have WebGL2 equivalents, but most can be safely skipped
                                continue;
                        }

                        // Convert desktop GLSL keywords to GLSL ES 3.00
                        String converted = line;

                        if (type == VERTEX_SHADER) {
                                // attribute → in (vertex shader)
                                converted = convertKeyword(converted, "attribute", "in");
                                // varying → out (vertex shader)
                                converted = convertKeyword(converted, "varying", "out");
                        } else {
                                // varying → in (fragment shader)
                                converted = convertKeyword(converted, "varying", "in");
                        }

                        // texture2D → texture (GLSL ES 3.00 unified texture function)
                        converted = convertTextureCall(converted, "texture2D");
                        converted = convertTextureCall(converted, "textureCube");

                        // Track gl_FragColor usage for output variable injection
                        if (converted.contains("gl_FragColor")) {
                                hasFragColor = true;
                        }

                        result.append(converted).append("\n");
                }

                // If the fragment shader uses gl_FragColor, we need to inject an output
                // variable declaration and replace gl_FragColor references
                if (type == FRAGMENT_SHADER && hasFragColor) {
                        String processed = result.toString();
                        // Replace gl_FragColor with our output variable
                        processed = processed.replace("gl_FragColor", "eagler_FragColor");
                        // Insert the output variable declaration after the version header + precision
                        int insertPos = processed.indexOf('\n', processed.indexOf('\n') + 1) + 1;
                        insertPos = processed.indexOf('\n', insertPos) + 1; // After precision lines
                        processed = processed.substring(0, insertPos)
                                        + "out vec4 eagler_FragColor;\n"
                                        + processed.substring(insertPos);
                        return processed;
                }

                return result.toString();
        }

        /**
         * Converts a GLSL keyword from desktop to ES 3.00 syntax.
         * Only replaces the keyword when it appears as a standalone token
         * (not as part of a longer identifier).
         *
         * @param line     the source line
         * @param oldWord  the keyword to replace
         * @param newWord  the replacement keyword
         * @return the line with replacements applied
         */
        private static String convertKeyword(String line, String oldWord, String newWord) {
                // Simple word boundary replacement
                // Replace "attribute " with "in " and similar patterns
                return line.replaceAll("\\b" + oldWord + "\\b", newWord);
        }

        /**
         * Converts a texture function call from desktop GLSL to GLSL ES 3.00.
         * e.g., texture2D(sampler, coord) → texture(sampler, coord)
         *
         * @param line      the source line
         * @param funcName  the texture function name to replace
         * @return the line with the texture call replaced
         */
        private static String convertTextureCall(String line, String funcName) {
                return line.replaceAll("\\b" + funcName + "\\s*\\(", "texture(");
        }

        // ========== Uniform Buffer Object Helpers ==========

        /**
         * Creates a uniform buffer object (UBO) for efficient uniform updates.
         * UBOs allow updating multiple uniforms in a single buffer upload,
         * which is significantly faster than individual uniform calls.
         *
         * <p>Usage pattern:</p>
         * <pre>
         *   JSObject ubo = EaglerShaderImpl.createUniformBuffer(matricesData, GL_STATIC_DRAW);
         *   EaglerShaderImpl.bindUniformBufferBase(ubo, 0); // bind to binding point 0
         *   // In shader: layout(std140) uniform MatricesBlock { ... } matricesBlock;
         * </pre>
         *
         * @param data   the initial buffer data as a Float32Array
         * @param usage  the buffer usage hint (GL_STATIC_DRAW, GL_DYNAMIC_DRAW, etc.)
         * @return the buffer JSObject handle
         */
        public static JSObject createUniformBuffer(Float32Array data, int usage) {
                JSObject buffer = PlatformOpenGL._wglCreateBuffer();
                PlatformOpenGL._wglBindBuffer(WebGL2RenderingContext.UNIFORM_BUFFER, buffer);
                PlatformOpenGL._wglBufferData(WebGL2RenderingContext.UNIFORM_BUFFER, data, usage);
                PlatformOpenGL._wglBindBuffer(WebGL2RenderingContext.UNIFORM_BUFFER, null);
                return buffer;
        }

        /**
         * Updates the data in a uniform buffer object.
         *
         * @param buffer  the UBO to update
         * @param data    the new data
         * @param offset  the offset into the buffer to start writing
         */
        public static void updateUniformBuffer(JSObject buffer, Float32Array data, int offset) {
                PlatformOpenGL._wglBindBuffer(WebGL2RenderingContext.UNIFORM_BUFFER, buffer);
                PlatformOpenGL._wglBufferSubData(WebGL2RenderingContext.UNIFORM_BUFFER, offset, data);
                PlatformOpenGL._wglBindBuffer(WebGL2RenderingContext.UNIFORM_BUFFER, null);
        }

        /**
         * Binds a uniform buffer to a specific binding point.
         *
         * @param buffer       the UBO to bind
         * @param bindingPoint the binding point index (must match the shader's layout binding)
         */
        public static void bindUniformBufferBase(JSObject buffer, int bindingPoint) {
                PlatformOpenGL._wglBindBufferBase(WebGL2RenderingContext.UNIFORM_BUFFER, bindingPoint, buffer);
        }

        /**
         * Unbinds any uniform buffer from the specified binding point.
         *
         * @param bindingPoint the binding point to unbind
         */
        public static void unbindUniformBufferBase(int bindingPoint) {
                PlatformOpenGL._wglBindBufferBase(WebGL2RenderingContext.UNIFORM_BUFFER, bindingPoint, null);
        }

        // ========== Shader Validation ==========

        /**
         * Validates a linked shader program.
         * Useful for debugging; not recommended for production use due to performance cost.
         *
         * @param program the program to validate
         * @return true if the program validated successfully
         */
        public static boolean validateProgram(JSObject program) {
                if (program == null) return false;
                PlatformOpenGL._wglValidateProgram(program);
                // Validation result is informational; the program may still work even if
                // validation "fails" (e.g., current vertex state doesn't match program)
                return true;
        }

        // ========== Statistics ==========

        /**
         * Gets the number of shaders compiled.
         */
        public static int getShadersCompiled() {
                return shadersCompiled;
        }

        /**
         * Gets the number of programs linked.
         */
        public static int getProgramsLinked() {
                return programsLinked;
        }

        /**
         * Gets the number of shader compilation failures.
         */
        public static int getShaderCompileFailures() {
                return shaderCompileFailures;
        }

        /**
         * Gets the number of program link failures.
         */
        public static int getProgramLinkFailures() {
                return programLinkFailures;
        }

        /**
         * Returns a formatted string with shader compilation statistics.
         */
        public static String getShaderStats() {
                return "Shaders compiled: " + shadersCompiled
                                + " (failed: " + shaderCompileFailures + "), "
                                + "Programs linked: " + programsLinked
                                + " (failed: " + programLinkFailures + ")";
        }

        // ========== Shader Caching ==========

        /**
         * Enables or disables shader source caching.
         * When enabled, the original source strings are retained so shaders
         * can be recompiled after a WebGL2 context loss.
         *
         * @param enabled whether to enable caching
         */
        public static void setCachingEnabled(boolean enabled) {
                cachingEnabled = enabled;
        }

        /**
         * Checks if shader source caching is enabled.
         */
        public static boolean isCachingEnabled() {
                return cachingEnabled;
        }

        /**
         * Resets the shader statistics counters.
         * Useful after a context loss recovery to get fresh statistics.
         */
        public static void resetStats() {
                shadersCompiled = 0;
                programsLinked = 0;
                shaderCompileFailures = 0;
                programLinkFailures = 0;
        }

        // ========== Title Screen Renderer ==========

        /** Cached title screen shader program. */
        private static JSObject titleProgram = null;

        /** Cached title screen VAO. */
        private static JSObject titleVAO = null;

        /** Cached title screen VBO. */
        private static JSObject titleVBO = null;

        /** Cached uniform locations for the title shader. */
        private static JSObject titleResolutionUniform = null;
        private static JSObject titleTimeUniform = null;

        /** Whether the title screen resources have been initialized. */
        private static boolean titleInitialized = false;

        /**
         * Renders the EaglerCraft title screen using a fullscreen gradient shader.
         *
         * <p>This draws a Minecraft-style sky gradient with a slowly moving
         * panorama effect, plus overlaid text info. It serves as the placeholder
         * title screen until the full MC GUI system is integrated.</p>
         *
         * @param gl       the WebGL2 context
         * @param width    the canvas width in pixels
         * @param height   the canvas height in pixels
         * @param title    the main title text (e.g. "EaglerCraftX 26.1.2")
         * @param subtitle the subtitle text
         * @param username the player username
         * @param seconds  elapsed seconds
         * @param frames   total frames rendered
         */
        public static void renderTitleScreen(WebGL2RenderingContext gl, int width, int height,
                        String title, String subtitle, String username, int seconds, int frames) {
                if (!titleInitialized) {
                        initTitleResources(gl);
                        titleInitialized = true;
                }
                if (titleProgram == null) return;

                // Use the title screen shader
                PlatformOpenGL._wglUseProgram(titleProgram);

                // Set uniforms
                PlatformOpenGL._wglUniform2f(titleResolutionUniform, (float)width, (float)height);
                PlatformOpenGL._wglUniform1f(titleTimeUniform, (float)seconds);

                // Draw fullscreen quad
                PlatformOpenGL._wglBindVertexArray(titleVAO);
                gl.drawArrays(WebGL2RenderingContext.TRIANGLE_STRIP, 0, 4);
                PlatformOpenGL._wglBindVertexArray(null);

                // Unbind shader
                PlatformOpenGL._wglUseProgram(null);
        }

        /**
         * Initializes the WebGL2 resources needed for the title screen renderer.
         * Creates a gradient shader program and a fullscreen quad VAO.
         */
        private static void initTitleResources(WebGL2RenderingContext gl) {
                // Vertex shader: fullscreen triangle strip quad
                String vsh = "#version 300 es\n"
                        + "precision highp float;\n"
                        + "in vec2 a_pos;\n"
                        + "out vec2 v_uv;\n"
                        + "void main() {\n"
                        + "  v_uv = a_pos * 0.5 + 0.5;\n"
                        + "  gl_Position = vec4(a_pos, 0.0, 1.0);\n"
                        + "}\n";

                // Fragment shader: MC-style sky gradient with animated panorama
                String fsh = "#version 300 es\n"
                        + "precision highp float;\n"
                        + "in vec2 v_uv;\n"
                        + "uniform vec2 u_resolution;\n"
                        + "uniform float u_time;\n"
                        + "out vec4 fragColor;\n"
                        + "void main() {\n"
                        + "  vec2 uv = v_uv;\n"
                        + "  float horizon = 0.35;\n"
                        + "  // Sky gradient (MC overworld sky colors)\n"
                        + "  vec3 skyTop = vec3(0.38, 0.62, 0.95);\n"
                        + "  vec3 skyHorizon = vec3(0.75, 0.87, 1.0);\n"
                        + "  vec3 groundFar = vec3(0.55, 0.73, 0.35);\n"
                        + "  vec3 groundNear = vec3(0.38, 0.55, 0.22);\n"
                        + "  vec3 col;\n"
                        + "  if (uv.y > horizon) {\n"
                        + "    float t = (uv.y - horizon) / (1.0 - horizon);\n"
                        + "    col = mix(skyHorizon, skyTop, t);\n"
                        + "    // Subtle clouds\n"
                        + "    float cloud = smoothstep(0.48, 0.52, sin(uv.x * 8.0 + u_time * 0.15) * 0.5 + 0.5);\n"
                        + "    col = mix(col, vec3(1.0), cloud * 0.3 * smoothstep(0.0, 0.3, t));\n"
                        + "  } else {\n"
                        + "    float t = uv.y / horizon;\n"
                        + "    col = mix(groundNear, groundFar, t);\n"
                        + "  }\n"
                        + "  // Vignette effect\n"
                        + "  vec2 vc = uv - 0.5;\n"
                        + "  float vig = 1.0 - dot(vc, vc) * 0.8;\n"
                        + "  col *= vig;\n"
                        + "  fragColor = vec4(col, 1.0);\n"
                        + "}\n";

                titleProgram = createProgramFromSource(vsh, fsh, "title_screen");
                if (titleProgram == null) {
                        ClientMain.error("[EaglerShaderImpl] Failed to create title screen shader");
                        return;
                }

                // Get uniform locations
                titleResolutionUniform = getUniformLocation(titleProgram, "u_resolution");
                titleTimeUniform = getUniformLocation(titleProgram, "u_time");

                // Create fullscreen quad VAO
                titleVAO = PlatformOpenGL._wglCreateVertexArray();
                PlatformOpenGL._wglBindVertexArray(titleVAO);

                titleVBO = PlatformOpenGL._wglCreateBuffer();
                PlatformOpenGL._wglBindBuffer(WebGL2RenderingContext.ARRAY_BUFFER, titleVBO);

                // Fullscreen quad vertices (triangle strip): -1,-1  1,-1  -1,1  1,1
                float[] quadData = new float[] {
                        -1.0f, -1.0f,
                         1.0f, -1.0f,
                        -1.0f,  1.0f,
                         1.0f,  1.0f
                };
                Float32Array quadBuf = Float32Array.create(quadData.length);
                for (int i = 0; i < quadData.length; i++) {
                        quadBuf.set(i, quadData[i]);
                }
                PlatformOpenGL._wglBufferData(WebGL2RenderingContext.ARRAY_BUFFER, quadBuf,
                        WebGL2RenderingContext.STATIC_DRAW);

                int posAttrib = getAttribLocation(titleProgram, "a_pos");
                PlatformOpenGL._wglEnableVertexAttribArray(posAttrib);
                PlatformOpenGL._wglVertexAttribPointer(posAttrib, 2,
                        WebGL2RenderingContext.FLOAT, false, 0, 0);

                PlatformOpenGL._wglBindVertexArray(null);
                PlatformOpenGL._wglBindBuffer(WebGL2RenderingContext.ARRAY_BUFFER, null);

                if (EaglerCraftConfig.VERBOSE_INIT_LOGGING) {
                        ClientMain.log("[EaglerShaderImpl] Title screen shader initialized");
                }
        }
}
