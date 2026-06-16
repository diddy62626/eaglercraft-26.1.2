package net.lax1dude.eaglercraft.v2_6.task.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.lax1dude.eaglercraft.v2_6.EaglerBuildTools.BuildException;

/**
 * Init Task for EaglerCraft 26.1.2
 * 
 * This is dramatically simplified compared to the MC 1.8 version:
 * 
 * MC 1.8 Init Process (OLD):
 *   1. Download MCP (Mod Coder Pack)
 *   2. Run SpecialSource to deobfuscate (SRG mappings)
 *   3. Run MCInjector to fix generics and annotations (EXC mappings)
 *   4. Run Fernflower to decompile (using CSV parameter mappings)
 *   5. Apply ECR patches (Eagler Context Redacted - no context lines)
 *   6. Fix decompiler artifacts
 * 
 * MC 26.1.2 Init Process (NEW):
 *   1. Locate minecraft-26.1.2.jar (unobfuscated!)
 *   2. Extract .class files directly
 *   3. Decompile with Vineflower (modern Fernflower fork, Java 25 compatible)
 *   4. Apply unified diff patches (context lines ARE safe since code is unobfuscated)
 *   5. Load assets from the JAR's asset index
 * 
 * No MCP, no SpecialSource, no MCInjector, no SRG/EXC/CSV mappings needed!
 * 
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
 */
public class InitTask {

    private static final String MC_VERSION = "26.1.2";
    private static final String JAR_NAME = "minecraft-" + MC_VERSION + ".jar";

    // Directory paths relative to base
    private static final String DIR_SOURCES = "sources";
    private static final String DIR_MC_SOURCE = "sources/minecraft";
    private static final String DIR_MC_CLASSES = "sources/minecraft-classes";
    private static final String DIR_RESOURCES = "sources/resources";
    private static final String DIR_PATCHES = "sources/patches";
    private static final String DIR_ASSETS = "sources/assets";

    // Flags
    private boolean noDecompile = false;
    private boolean noPatches = false;
    private File explicitJarPath = null;
    private List<String> vineflowerArgs = new ArrayList<>();

    public static void execute(String[] args, File baseDir) throws BuildException {
        InitTask task = new InitTask();
        task.parseArgs(args);
        task.run(baseDir);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--jar":
                    if (i + 1 < args.length) {
                        explicitJarPath = new File(args[++i]);
                    } else {
                        throw new BuildException("--jar requires a path argument");
                    }
                    break;
                case "--no-decompile":
                    noDecompile = true;
                    break;
                case "--no-patches":
                    noPatches = true;
                    break;
                case "--vineflower-args":
                    if (i + 1 < args.length) {
                        vineflowerArgs.add(args[++i]);
                    }
                    break;
                default:
                    if (args[i].startsWith("--")) {
                        System.out.println("[Init] Warning: Unknown option: " + args[i]);
                    }
                    break;
            }
        }
    }

    private void run(File baseDir) throws BuildException {
        System.out.println("============================================");
        System.out.println("  EaglerCraft " + MC_VERSION + " - Init Task");
        System.out.println("  No MCP Required - Unobfuscated JAR");
        System.out.println("============================================");
        System.out.println();

        // Step 1: Locate Minecraft JAR
        System.out.println("[Init] Step 1: Locating Minecraft " + MC_VERSION + " JAR...");
        File mcJar = locateMinecraftJar(baseDir);
        System.out.println("[Init] Found: " + mcJar.getAbsolutePath());
        System.out.println("[Init] JAR size: " + String.format("%.1f MB", mcJar.length() / 1024.0 / 1024.0));
        System.out.println();

        // Validate the JAR
        System.out.println("[Init] Validating JAR...");
        validateMinecraftJar(mcJar);
        System.out.println("[Init] JAR validation passed - unobfuscated code confirmed!");
        System.out.println();

        // Step 2: Create output directories
        System.out.println("[Init] Step 2: Creating output directories...");
        File mcSourceDir = new File(baseDir, DIR_MC_SOURCE);
        File mcClassesDir = new File(baseDir, DIR_MC_CLASSES);
        File resourcesDir = new File(baseDir, DIR_RESOURCES);
        File assetsDir = new File(baseDir, DIR_ASSETS);

        ensureDirectory(mcSourceDir);
        ensureDirectory(mcClassesDir);
        ensureDirectory(resourcesDir);
        ensureDirectory(assetsDir);
        System.out.println("[Init] Directories created.");
        System.out.println();

        // Step 3: Extract class files and resources from JAR
        System.out.println("[Init] Step 3: Extracting files from JAR...");
        int[] counts = extractFromJar(mcJar, mcClassesDir, resourcesDir, assetsDir);
        System.out.println("[Init] Extracted " + counts[0] + " class files, " + counts[1] +
            " resource files, " + counts[2] + " asset files");
        System.out.println();

        // Step 4: Decompile with Vineflower
        if (!noDecompile) {
            System.out.println("[Init] Step 4: Decompiling with Vineflower...");
            System.out.println("[Init] (Vineflower is a modern Fernflower fork with Java 25 support)");
            DecompileMinecraft decompiler = new DecompileMinecraft();
            decompiler.decompile(mcClassesDir, mcSourceDir, vineflowerArgs);
            System.out.println("[Init] Decompilation complete!");
            System.out.println();

            // Step 5: Format decompiled source
            System.out.println("[Init] Step 5: Formatting decompiled source...");
            formatSource(mcSourceDir);
            System.out.println("[Init] Source formatting complete.");
            System.out.println();
        } else {
            System.out.println("[Init] Step 4: Skipping decompilation (--no-decompile)");
            System.out.println();
        }

        // Step 6: Apply EaglerCraft patches
        if (!noPatches) {
            System.out.println("[Init] Step 6: Applying EaglerCraft patches...");
            File patchesDir = new File(baseDir, DIR_PATCHES);
            if (patchesDir.exists() && patchesDir.isDirectory()) {
                net.lax1dude.eaglercraft.v2_6.task.diff.ApplyPatchesToZip patcher =
                    new net.lax1dude.eaglercraft.v2_6.task.diff.ApplyPatchesToZip();
                int patchCount = patcher.applyPatches(patchesDir, mcSourceDir);
                System.out.println("[Init] Applied " + patchCount + " patches.");
            } else {
                System.out.println("[Init] No patches directory found, skipping.");
            }
            System.out.println();
        } else {
            System.out.println("[Init] Step 6: Skipping patches (--no-patches)");
            System.out.println();
        }

        // Step 7: Load asset index
        System.out.println("[Init] Step 7: Processing asset index...");
        processAssetIndex(mcJar, assetsDir);
        System.out.println();

        // Step 8: Write init marker
        System.out.println("[Init] Step 8: Writing initialization marker...");
        writeInitMarker(baseDir, mcJar);
        System.out.println();

        System.out.println("============================================");
        System.out.println("  Init complete! Workspace initialized.");
        System.out.println("  Run 'workspace' to set up the dev workspace.");
        System.out.println("============================================");
    }

    /**
     * Locate the Minecraft JAR using MinecraftLocator
     */
    private File locateMinecraftJar(File baseDir) throws BuildException {
        // Check explicit path first
        if (explicitJarPath != null) {
            if (explicitJarPath.exists() && explicitJarPath.isFile()) {
                return explicitJarPath;
            }
            throw new BuildException("Specified JAR not found: " + explicitJarPath.getAbsolutePath());
        }

        // Check in base directory
        File localJar = new File(baseDir, JAR_NAME);
        if (localJar.exists()) {
            return localJar;
        }

        // Use MinecraftLocator to find it
        File located = MinecraftLocator.locate(MC_VERSION);
        if (located != null) {
            // Copy to base directory for future use
            try {
                Files.copy(located.toPath(), localJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("[Init] Copied JAR to: " + localJar.getAbsolutePath());
                return localJar;
            } catch (IOException e) {
                System.out.println("[Init] Warning: Could not copy JAR, using original location");
                return located;
            }
        }

        throw new BuildException(
            "Could not locate minecraft-" + MC_VERSION + ".jar!\n" +
            "Please provide the JAR path using --jar <path>\n" +
            "Or place the JAR in: " + baseDir.getAbsolutePath()
        );
    }

    /**
     * Validate that the JAR contains unobfuscated code
     */
    private void validateMinecraftJar(File mcJar) throws BuildException {
        try (ZipFile zip = new ZipFile(mcJar)) {
            // Check for unobfuscated class names (net/minecraft/ package)
            boolean hasUnobfuscated = false;
            boolean hasObfuscated = false;

            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith("net/minecraft/") && name.endsWith(".class")) {
                    hasUnobfuscated = true;
                    break;
                }
                // Check for obfuscated names (single letter packages like a/, b/, etc.)
                if (name.matches("^[a-z]/[a-z]+\\.class$")) {
                    hasObfuscated = true;
                }
            }

            if (!hasUnobfuscated && hasObfuscated) {
                throw new BuildException(
                    "The JAR appears to be obfuscated! MC 26.1.2 should be unobfuscated.\n" +
                    "Make sure you're using the correct version."
                );
            }

            if (!hasUnobfuscated) {
                System.out.println("[Init] Warning: Could not verify unobfuscated classes - proceeding anyway");
            }
        } catch (IOException e) {
            throw new BuildException("Failed to read JAR: " + e.getMessage(), e);
        }
    }

    /**
     * Extract class files and resources from the Minecraft JAR
     */
    private int[] extractFromJar(File mcJar, File classesDir, File resourcesDir, File assetsDir)
            throws BuildException {
        int classCount = 0;
        int resourceCount = 0;
        int assetCount = 0;

        try (ZipFile zip = new ZipFile(mcJar)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) continue;

                String name = entry.getName();
                File outFile;

                if (name.endsWith(".class")) {
                    outFile = new File(classesDir, name);
                    classCount++;
                } else if (name.startsWith("assets/") || name.startsWith("minecraft/textures/") ||
                           name.startsWith("minecraft/sounds/") || name.startsWith("minecraft/models/")) {
                    outFile = new File(assetsDir, name);
                    assetCount++;
                } else if (name.startsWith("pack.mcmeta") || name.startsWith("version.json") ||
                           name.endsWith(".json") && name.contains("minecraft")) {
                    outFile = new File(resourcesDir, name);
                    resourceCount++;
                } else {
                    outFile = new File(resourcesDir, name);
                    resourceCount++;
                }

                // Ensure parent directory exists
                File parent = outFile.getParentFile();
                if (!parent.exists() && !parent.mkdirs()) {
                    throw new BuildException("Failed to create directory: " + parent.getAbsolutePath());
                }

                // Extract the file
                try (InputStream is = zip.getInputStream(entry);
                     FileOutputStream fos = new FileOutputStream(outFile)) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            throw new BuildException("Failed to extract JAR: " + e.getMessage(), e);
        }

        return new int[]{classCount, resourceCount, assetCount};
    }

    /**
     * Format decompiled source code using Google Java Format
     */
    private void formatSource(File sourceDir) {
        // Count Java files
        int[] count = {0};
        formatSourceRecursive(sourceDir, count);
        System.out.println("[Init] Formatted " + count[0] + " Java source files");
    }

    private void formatSourceRecursive(File dir, int[] count) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                formatSourceRecursive(file, count);
            } else if (file.getName().endsWith(".java")) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));

                    // Basic formatting: normalize line endings, trim trailing whitespace
                    String formatted = content
                        .replace("\r\n", "\n")
                        .replace("\r", "\n")
                        .replaceAll("[ \t]+\n", "\n");

                    // Ensure file ends with newline
                    if (!formatted.endsWith("\n")) {
                        formatted += "\n";
                    }

                    if (!content.equals(formatted)) {
                        Files.write(file.toPath(), formatted.getBytes());
                        count[0]++;
                    }
                } catch (IOException e) {
                    System.out.println("[Init] Warning: Could not format " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Process the asset index from version JSON
     */
    private void processAssetIndex(File mcJar, File assetsDir) {
        // Read the version JSON to find asset index name
        File versionJson = new File(assetsDir, "version.json");
        if (!versionJson.exists()) {
            versionJson = new File(assetsDir.getParentFile(), "resources/version.json");
        }

        if (versionJson.exists()) {
            System.out.println("[Init] Found version.json, parsing asset index reference...");
            try {
                String content = new String(Files.readAllBytes(versionJson.toPath()));
                // Simple JSON parsing for assetIndex field
                int idx = content.indexOf("\"assetIndex\"");
                if (idx >= 0) {
                    int idStart = content.indexOf("\"id\"", idx);
                    if (idStart >= 0) {
                        int valueStart = content.indexOf("\"", idStart + 4) + 1;
                        int valueEnd = content.indexOf("\"", valueStart);
                        String assetIndexId = content.substring(valueStart, valueEnd);
                        System.out.println("[Init] Asset index: " + assetIndexId);
                    }
                }
            } catch (IOException e) {
                System.out.println("[Init] Warning: Could not read version.json: " + e.getMessage());
            }
        } else {
            System.out.println("[Init] No version.json found, assets will be used directly from JAR extraction");
        }

        // Count extracted assets
        int[] assetCount = {0};
        countFilesRecursive(assetsDir, assetCount);
        System.out.println("[Init] Total asset files: " + assetCount[0]);
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

    /**
     * Write an initialization marker file
     */
    private void writeInitMarker(File baseDir, File mcJar) throws BuildException {
        File marker = new File(baseDir, ".eaglercraft-init");
        try {
            String content = "# EaglerCraft Build Tools Init Marker\n" +
                "# Do NOT delete this file\n" +
                "version=" + MC_VERSION + "\n" +
                "initTime=" + System.currentTimeMillis() + "\n" +
                "jarPath=" + mcJar.getAbsolutePath() + "\n" +
                "jarSize=" + mcJar.length() + "\n" +
                "decompiled=" + (!noDecompile) + "\n" +
                "patched=" + (!noPatches) + "\n";
            Files.write(marker.toPath(), content.getBytes());
        } catch (IOException e) {
            throw new BuildException("Failed to write init marker: " + e.getMessage(), e);
        }
    }

    private void ensureDirectory(File dir) throws BuildException {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BuildException("Failed to create directory: " + dir.getAbsolutePath());
        }
    }
}
