package net.lax1dude.eaglercraft.v2_6;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * EaglerCraft Build Tools v2.6 for Minecraft 26.1.2
 * 
 * Main entry point for the EaglerCraft build system.
 * This version is significantly simplified compared to the MC 1.8 version
 * because Minecraft 26.1.2 ships with unobfuscated code, eliminating the
 * need for MCP (Mod Coder Pack), SpecialSource, MCInjector, and SRG/EXC mappings.
 * 
 * Commands:
 *   init      - Initialize workspace: locate MC jar, decompile, apply patches
 *   workspace - Create development workspace with IDE project files
 *   compile   - Compile the EaglerCraft client via TeaVM
 *   clean     - Remove all build artifacts
 *   patch     - Apply/update EaglerCraft patches to decompiled source
 *   dist      - Create distribution packages (offline download, signed client)
 * 
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
 */
public class EaglerBuildTools {

    private static final String VERSION = "26.1.2";
    private static final String BUILD_TOOLS_VERSION = "2.6.0";

    /** Base directory for all build operations */
    private static File baseDirectory;

    /** Map of command names to their handler classes */
    private static final Map<String, Class<?>> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("init", net.lax1dude.eaglercraft.v2_6.task.init.InitTask.class);
        COMMANDS.put("workspace", net.lax1dude.eaglercraft.v2_6.task.init.SetupWorkspace.class);
        COMMANDS.put("compile", net.lax1dude.eaglercraft.v2_6.task.teavm.TeaVMBridge.class);
        COMMANDS.put("clean", net.lax1dude.eaglercraft.v2_6.EaglerBuildTools.CleanTask.class);
        COMMANDS.put("patch", net.lax1dude.eaglercraft.v2_6.task.diff.ApplyPatchesToZip.class);
        COMMANDS.put("dist", net.lax1dude.eaglercraft.v2_6.EaglerBuildTools.DistTask.class);
    }

    public static void main(String[] args) {
        printBanner();

        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String command = args[0].toLowerCase();
        String[] commandArgs = new String[args.length - 1];
        if (args.length > 1) {
            System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
        }

        // Determine base directory
        baseDirectory = findBaseDirectory();
        System.out.println("[EaglerBuildTools] Base directory: " + baseDirectory.getAbsolutePath());

        // Handle special commands
        switch (command) {
            case "help":
            case "--help":
            case "-h":
                printUsage();
                return;
            case "version":
            case "--version":
            case "-v":
                System.out.println("EaglerCraft Build Tools v" + BUILD_TOOLS_VERSION);
                System.out.println("Target: Minecraft " + VERSION);
                return;
            case "status":
                printStatus();
                return;
        }

        // Execute the command
        Class<?> handlerClass = COMMANDS.get(command);
        if (handlerClass == null) {
            System.err.println("[ERROR] Unknown command: " + command);
            printUsage();
            System.exit(1);
        }

        try {
            System.out.println("[EaglerBuildTools] Executing command: " + command);
            long startTime = System.currentTimeMillis();

            executeCommand(handlerClass, commandArgs);

            long elapsed = System.currentTimeMillis() - startTime;
            System.out.println("[EaglerBuildTools] Command '" + command + "' completed in " + formatDuration(elapsed));
        } catch (BuildException e) {
            System.err.println("[ERROR] Build failed: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("[ERROR] Caused by: " + e.getCause().getMessage());
            }
            System.exit(2);
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error during command execution:");
            e.printStackTrace();
            System.exit(3);
        }
    }

    /**
     * Execute a build command via reflection
     */
    private static void executeCommand(Class<?> handlerClass, String[] args) throws Exception {
        try {
            java.lang.reflect.Method mainMethod = handlerClass.getMethod("execute", String[].class, File.class);
            mainMethod.invoke(null, args, baseDirectory);
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw e;
        }
    }

    /**
     * Find the base directory (the eaglercraft-26.1.2 root)
     */
    private static File findBaseDirectory() {
        // Check for environment variable
        String envDir = System.getenv("EAGLERCRAFT_HOME");
        if (envDir != null && !envDir.isEmpty()) {
            File f = new File(envDir);
            if (f.isDirectory()) {
                return f;
            }
        }

        // Walk up from current directory looking for marker file
        File current = new File(System.getProperty("user.dir"));
        for (int i = 0; i < 10; i++) {
            if (new File(current, "eaglercraft.version").exists() ||
                new File(current, "buildtools").isDirectory()) {
                return current;
            }
            File parent = current.getParentFile();
            if (parent == null) break;
            current = parent;
        }

        // Fall back to current directory
        return new File(System.getProperty("user.dir"));
    }

    /**
     * Print the EaglerCraft banner
     */
    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║   EaglerCraft Build Tools v" + BUILD_TOOLS_VERSION + "                       ║");
        System.out.println("  ║   Target: Minecraft " + VERSION + " (Unobfuscated)              ║");
        System.out.println("  ║   No MCP Required - Direct decompilation with Vineflower ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Print usage information
     */
    private static void printUsage() {
        System.out.println("Usage: java -jar BuildTools.jar <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  init       Initialize workspace: locate MC jar, decompile, apply patches");
        System.out.println("  workspace  Create development workspace with IDE project files");
        System.out.println("  compile    Compile the EaglerCraft client via TeaVM 0.10.0");
        System.out.println("  clean      Remove all build artifacts");
        System.out.println("  patch      Apply/update EaglerCraft patches to decompiled source");
        System.out.println("  dist       Create distribution packages");
        System.out.println("  status     Show current build status");
        System.out.println("  version    Show version information");
        System.out.println();
        System.out.println("Options for 'init':");
        System.out.println("  --jar <path>       Path to minecraft-26.1.2.jar");
        System.out.println("  --no-decompile     Skip decompilation step");
        System.out.println("  --no-patches       Skip patch application");
        System.out.println("  --vineflower-args  Additional Vineflower arguments");
        System.out.println();
        System.out.println("Options for 'compile':");
        System.out.println("  --release          Build release version (minified)");
        System.out.println("  --debug            Build debug version (with source maps)");
        System.out.println("  --no-obfuscate     Skip TeaVM obfuscation");
        System.out.println();
        System.out.println("Environment Variables:");
        System.out.println("  EAGLERCRAFT_HOME   Base directory for EaglerCraft project");
        System.out.println("  JAVA_HOME          Java installation directory");
    }

    /**
     * Print the current build status
     */
    private static void printStatus() {
        System.out.println("Build Status:");
        System.out.println("─────────────────────────────────────────");

        File[] dirs = {
            new File(baseDirectory, "buildtools"),
            new File(baseDirectory, "sources"),
            new File(baseDirectory, "sources/minecraft"),
            new File(baseDirectory, "sources/patches"),
            new File(baseDirectory, "workspace"),
            new File(baseDirectory, "dist")
        };

        String[] labels = {
            "Build Tools",
            "EaglerCraft Source",
            "Decompiled MC Source",
            "Patches",
            "Workspace",
            "Distribution"
        };

        for (int i = 0; i < dirs.length; i++) {
            String status = dirs[i].exists() ? "✓ EXISTS" : "✗ MISSING";
            System.out.println("  " + labels[i] + ": " + status);
            if (dirs[i].exists()) {
                System.out.println("    Path: " + dirs[i].getAbsolutePath());
            }
        }

        // Check for minecraft jar
        File mcJar = new File(baseDirectory, "minecraft-26.1.2.jar");
        if (mcJar.exists()) {
            System.out.println("  Minecraft JAR: ✓ FOUND (" + (mcJar.length() / 1024 / 1024) + " MB)");
        } else {
            System.out.println("  Minecraft JAR: ✗ NOT FOUND (run 'init' to locate)");
        }

        System.out.println("─────────────────────────────────────────");
    }

    /**
     * Format milliseconds into a human-readable duration
     */
    private static String formatDuration(long millis) {
        if (millis < 1000) {
            return millis + "ms";
        }
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        }
        return seconds + "s";
    }

    /**
     * Get the base directory
     */
    public static File getBaseDirectory() {
        return baseDirectory;
    }

    /**
     * Custom build exception
     */
    public static class BuildException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public BuildException(String message) {
            super(message);
        }

        public BuildException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Clean task - removes all build artifacts
     */
    public static class CleanTask {
        public static void execute(String[] args, File baseDir) throws BuildException {
            System.out.println("[Clean] Cleaning build artifacts...");

            String[] cleanDirs = {
                "sources/minecraft",
                "workspace/bin",
                "workspace/.teavm",
                "dist",
                "buildtools/out",
                "buildtools/tmp"
            };

            int totalFiles = 0;
            for (String dirName : cleanDirs) {
                File dir = new File(baseDir, dirName);
                if (dir.exists()) {
                    int count = deleteRecursive(dir);
                    totalFiles += count;
                    System.out.println("[Clean] Removed: " + dirName + " (" + count + " files)");
                } else {
                    System.out.println("[Clean] Skipped: " + dirName + " (not found)");
                }
            }

            // Clean specific file types
            File sourcesDir = new File(baseDir, "sources");
            if (sourcesDir.exists()) {
                int count = cleanFilesByExtension(sourcesDir, ".class");
                System.out.println("[Clean] Removed " + count + " .class files from sources/");
            }

            System.out.println("[Clean] Total files removed: " + totalFiles);

            boolean deep = false;
            for (String arg : args) {
                if ("--deep".equals(arg)) {
                    deep = true;
                    break;
                }
            }

            if (deep) {
                System.out.println("[Clean] Deep clean: removing workspace and decompiled source...");
                File workspace = new File(baseDir, "workspace");
                File mcSource = new File(baseDir, "sources/minecraft");
                if (workspace.exists()) {
                    deleteRecursive(workspace);
                }
                if (mcSource.exists()) {
                    deleteRecursive(mcSource);
                }
                System.out.println("[Clean] Deep clean complete.");
            }
        }

        private static int deleteRecursive(File file) {
            int count = 0;
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children != null) {
                    for (File child : children) {
                        count += deleteRecursive(child);
                    }
                }
            }
            if (file.delete()) {
                count++;
            }
            return count;
        }

        private static int cleanFilesByExtension(File dir, String extension) {
            int count = 0;
            if (dir.isDirectory()) {
                File[] children = dir.listFiles();
                if (children != null) {
                    for (File child : children) {
                        if (child.isDirectory()) {
                            count += cleanFilesByExtension(child, extension);
                        } else if (child.getName().endsWith(extension)) {
                            if (child.delete()) count++;
                        }
                    }
                }
            }
            return count;
        }
    }

    /**
     * Distribution task - creates offline downloads and signed clients
     */
    public static class DistTask {
        public static void execute(String[] args, File baseDir) throws BuildException {
            System.out.println("[Dist] Creating distribution packages...");

            File compileOutput = new File(baseDir, "workspace/bin");
            if (!compileOutput.exists() || compileOutput.list() == null || compileOutput.list().length == 0) {
                throw new BuildException("No compiled output found. Run 'compile' first.");
            }

            File distDir = new File(baseDir, "dist");
            if (!distDir.exists() && !distDir.mkdirs()) {
                throw new BuildException("Failed to create dist directory: " + distDir.getAbsolutePath());
            }

            boolean offline = true;
            boolean signed = false;
            String versionSuffix = "";

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--offline":
                        offline = true;
                        break;
                    case "--signed":
                        signed = true;
                        break;
                    case "--version-suffix":
                        if (i + 1 < args.length) {
                            versionSuffix = args[++i];
                        }
                        break;
                }
            }

            try {
                if (offline) {
                    createOfflineDownload(distDir, compileOutput, versionSuffix);
                }
                if (signed) {
                    createSignedClient(distDir, compileOutput, versionSuffix);
                }
            } catch (IOException e) {
                throw new BuildException("Failed to create distribution: " + e.getMessage(), e);
            }

            System.out.println("[Dist] Distribution packages created in: " + distDir.getAbsolutePath());
        }

        private static void createOfflineDownload(File distDir, File compileOutput, String versionSuffix)
                throws IOException {
            System.out.println("[Dist] Creating offline download package...");

            File templateFile = new File(distDir.getParentFile(),
                "sources/setup/workspace_template/javascript/OfflineDownloadTemplate.txt");
            if (!templateFile.exists()) {
                System.out.println("[Dist] Warning: Offline download template not found, using defaults");
            }

            String fileName = "EaglerCraft_" + VERSION + versionSuffix + "_Offline.html";
            File outputFile = new File(distDir, fileName);

            // Bundle all JS and assets into a single HTML file
            java.util.List<File> jsFiles = new java.util.ArrayList<>();
            collectFiles(compileOutput, ".js", jsFiles);

            StringBuilder bundle = new StringBuilder();
            for (File jsFile : jsFiles) {
                String content = new String(java.nio.file.Files.readAllBytes(jsFile.toPath()));
                bundle.append(content).append("\n");
            }

            // Read index.html template
            File htmlTemplate = new File(distDir.getParentFile(),
                "sources/setup/workspace_template/javascript/index.html");
            String htmlContent;
            if (htmlTemplate.exists()) {
                htmlContent = new String(java.nio.file.Files.readAllBytes(htmlTemplate.toPath()));
            } else {
                htmlContent = "<!DOCTYPE html><html><head><title>EaglerCraft " + VERSION + "</title></head><body><script>";
            }

            // Inject bundled JS
            String injectMarker = "<!-- EAGLERCRAFT_BUNDLE -->";
            if (htmlContent.contains(injectMarker)) {
                htmlContent = htmlContent.replace(injectMarker,
                    "<script type=\"text/javascript\">\n" + bundle.toString() + "\n</script>");
            } else {
                htmlContent = htmlContent.replace("</body>",
                    "<script type=\"text/javascript\">\n" + bundle.toString() + "\n</script></body>");
            }

            java.nio.file.Files.write(outputFile.toPath(), htmlContent.getBytes());
            System.out.println("[Dist] Offline download: " + outputFile.getName() + " (" +
                (outputFile.length() / 1024 / 1024) + " MB)");
        }

        private static void createSignedClient(File distDir, File compileOutput, String versionSuffix)
                throws IOException {
            System.out.println("[Dist] Creating signed client package...");

            File templateFile = new File(distDir.getParentFile(),
                "sources/setup/workspace_template/javascript/SignedClientTemplate.txt");
            if (!templateFile.exists()) {
                System.out.println("[Dist] Warning: Signed client template not found, skipping");
                return;
            }

            String dirName = "EaglerCraft_" + VERSION + versionSuffix + "_Signed";
            File signedDir = new File(distDir, dirName);
            if (!signedDir.exists() && !signedDir.mkdirs()) {
                throw new IOException("Failed to create signed client directory");
            }

            // Copy compiled files
            copyRecursive(compileOutput, signedDir);

            System.out.println("[Dist] Signed client package: " + signedDir.getName());
        }

        private static void collectFiles(File dir, String extension, java.util.List<File> result) {
            File[] files = dir.listFiles();
            if (files == null) return;
            for (File f : files) {
                if (f.isDirectory()) {
                    collectFiles(f, extension, result);
                } else if (f.getName().endsWith(extension)) {
                    result.add(f);
                }
            }
        }

        private static void copyRecursive(File src, File dest) throws IOException {
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
                java.nio.file.Files.copy(src.toPath(), dest.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
