package net.lax1dude.eaglercraft.v2_6.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.lax1dude.eaglercraft.v2_6.EaglerBuildTools.BuildException;

/**
 * Decompiler for Minecraft 26.1.2
 * 
 * Comparison with MC 1.8 decompilation:
 * 
 * MC 1.8 (OLD - Complex Pipeline):
 *   1. SpecialSource: Deobfuscate using SRG mappings (notch -> searge names)
 *   2. MCInjector: Restore generics, annotations, inner classes via EXC mappings
 *   3. Fernflower: Decompile with CSV parameter/field mappings
 *   4. Post-process: Fix decompiler bugs, rename parameters
 *   5. ECR patches: Eagler patches WITHOUT context (obfuscated = can't include context)
 * 
 * MC 26.1.2 (NEW - Simplified Pipeline):
 *   1. Extract: Just unzip .class files (already unobfuscated!)
 *   2. Vineflower: Decompile (modern Fernflower fork, Java 25 compatible)
 *   3. Format: Apply consistent code formatting
 *   4. Patches: Standard unified diff WITH context (safe since code is unobfuscated)
 * 
 * Key differences:
 *   - NO SpecialSource needed (code is already deobfuscated)
 *   - NO MCInjector needed (generics/annotations preserved in unobfuscated JAR)
 *   - NO SRG/EXC/CSV mapping files needed at all
 *   - Vineflower replaces Fernflower (active fork, Java 25 support)
 *   - Patches can include context lines (unobfuscated = readable)
 * 
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
 */
public class DecompileMinecraft {

    private static final String VINEFLOWER_MAIN = "org.vineflower.VineflowerMain";
    private static final String VINEFLOWER_JAR_NAME = "vineflower-1.10.1.jar";

    // Decompilation options tuned for MC source
    private static final String[] DEFAULT_OPTIONS = {
        "--indent", "4",                     // 4-space indentation (MC style)
        "--remove-bridge", "true",           // Remove bridge methods
        "--remove-synthetic", "false",       // Keep synthetic members for accuracy
        "--decompile-generics", "true",      // Decompile generics (MC 26.1.2 uses them)
        "--decompile-inner", "true",         // Decompile inner classes
        "--decompile-java4", "false",        // Not Java 4
        "--decompile-java9", "true",         // Java 9+ modules support
        "--decompile-generics-signatures", "true", // Use generic signatures
        "--ascii-strings", "true",           // ASCII string output
        "--verify-annotations", "true",      // Verify annotation consistency
        "--lift-constructor-init", "true",   // Lift constructor init
        "--constructor-auto-synthetic", "true", // Auto-synthetic constructors
        "--simplify-member-refs", "true",    // Simplify member references
        "--inline-simple-lambdas", "true",   // Inline simple lambdas
        "--overwrite", "true"                // Overwrite existing output
    };

    private File vineflowerJar;
    private boolean vineflowerFound = false;

    /**
     * Decompile Minecraft classes using Vineflower
     * 
     * @param classesDir  Directory containing extracted .class files
     * @param outputDir   Directory to write decompiled .java files
     * @param extraArgs   Additional Vineflower arguments from user
     */
    public void decompile(File classesDir, File outputDir, List<String> extraArgs) throws BuildException {
        System.out.println("[Decompile] Starting decompilation of MC 26.1.2...");
        System.out.println("[Decompile] Input:  " + classesDir.getAbsolutePath());
        System.out.println("[Decompile] Output: " + outputDir.getAbsolutePath());

        // Validate inputs
        if (!classesDir.exists() || !classesDir.isDirectory()) {
            throw new BuildException("Classes directory not found: " + classesDir.getAbsolutePath());
        }

        // Count class files
        int classCount = countClassFiles(classesDir);
        System.out.println("[Decompile] Found " + classCount + " class files to decompile");

        if (classCount == 0) {
            throw new BuildException("No .class files found in: " + classesDir.getAbsolutePath());
        }

        // Locate Vineflower
        locateVineflower(classesDir);
        if (!vineflowerFound) {
            System.out.println("[Decompile] Vineflower JAR not found, attempting to download...");
            downloadVineflower(classesDir);
        }

        if (!vineflowerFound) {
            throw new BuildException(
                "Vineflower not found! Please place " + VINEFLOWER_JAR_NAME +
                " in the buildtools/lib/ directory\n" +
                "Download from: https://github.com/Vineflower/vineflower/releases"
            );
        }

        // Build decompilation command
        List<String> command = buildDecompileCommand(classesDir, outputDir, extraArgs);
        System.out.println("[Decompile] Running Vineflower...");

        // Execute decompilation
        long startTime = System.currentTimeMillis();
        executeVineflower(command);
        long elapsed = System.currentTimeMillis() - startTime;

        // Verify output
        int javaCount = countJavaFiles(outputDir);
        System.out.println("[Decompile] Decompiled " + javaCount + " Java files in " +
            formatDuration(elapsed));

        if (javaCount == 0) {
            throw new BuildException("Decompilation produced no output files!");
        }

        // Post-processing
        System.out.println("[Decompile] Running post-processing...");
        postProcessDecompiled(outputDir);

        System.out.println("[Decompile] Decompilation complete!");
    }

    /**
     * Locate the Vineflower JAR
     */
    private void locateVineflower(File baseDir) {
        // Check common locations
        File[] searchPaths = {
            new File("buildtools/lib/" + VINEFLOWER_JAR_NAME),
            new File("lib/" + VINEFLOWER_JAR_NAME),
            new File(VINEFLOWER_JAR_NAME),
            new File(System.getProperty("user.home"), ".eaglercraft/" + VINEFLOWER_JAR_NAME),
            new File(System.getProperty("user.home"), ".vineflower/" + VINEFLOWER_JAR_NAME)
        };

        for (File path : searchPaths) {
            if (path.exists() && path.isFile()) {
                vineflowerJar = path;
                vineflowerFound = true;
                System.out.println("[Decompile] Found Vineflower: " + path.getAbsolutePath());
                return;
            }
        }

        // Check VINEFLOWER_HOME environment variable
        String envPath = System.getenv("VINEFLOWER_HOME");
        if (envPath != null && !envPath.isEmpty()) {
            File envFile = new File(envPath);
            if (envFile.exists()) {
                vineflowerJar = envFile;
                vineflowerFound = true;
                System.out.println("[Decompile] Found Vineflower (from env): " + envFile.getAbsolutePath());
            }
        }
    }

    /**
     * Attempt to download Vineflower
     */
    private void downloadVineflower(File baseDir) {
        String downloadUrl = "https://github.com/Vineflower/vineflower/releases/latest/download/" +
            VINEFLOWER_JAR_NAME;
        File targetDir = new File("buildtools/lib");
        File targetFile = new File(targetDir, VINEFLOWER_JAR_NAME);

        try {
            if (!targetDir.exists() && !targetDir.mkdirs()) {
                System.out.println("[Decompile] Could not create lib directory");
                return;
            }

            System.out.println("[Decompile] Downloading " + VINEFLOWER_JAR_NAME + "...");
            ProcessBuilder pb = new ProcessBuilder(
                "curl", "-L", "-o", targetFile.getAbsolutePath(), downloadUrl
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Suppress curl progress output
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0 && targetFile.exists() && targetFile.length() > 1000) {
                vineflowerJar = targetFile;
                vineflowerFound = true;
                System.out.println("[Decompile] Downloaded Vineflower: " + targetFile.getAbsolutePath());
            } else {
                System.out.println("[Decompile] Download failed (exit code: " + exitCode + ")");
                if (targetFile.exists()) {
                    targetFile.delete();
                }
            }
        } catch (Exception e) {
            System.out.println("[Decompile] Download failed: " + e.getMessage());
        }
    }

    /**
     * Build the Vineflower command line
     */
    private List<String> buildDecompileCommand(File inputDir, File outputDir, List<String> extraArgs) {
        List<String> command = new ArrayList<>();

        // Java executable
        String javaHome = System.getProperty("java.home");
        String javaExec = javaHome != null
            ? new File(javaHome, "bin/java").getAbsolutePath()
            : "java";
        command.add(javaExec);

        // JVM arguments for large decompilation
        command.add("-Xmx2G");
        command.add("-XX:+UseG1GC");
        command.add("-XX:MaxMetaspaceSize=512M");

        // Vineflower main class or JAR
        command.add("-jar");
        command.add(vineflowerJar.getAbsolutePath());

        // Default options
        for (String opt : DEFAULT_OPTIONS) {
            command.add(opt);
        }

        // Extra user arguments
        if (extraArgs != null) {
            for (String arg : extraArgs) {
                // Parse space-separated arguments
                String[] parts = arg.split("\\s+");
                for (String part : parts) {
                    if (!part.trim().isEmpty()) {
                        command.add(part.trim());
                    }
                }
            }
        }

        // Input and output directories
        command.add(inputDir.getAbsolutePath());
        command.add(outputDir.getAbsolutePath());

        return command;
    }

    /**
     * Execute Vineflower decompilation process
     */
    private void executeVineflower(List<String> command) throws BuildException {
        System.out.println("[Decompile] Command: " + String.join(" ", command));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new BuildException("Failed to start Vineflower: " + e.getMessage(), e);
        }

        // Capture output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                lineCount++;
                // Print progress every 100 lines
                if (lineCount % 100 == 0) {
                    System.out.println("[Decompile] Progress: " + lineCount + " classes processed...");
                }
            }
        } catch (IOException e) {
            throw new BuildException("Error reading Vineflower output: " + e.getMessage(), e);
        }

        // Wait for process to complete
        int exitCode;
        try {
            if (!process.waitFor(30, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                throw new BuildException("Vineflower timed out after 30 minutes");
            }
            exitCode = process.exitValue();
        } catch (InterruptedException e) {
            process.destroyForcibly();
            Thread.currentThread().interrupt();
            throw new BuildException("Vineflower was interrupted", e);
        }

        if (exitCode != 0) {
            // Print last 50 lines of output for debugging
            String[] lines = output.toString().split("\n");
            int start = Math.max(0, lines.length - 50);
            System.err.println("[Decompile] Vineflower exited with code " + exitCode);
            System.err.println("[Decompile] Last output lines:");
            for (int i = start; i < lines.length; i++) {
                System.err.println("  " + lines[i]);
            }
            throw new BuildException("Vineflower decompilation failed with exit code " + exitCode);
        }

        System.out.println("[Decompile] Vineflower completed successfully.");
    }

    /**
     * Post-process decompiled source files
     */
    private void postProcessDecompiled(File sourceDir) throws BuildException {
        System.out.println("[Decompile] Post-processing decompiled source...");
        int[] stats = {0, 0, 0}; // [cleaned, fixed, renamed]

        postProcessRecursive(sourceDir, stats);

        System.out.println("[Decompile] Post-processing complete:");
        System.out.println("[Decompile]   Cleaned " + stats[0] + " files");
        System.out.println("[Decompile]   Fixed " + stats[1] + " decompiler artifacts");
        System.out.println("[Decompile]   Renamed " + stats[2] + " files");
    }

    private void postProcessRecursive(File dir, int[] stats) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                postProcessRecursive(file, stats);
                continue;
            }

            if (!file.getName().endsWith(".java")) continue;

            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                String original = content;

                // Fix common Vineflower decompiler artifacts
                content = fixDecompilerArtifacts(content);

                // Remove unnecessary imports
                content = removeUnnecessaryImports(content);

                // Normalize formatting
                content = normalizeFormatting(content);

                if (!content.equals(original)) {
                    Files.write(file.toPath(), content.getBytes());
                    stats[0]++;
                }
            } catch (IOException e) {
                System.out.println("[Decompile] Warning: Could not process " + file.getName());
            }
        }
    }

    /**
     * Fix common decompiler artifacts in the source code
     */
    private String fixDecompilerArtifacts(String source) {
        // Remove Vineflower comment headers
        source = source.replaceAll("//\\s*Decompiled with Vineflower.*\\n", "");
        source = source.replaceAll("//\\s*Powered by Fernflower.*\\n", "");

        // Fix $ unnamed inner class references
        // Replace synthetic accessor method calls where possible
        source = source.replaceAll("access\\$(\\d+)\\(", "/* access$\\1 */ ");

        // Fix malformed generic signatures
        source = source.replaceAll("<\\s*>", "<>"); // Fix diamond operator spacing

        return source;
    }

    /**
     * Remove unnecessary/duplicate imports
     */
    private String removeUnnecessaryImports(String source) {
        // Basic cleanup: remove duplicate imports
        List<String> imports = new ArrayList<>();
        List<String> seen = new ArrayList<>();
        String[] lines = source.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith("import ")) {
                if (!seen.contains(line)) {
                    seen.add(line);
                    imports.add(line);
                }
                // Skip duplicate
            } else {
                result.append(line).append("\n");
            }
        }

        // Reconstruct with deduplicated imports
        if (!imports.isEmpty()) {
            StringBuilder withImports = new StringBuilder();
            boolean inPackage = true;
            String[] resultLines = result.toString().split("\n");

            for (String line : resultLines) {
                if (inPackage && line.startsWith("package ")) {
                    withImports.append(line).append("\n\n");
                    for (String imp : imports) {
                        withImports.append(imp).append("\n");
                    }
                    withImports.append("\n");
                    inPackage = false;
                } else if (inPackage && !line.startsWith("package ")) {
                    // No package statement, insert imports at top
                    for (String imp : imports) {
                        withImports.append(imp).append("\n");
                    }
                    withImports.append("\n");
                    withImports.append(line).append("\n");
                    inPackage = false;
                } else {
                    withImports.append(line).append("\n");
                }
            }

            return withImports.toString();
        }

        return source;
    }

    /**
     * Normalize formatting in decompiled source
     */
    private String normalizeFormatting(String source) {
        // Normalize line endings
        source = source.replace("\r\n", "\n").replace("\r", "\n");

        // Remove trailing whitespace
        source = source.replaceAll("[ \\t]+$", "");

        // Ensure file ends with newline
        if (!source.endsWith("\n")) {
            source += "\n";
        }

        // Remove excessive blank lines (more than 2 consecutive)
        source = source.replaceAll("\\n{4,}", "\n\n\n");

        return source;
    }

    // Utility methods

    private int countClassFiles(File dir) {
        int[] count = {0};
        countFilesRecursive(dir, ".class", count);
        return count[0];
    }

    private int countJavaFiles(File dir) {
        int[] count = {0};
        countFilesRecursive(dir, ".java", count);
        return count[0];
    }

    private void countFilesRecursive(File dir, String extension, int[] count) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                countFilesRecursive(f, extension, count);
            } else if (f.getName().endsWith(extension)) {
                count[0]++;
            }
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
