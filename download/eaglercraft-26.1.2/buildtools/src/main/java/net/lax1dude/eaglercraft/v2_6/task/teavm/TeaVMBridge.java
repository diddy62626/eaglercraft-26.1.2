package net.lax1dude.eaglercraft.v2_6.task.teavm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.lax1dude.eaglercraft.v2_6.EaglerBuildTools.BuildException;

/**
 * TeaVM Compilation Bridge for EaglerCraft 26.1.2
 * 
 * Bridges the EaglerCraft build system with TeaVM 0.10.0, which compiles
 * Java bytecode to JavaScript for running in web browsers.
 * 
 * TeaVM is the core technology that makes EaglerCraft possible - it translates
 * the Minecraft Java code into JavaScript that runs in a browser with WebGL2.
 * 
 * Key features of this bridge:
 *   - Configures TeaVM for Minecraft's specific requirements
 *   - Handles incremental compilation for faster builds
 *   - Generates source maps for debugging
 *   - Supports both debug and release builds
 *   - Manages TeaVM's classpath including MC dependencies
 *   - Handles WebGL2/WASM-adjacent compilation targets
 * 
 * TeaVM 0.10.0 changes from older versions:
 *   - Improved JSO (JavaScript Overlay) API
 *   - Better async/await support
 *   - Faster compilation with incremental mode
 *   - Improved obfuscation
 *   - Better source map generation
 * 
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
 */
public class TeaVMBridge {

    private static final String TEAVM_VERSION = "0.10.0";
    private static final String TEAVM_MAIN_CLASS = "org.teavm.cli.TeaVMRunner";
    private static final String EAGLERCRAFT_ENTRY_POINT = "net.lax1dude.eaglercraft.v2_6.internal.teavm.MainClass";

    // Compilation modes
    public enum CompileMode {
        DEBUG,       // Full debug info, source maps, no obfuscation
        DEVELOPMENT, // Some optimizations, source maps
        RELEASE      // Full optimization, obfuscation, no source maps
    }

    // Configuration
    private CompileMode mode = CompileMode.DEVELOPMENT;
    private boolean incremental = true;
    private boolean sourceMaps = true;
    private boolean obfuscate = false;
    private boolean minify = false;
    private File outputDir;
    private File workspaceDir;
    private File cacheDir;
    private int maxHeapSize = 2048; // MB

    public static void execute(String[] args, File baseDir) throws BuildException {
        TeaVMBridge bridge = new TeaVMBridge();
        bridge.parseArgs(args);
        bridge.compile(baseDir);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--release":
                    mode = CompileMode.RELEASE;
                    obfuscate = true;
                    minify = true;
                    sourceMaps = false;
                    break;
                case "--debug":
                    mode = CompileMode.DEBUG;
                    obfuscate = false;
                    minify = false;
                    sourceMaps = true;
                    break;
                case "--development":
                    mode = CompileMode.DEVELOPMENT;
                    break;
                case "--no-obfuscate":
                    obfuscate = false;
                    break;
                case "--no-source-maps":
                    sourceMaps = false;
                    break;
                case "--no-incremental":
                    incremental = false;
                    break;
                case "--minify":
                    minify = true;
                    break;
                case "--heap":
                    if (i + 1 < args.length) {
                        try {
                            maxHeapSize = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.out.println("[TeaVM] Invalid heap size, using default: " + maxHeapSize);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Compile the EaglerCraft client using TeaVM
     */
    public void compile(File baseDir) throws BuildException {
        System.out.println("============================================");
        System.out.println("  TeaVM Compilation Bridge v" + TEAVM_VERSION);
        System.out.println("  Mode: " + mode);
        System.out.println("============================================");
        System.out.println();

        // Setup directories
        workspaceDir = new File(baseDir, "workspace");
        outputDir = new File(workspaceDir, "javascript");
        cacheDir = new File(workspaceDir, ".teavm");

        File srcMainDir = new File(workspaceDir, "src/main/java");
        File srcTeavmDir = new File(workspaceDir, "src/teavm/java");
        File resourcesDir = new File(workspaceDir, "resources");
        File binDir = new File(workspaceDir, "bin");

        // Verify workspace
        if (!workspaceDir.exists()) {
            throw new BuildException("Workspace not found! Run 'workspace' first.");
        }

        if (!srcMainDir.exists() && !srcTeavmDir.exists()) {
            throw new BuildException("No source files found in workspace!");
        }

        // Step 1: Compile Java source to bytecode
        System.out.println("[TeaVM] Step 1: Compiling Java source to bytecode...");
        compileJavaSource(srcMainDir, srcTeavmDir, binDir);
        System.out.println();

        // Step 2: Ensure output directory exists
        System.out.println("[TeaVM] Step 2: Preparing output directory...");
        ensureOutputDirectory();
        System.out.println();

        // Step 3: Run TeaVM compilation
        System.out.println("[TeaVM] Step 3: Running TeaVM compilation...");
        long startTime = System.currentTimeMillis();
        runTeaVMCompilation(binDir);
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("[TeaVM] TeaVM compilation completed in " + formatDuration(elapsed));
        System.out.println();

        // Step 4: Post-process output
        System.out.println("[TeaVM] Step 4: Post-processing output...");
        postProcessOutput();
        System.out.println();

        // Step 5: Package assets
        System.out.println("[TeaVM] Step 5: Packaging assets...");
        packageAssets(resourcesDir, outputDir);
        System.out.println();

        // Print summary
        printCompilationSummary();
    }

    /**
     * Compile Java source files to bytecode using javac
     */
    private void compileJavaSource(File srcMainDir, File srcTeavmDir, File binDir) throws BuildException {
        if (!binDir.exists() && !binDir.mkdirs()) {
            throw new BuildException("Failed to create output directory: " + binDir.getAbsolutePath());
        }

        // Collect all Java source files
        List<String> sourceFiles = new ArrayList<>();
        collectJavaFiles(srcMainDir, sourceFiles);
        collectJavaFiles(srcTeavmDir, sourceFiles);

        if (sourceFiles.isEmpty()) {
            throw new BuildException("No Java source files found!");
        }

        System.out.println("[TeaVM] Found " + sourceFiles.size() + " source files");

        // Build javac command
        List<String> command = new ArrayList<>();
        String javaHome = System.getProperty("java.home");
        String javacExec = javaHome != null
            ? new File(javaHome, "bin/javac").getAbsolutePath()
            : "javac";

        command.add(javacExec);
        command.add("-source");
        command.add("25");
        command.add("-target");
        command.add("25");
        command.add("-d");
        command.add(binDir.getAbsolutePath());
        command.add("-encoding");
        command.add("UTF-8");
        command.add("-Xlint:-options");

        // Add classpath (TeaVM libraries)
        String classpath = buildClasspath();
        if (!classpath.isEmpty()) {
            command.add("-cp");
            command.add(classpath);
        }

        command.addAll(sourceFiles);

        // Execute javac
        System.out.println("[TeaVM] Compiling with javac...");
        executeCommand(command, "javac");

        System.out.println("[TeaVM] Java compilation successful.");
    }

    /**
     * Collect all .java files in a directory recursively
     */
    private void collectJavaFiles(File dir, List<String> files) {
        if (!dir.exists() || !dir.isDirectory()) return;

        File[] children = dir.listFiles();
        if (children == null) return;

        for (File child : children) {
            if (child.isDirectory()) {
                collectJavaFiles(child, files);
            } else if (child.getName().endsWith(".java")) {
                files.add(child.getAbsolutePath());
            }
        }
    }

    /**
     * Build the classpath string for compilation
     */
    private String buildClasspath() {
        List<String> classpathEntries = new ArrayList<>();

        // Add TeaVM JARs from lib directory
        File libDir = new File(workspaceDir, "lib");
        if (libDir.exists()) {
            addJarsFromDir(new File(libDir, "teavm"), classpathEntries);
            addJarsFromDir(libDir, classpathEntries);
        }

        // Add Gradle cache
        File gradleCache = new File(System.getProperty("user.home"),
            ".gradle/caches/modules-2/files-2.1/org.teavm");
        if (gradleCache.exists()) {
            addJarsFromDirRecursive(gradleCache, classpathEntries);
        }

        return String.join(File.pathSeparator, classpathEntries);
    }

    private void addJarsFromDir(File dir, List<String> entries) {
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] jars = dir.listFiles((d, name) -> name.endsWith(".jar"));
        if (jars != null) {
            for (File jar : jars) {
                entries.add(jar.getAbsolutePath());
            }
        }
    }

    private void addJarsFromDirRecursive(File dir, List<String> entries) {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                addJarsFromDirRecursive(f, entries);
            } else if (f.getName().endsWith(".jar")) {
                entries.add(f.getAbsolutePath());
            }
        }
    }

    /**
     * Run TeaVM compilation to convert bytecode to JavaScript
     */
    private void runTeaVMCompilation(File binDir) throws BuildException {
        List<String> command = new ArrayList<>();

        // Java executable
        String javaHome = System.getProperty("java.home");
        String javaExec = javaHome != null
            ? new File(javaHome, "bin/java").getAbsolutePath()
            : "java";

        command.add(javaExec);

        // JVM arguments
        command.add("-Xmx" + maxHeapSize + "M");
        command.add("-XX:+UseG1GC");
        command.add("-XX:MaxMetaspaceSize=512M");
        command.add("-Dteavm.cache.dir=" + cacheDir.getAbsolutePath());

        // Classpath
        String classpath = binDir.getAbsolutePath();
        String teavmClasspath = buildClasspath();
        if (!teavmClasspath.isEmpty()) {
            classpath += File.pathSeparator + teavmClasspath;
        }
        command.add("-cp");
        command.add(classpath);

        // TeaVM main class
        command.add(TEAVM_MAIN_CLASS);

        // TeaVM options
        command.add("--target");
        command.add("js");  // JavaScript target

        command.add("--main-class");
        command.add(EAGLERCRAFT_ENTRY_POINT);

        command.add("--output-dir");
        command.add(outputDir.getAbsolutePath());

        // Mode-specific options
        switch (mode) {
            case DEBUG:
                command.add("--debug-information");
                if (sourceMaps) {
                    command.add("--source-map");
                    command.add("enabled");
                }
                break;
            case DEVELOPMENT:
                command.add("--optimization-level");
                command.add("SIMPLE");
                if (sourceMaps) {
                    command.add("--source-map");
                    command.add("enabled");
                }
                break;
            case RELEASE:
                command.add("--optimization-level");
                command.add("FULL");
                command.add("--obfuscation-level");
                command.add("FULL");
                if (minify) {
                    command.add("--minification");
                    command.add("enabled");
                }
                break;
        }

        // Incremental compilation
        if (incremental && cacheDir.exists()) {
            command.add("--incremental");
        }

        // Execute TeaVM
        System.out.println("[TeaVM] Running TeaVM compiler...");
        System.out.println("[TeaVM] Entry point: " + EAGLERCRAFT_ENTRY_POINT);
        System.out.println("[TeaVM] Output: " + outputDir.getAbsolutePath());

        executeCommand(command, "TeaVM");
    }

    /**
     * Post-process the TeaVM output
     */
    private void postProcessOutput() throws BuildException {
        File runtimeJs = new File(outputDir, "runtime.js");
        File classesJs = new File(outputDir, "classes.js");

        // Check for expected output files
        if (!classesJs.exists() && !runtimeJs.exists()) {
            // TeaVM might output a different file structure
            File[] jsFiles = outputDir.listFiles((dir, name) -> name.endsWith(".js"));
            if (jsFiles == null || jsFiles.length == 0) {
                throw new BuildException("TeaVM did not produce any JavaScript output!");
            }

            System.out.println("[TeaVM] Output files:");
            for (File js : jsFiles) {
                System.out.println("[TeaVM]   " + js.getName() + " (" +
                    String.format("%.1f KB", js.length() / 1024.0) + ")");
            }
        } else {
            System.out.println("[TeaVM] runtime.js: " +
                (runtimeJs.exists() ? String.format("%.1f KB", runtimeJs.length() / 1024.0) : "not found"));
            System.out.println("[TeaVM] classes.js: " +
                (classesJs.exists() ? String.format("%.1f KB", classesJs.length() / 1024.0) : "not found"));
        }

        // Process source maps
        if (sourceMaps) {
            File sourceMap = new File(outputDir, "classes.js.map");
            if (sourceMap.exists()) {
                System.out.println("[TeaVM] Source map: " +
                    String.format("%.1f KB", sourceMap.length() / 1024.0));
            }
        }
    }

    /**
     * Package assets into the output directory
     */
    private void packageAssets(File resourcesDir, File outputDir) throws BuildException {
        File epkDir = new File(resourcesDir, "epk");
        if (!epkDir.exists()) {
            System.out.println("[TeaVM] No EPK assets directory found, skipping asset packaging.");
            return;
        }

        // Count asset files
        int[] count = {0};
        countFilesRecursive(epkDir, count);

        if (count[0] == 0) {
            System.out.println("[TeaVM] No asset files to package.");
            return;
        }

        System.out.println("[TeaVM] Packaging " + count[0] + " asset files...");

        // Copy assets to output
        try {
            copyRecursive(epkDir, outputDir);
        } catch (IOException e) {
            throw new BuildException("Failed to package assets: " + e.getMessage(), e);
        }
    }

    /**
     * Print compilation summary
     */
    private void printSummary() {
        System.out.println("[TeaVM] ═══════════════════════════════════════");
        System.out.println("[TeaVM] Compilation Summary:");
        System.out.println("[TeaVM]   Mode:       " + mode);
        System.out.println("[TeaVM]   Obfuscate:  " + obfuscate);
        System.out.println("[TeaVM]   Minify:     " + minify);
        System.out.println("[TeaVM]   Source Maps: " + sourceMaps);
        System.out.println("[TeaVM]   Output:     " + outputDir.getAbsolutePath());
        System.out.println("[TeaVM] ═══════════════════════════════════════");
    }

    // Helper methods

    private void ensureOutputDirectory() throws BuildException {
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new BuildException("Failed to create output directory: " + outputDir.getAbsolutePath());
        }
    }

    private void executeCommand(List<String> command, String processName) throws BuildException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new BuildException("Failed to start " + processName + ": " + e.getMessage(), e);
        }

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                // Print important output lines
                if (line.contains("ERROR") || line.contains("error") || line.contains("Warning")) {
                    System.out.println("[" + processName + "] " + line);
                }
            }
        } catch (IOException e) {
            throw new BuildException("Error reading " + processName + " output: " + e.getMessage(), e);
        }

        int exitCode;
        try {
            if (!process.waitFor(30, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                throw new BuildException(processName + " timed out after 30 minutes");
            }
            exitCode = process.exitValue();
        } catch (InterruptedException e) {
            process.destroyForcibly();
            Thread.currentThread().interrupt();
            throw new BuildException(processName + " was interrupted", e);
        }

        if (exitCode != 0) {
            String[] lines = output.toString().split("\n");
            int start = Math.max(0, lines.length - 30);
            System.err.println("[" + processName + "] Exited with code " + exitCode);
            for (int i = start; i < lines.length; i++) {
                System.err.println("  " + lines[i]);
            }
            throw new BuildException(processName + " failed with exit code " + exitCode);
        }
    }

    private void printCompilationSummary() {
        // Calculate total output size
        long totalSize = 0;
        File[] files = outputDir.listFiles();
        if (files != null) {
            for (File f : files) {
                totalSize += f.length();
            }
        }

        System.out.println();
        System.out.println("============================================");
        System.out.println("  Compilation Complete!");
        System.out.println("  Mode:   " + mode);
        System.out.println("  Output: " + outputDir.getAbsolutePath());
        System.out.println("  Size:   " + String.format("%.1f MB", totalSize / 1024.0 / 1024.0));
        System.out.println("  Run 'dist' to create distribution packages.");
        System.out.println("============================================");
    }

    private void countFilesRecursive(File dir, int[] count) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                countFilesRecursive(f, count);
            } else {
                count[0]++;
            }
        }
    }

    private void copyRecursive(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists() && !dest.mkdirs()) {
                throw new IOException("Failed to create directory: " + dest);
            }
            File[] children = src.listFiles();
            if (children != null) {
                for (File child : children) {
                    copyRecursive(child, new File(dest, child.getName()));
                }
            }
        } else {
            Files.copy(src.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private String formatDuration(long millis) {
        if (millis < 1000) return millis + "ms";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        }
        return seconds + "s";
    }
}
