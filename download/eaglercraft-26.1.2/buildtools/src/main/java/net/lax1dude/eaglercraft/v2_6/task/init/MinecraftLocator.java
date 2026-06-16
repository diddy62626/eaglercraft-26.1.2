package net.lax1dude.eaglercraft.v2_6.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.lax1dude.eaglercraft.v2_6.EaglerBuildTools.BuildException;

/**
 * Minecraft Locator for MC 26.1.2
 * 
 * Locates the Minecraft installation and JAR file across different platforms.
 * Supports Windows, macOS, and Linux.
 * 
 * Search strategy:
 *   1. Check ~/.minecraft/versions/26.1.2/ (default MC install location)
 *   2. Check common alternative installation paths
 *   3. Check launcher-specific directories (MultiMC, Prism, FTB, etc.)
 *   4. Validate that the found JAR contains unobfuscated class names
 *   5. Read the version JSON for asset index information
 * 
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
 */
public class MinecraftLocator {

    private static final String MC_VERSION = "26.1.2";
    private static final String JAR_NAME = "minecraft-" + MC_VERSION + ".jar";
    private static final String VERSION_JSON = MC_VERSION + ".json";

    /** Cached OS type */
    private static final OSType CURRENT_OS = detectOS();

    /**
     * Locate the Minecraft JAR for the given version
     * 
     * @param version The Minecraft version string (e.g., "26.1.2")
     * @return The File pointing to the JAR, or null if not found
     */
    public static File locate(String version) {
        System.out.println("[Locator] Searching for Minecraft " + version + " JAR...");
        System.out.println("[Locator] Current OS: " + CURRENT_OS);

        // Strategy 1: Default Minecraft installation
        File defaultJar = checkDefaultInstall(version);
        if (defaultJar != null) {
            System.out.println("[Locator] Found in default installation: " + defaultJar.getAbsolutePath());
            return defaultJar;
        }

        // Strategy 2: Alternative Minecraft directories
        File altJar = checkAlternativeInstalls(version);
        if (altJar != null) {
            System.out.println("[Locator] Found in alternative location: " + altJar.getAbsolutePath());
            return altJar;
        }

        // Strategy 3: Third-party launchers
        File launcherJar = checkThirdPartyLaunchers(version);
        if (launcherJar != null) {
            System.out.println("[Locator] Found in third-party launcher: " + launcherJar.getAbsolutePath());
            return launcherJar;
        }

        // Strategy 4: Environment variable
        String mcHome = System.getenv("MINECRAFT_HOME");
        if (mcHome != null && !mcHome.isEmpty()) {
            File envJar = new File(mcHome, "versions/" + version + "/" + JAR_NAME);
            if (isValidJar(envJar)) {
                System.out.println("[Locator] Found via MINECRAFT_HOME: " + envJar.getAbsolutePath());
                return envJar;
            }
        }

        // Strategy 5: Search common directories
        File searchJar = searchCommonDirectories(version);
        if (searchJar != null) {
            System.out.println("[Locator] Found via directory search: " + searchJar.getAbsolutePath());
            return searchJar;
        }

        System.out.println("[Locator] Minecraft " + version + " JAR not found.");
        System.out.println("[Locator] Please install Minecraft " + version + " or provide the JAR path with --jar");
        return null;
    }

    /**
     * Get the default .minecraft directory for the current platform
     */
    public static File getDefaultMinecraftDir() {
        String userHome = System.getProperty("user.home");
        if (userHome == null) return null;

        switch (CURRENT_OS) {
            case WINDOWS:
                String appData = System.getenv("APPDATA");
                if (appData != null && !appData.isEmpty()) {
                    return new File(appData, ".minecraft");
                }
                return new File(userHome, "AppData/Roaming/.minecraft");

            case MACOS:
                return new File(userHome, "Library/Application Support/minecraft");

            case LINUX:
            default:
                // Check XDG_DATA_HOME first
                String xdgData = System.getenv("XDG_DATA_HOME");
                if (xdgData != null && !xdgData.isEmpty()) {
                    return new File(xdgData, ".minecraft");
                }
                return new File(userHome, ".minecraft");
        }
    }

    /**
     * Check the default Minecraft installation directory
     */
    private static File checkDefaultInstall(String version) {
        File mcDir = getDefaultMinecraftDir();
        if (mcDir == null || !mcDir.exists()) {
            System.out.println("[Locator] Default MC directory not found: " +
                (mcDir != null ? mcDir.getAbsolutePath() : "null"));
            return null;
        }

        File versionDir = new File(mcDir, "versions/" + version);
        if (!versionDir.exists() || !versionDir.isDirectory()) {
            System.out.println("[Locator] Version directory not found: " + versionDir.getAbsolutePath());
            return null;
        }

        File jarFile = new File(versionDir, JAR_NAME);
        if (isValidJar(jarFile)) {
            return jarFile;
        }

        // Try alternate naming
        File altJar = new File(versionDir, version + ".jar");
        if (isValidJar(altJar)) {
            return altJar;
        }

        System.out.println("[Locator] JAR not found in: " + versionDir.getAbsolutePath());
        return null;
    }

    /**
     * Check alternative Minecraft installation directories
     */
    private static File checkAlternativeInstalls(String version) {
        String userHome = System.getProperty("user.home");
        if (userHome == null) return null;

        List<File> altDirs = new ArrayList<>();

        switch (CURRENT_OS) {
            case WINDOWS:
                String appData = System.getenv("APPDATA");
                if (appData != null) {
                    altDirs.add(new File(appData, "minecraft"));
                    altDirs.add(new File(appData, ".minecraft"));
                }
                String localAppData = System.getenv("LOCALAPPDATA");
                if (localAppData != null) {
                    altDirs.add(new File(localAppData, "minecraft"));
                    altDirs.add(new File(localAppData, "Packages/Microsoft.42971271364_8wekyb3d8bbwe/LocalCache/Local/minecraft"));
                }
                break;

            case MACOS:
                altDirs.add(new File(userHome, "Library/Application Support/minecraft"));
                altDirs.add(new File(userHome, ".minecraft"));
                break;

            case LINUX:
                altDirs.add(new File(userHome, ".minecraft"));
                String xdgData = System.getenv("XDG_DATA_HOME");
                if (xdgData != null) {
                    altDirs.add(new File(xdgData, "minecraft"));
                    altDirs.add(new File(xdgData, ".minecraft"));
                }
                altDirs.add(new File(userHome, ".local/share/minecraft"));
                break;
        }

        // Also check flatpak/snap installations on Linux
        if (CURRENT_OS == OSType.LINUX) {
            altDirs.add(new File(userHome, ".var/app/com.mojang.Minecraft/.minecraft"));
            altDirs.add(new File("/snap/minecraft/common/.minecraft"));
        }

        for (File dir : altDirs) {
            if (!dir.exists()) continue;
            File jar = new File(dir, "versions/" + version + "/" + JAR_NAME);
            if (isValidJar(jar)) {
                return jar;
            }
            File altJar = new File(dir, "versions/" + version + "/" + version + ".jar");
            if (isValidJar(altJar)) {
                return altJar;
            }
        }

        return null;
    }

    /**
     * Check third-party launcher directories
     */
    private static File checkThirdPartyLaunchers(String version) {
        String userHome = System.getProperty("user.home");
        if (userHome == null) return null;

        List<File> launcherDirs = new ArrayList<>();

        // MultiMC / Prism Launcher
        switch (CURRENT_OS) {
            case WINDOWS:
                String appData = System.getenv("APPDATA");
                if (appData != null) {
                    launcherDirs.add(new File(appData, "Multimc"));
                    launcherDirs.add(new File(appData, "PrismLauncher"));
                    launcherDirs.add(new File(appData, "ATLauncher"));
                }
                break;
            case MACOS:
                launcherDirs.add(new File(userHome, "Applications/MultiMC"));
                launcherDirs.add(new File(userHome, "Applications/PrismLauncher"));
                break;
            case LINUX:
                launcherDirs.add(new File(userHome, "Multimc"));
                launcherDirs.add(new File(userHome, "PrismLauncher"));
                launcherDirs.add(new File(userHome, ".local/share/multimc"));
                launcherDirs.add(new File(userHome, ".local/share/PrismLauncher"));
                launcherDirs.add(new File(userHome, ".local/share/ATLauncher"));
                break;
        }

        for (File launcherDir : launcherDirs) {
            if (!launcherDir.exists()) continue;
            File found = searchLauncherDir(launcherDir, version);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    /**
     * Search a launcher directory for the version JAR
     */
    private static File searchLauncherDir(File launcherDir, String version) {
        // MultiMC/Prism store instances with their own .minecraft dirs
        File instancesDir = new File(launcherDir, "instances");
        if (instancesDir.exists() && instancesDir.isDirectory()) {
            File[] instances = instancesDir.listFiles();
            if (instances == null) return null;

            for (File instance : instances) {
                if (!instance.isDirectory()) continue;
                File mcDir = new File(instance, ".minecraft");
                if (!mcDir.exists()) {
                    mcDir = new File(instance, "minecraft");
                }
                if (!mcDir.exists()) continue;

                File jar = new File(mcDir, "versions/" + version + "/" + JAR_NAME);
                if (isValidJar(jar)) {
                    return jar;
                }
            }
        }

        return null;
    }

    /**
     * Search common directories for the JAR
     */
    private static File searchCommonDirectories(String version) {
        String userHome = System.getProperty("user.home");
        if (userHome == null) return null;

        // Search common download directories
        List<File> searchDirs = new ArrayList<>();
        searchDirs.add(new File(userHome, "Downloads"));
        searchDirs.add(new File(userHome, "Desktop"));

        for (File dir : searchDirs) {
            if (!dir.exists() || !dir.isDirectory()) continue;
            File found = searchForJar(dir, version, 2); // Depth limit of 2
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    /**
     * Recursively search a directory for the MC JAR
     */
    private static File searchForJar(File dir, String version, int maxDepth) {
        if (maxDepth <= 0) return null;

        File[] files = dir.listFiles();
        if (files == null) return null;

        for (File file : files) {
            if (file.isDirectory()) {
                File found = searchForJar(file, version, maxDepth - 1);
                if (found != null) return found;
            } else if (file.getName().equals(JAR_NAME) || file.getName().equals(version + ".jar")) {
                if (isValidJar(file)) {
                    return file;
                }
            }
        }

        return null;
    }

    /**
     * Validate that a JAR file exists and contains unobfuscated Minecraft classes
     */
    public static boolean isValidJar(File jarFile) {
        if (jarFile == null || !jarFile.exists() || !jarFile.isFile()) {
            return false;
        }

        // Check file size (MC JARs are typically 30+ MB)
        if (jarFile.length() < 10 * 1024 * 1024) { // Less than 10MB is suspicious
            System.out.println("[Locator] Warning: JAR file is suspiciously small (" +
                jarFile.length() / 1024 / 1024 + " MB)");
            // Don't return false - might still be valid
        }

        try (ZipFile zip = new ZipFile(jarFile)) {
            // Check for unobfuscated class names
            boolean hasMinecraftPackage = false;
            boolean hasVersionEntry = false;

            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith("net/minecraft/") && name.endsWith(".class")) {
                    hasMinecraftPackage = true;
                }
                if (name.equals("version.json") || name.equals("pack.mcmeta")) {
                    hasVersionEntry = true;
                }

                if (hasMinecraftPackage && hasVersionEntry) {
                    return true;
                }
            }

            // Even without version.json, if we have MC classes it's likely valid
            return hasMinecraftPackage;

        } catch (IOException e) {
            System.out.println("[Locator] Error reading JAR: " + e.getMessage());
            return false;
        }
    }

    /**
     * Read the asset index name from the version JSON
     * 
     * @param versionDir The directory containing the version JSON
     * @return The asset index name (e.g., "18"), or null if not found
     */
    public static String readAssetIndex(File versionDir) {
        File jsonFile = new File(versionDir, VERSION_JSON);
        if (!jsonFile.exists()) {
            jsonFile = new File(versionDir, MC_VERSION + ".json");
        }

        if (!jsonFile.exists()) {
            System.out.println("[Locator] Version JSON not found in: " + versionDir.getAbsolutePath());
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(jsonFile.toPath()));

            // Simple JSON parsing - find "assetIndex" -> "id"
            int assetIdx = content.indexOf("\"assetIndex\"");
            if (assetIdx < 0) return null;

            int idIdx = content.indexOf("\"id\"", assetIdx);
            if (idIdx < 0) return null;

            int valueStart = content.indexOf("\"", idIdx + 4) + 1;
            int valueEnd = content.indexOf("\"", valueStart);

            if (valueStart > 0 && valueEnd > valueStart) {
                return content.substring(valueStart, valueEnd);
            }
        } catch (IOException e) {
            System.out.println("[Locator] Error reading version JSON: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get the assets directory for the current Minecraft installation
     */
    public static File getAssetsDirectory() {
        File mcDir = getDefaultMinecraftDir();
        if (mcDir == null) return null;

        File assetsDir = new File(mcDir, "assets");
        if (assetsDir.exists() && assetsDir.isDirectory()) {
            return assetsDir;
        }

        return null;
    }

    /**
     * Get the libraries directory for the current Minecraft installation
     */
    public static File getLibrariesDirectory() {
        File mcDir = getDefaultMinecraftDir();
        if (mcDir == null) return null;

        File libsDir = new File(mcDir, "libraries");
        if (libsDir.exists() && libsDir.isDirectory()) {
            return libsDir;
        }

        return null;
    }

    /**
     * Detect the current operating system
     */
    private static OSType detectOS() {
        String osName = System.getProperty("os.name", "").toLowerCase();
        if (osName.contains("win")) {
            return OSType.WINDOWS;
        } else if (osName.contains("mac") || osName.contains("darwin")) {
            return OSType.MACOS;
        } else {
            return OSType.LINUX;
        }
    }

    /**
     * Operating system types
     */
    private enum OSType {
        WINDOWS,
        MACOS,
        LINUX
    }

    /**
     * Get information about the located Minecraft installation
     */
    public static String getInstallationInfo(File jarFile) {
        StringBuilder info = new StringBuilder();
        info.append("Minecraft Installation Info:\n");
        info.append("  Version: ").append(MC_VERSION).append("\n");
        info.append("  JAR Path: ").append(jarFile.getAbsolutePath()).append("\n");
        info.append("  JAR Size: ").append(String.format("%.1f MB", jarFile.length() / 1024.0 / 1024.0)).append("\n");
        info.append("  OS: ").append(CURRENT_OS).append("\n");

        File mcDir = getDefaultMinecraftDir();
        if (mcDir != null) {
            info.append("  MC Directory: ").append(mcDir.getAbsolutePath()).append("\n");
            info.append("  MC Directory exists: ").append(mcDir.exists()).append("\n");
        }

        File versionDir = jarFile.getParentFile();
        if (versionDir != null) {
            String assetIndex = readAssetIndex(versionDir);
            if (assetIndex != null) {
                info.append("  Asset Index: ").append(assetIndex).append("\n");
            }
        }

        return info.toString();
    }
}
