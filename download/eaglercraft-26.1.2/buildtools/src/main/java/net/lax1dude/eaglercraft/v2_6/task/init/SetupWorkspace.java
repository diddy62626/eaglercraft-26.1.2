package net.lax1dude.eaglercraft.v2_6.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v2_6.EaglerBuildTools.BuildException;

/**
 * Setup Workspace Task for EaglerCraft 26.1.2
 * 
 * Creates the development workspace structure:
 *   - Copies workspace template files
 *   - Copies EaglerCraft source files
 *   - Extracts decompiled Minecraft source
 *   - Creates IDE project files (Eclipse, IntelliJ, VS Code)
 *   - Sets up TeaVM build configuration
 * 
 * Workspace structure:
 *   workspace/
 *     ├── src/
 *     │   ├── main/java/           - EaglerCraft + MC source
 *     │   └── teavm/java/          - TeaVM-specific source
 *     ├── resources/
 *     │   ├── assets/              - MC assets
 *     │   └── epk/                 - EaglerCraft package files
 *     ├── javascript/              - HTML/JS output
 *     ├── build.gradle             - Gradle build script
 *     ├── .classpath               - Eclipse classpath
 *     ├── .project                 - Eclipse project
 *     ├── eaglercraft.iml          - IntelliJ module
 *     └── .vscode/                 - VS Code settings
 * 
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
 */
public class SetupWorkspace {

    private static final String MC_VERSION = "26.1.2";
    private static final String TEAVM_VERSION = "0.10.0";

    // Source directories
    private static final String DIR_EAGLERCRAFT_SRC = "sources/main/java";
    private static final String DIR_TEAVM_SRC = "sources/teavm/java";
    private static final String DIR_MC_SRC = "sources/minecraft";
    private static final String DIR_RESOURCES = "sources/resources";
    private static final String DIR_ASSETS = "sources/assets";
    private static final String DIR_TEMPLATE = "sources/setup/workspace_template";

    // Workspace directories
    private static final String DIR_WORKSPACE = "workspace";
    private static final String DIR_WS_SRC = "workspace/src/main/java";
    private static final String DIR_WS_TEAVM = "workspace/src/teavm/java";
    private static final String DIR_WS_RESOURCES = "workspace/resources";
    private static final String DIR_WS_JAVASCRIPT = "workspace/javascript";
    private static final String DIR_WS_LIB = "workspace/lib";

    // IDE options
    private boolean createEclipse = true;
    private boolean createIntelliJ = true;
    private boolean createVSCode = true;

    public static void execute(String[] args, File baseDir) throws BuildException {
        SetupWorkspace task = new SetupWorkspace();
        task.parseArgs(args);
        task.run(baseDir);
    }

    private void parseArgs(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "--no-eclipse":
                    createEclipse = false;
                    break;
                case "--no-intellij":
                    createIntelliJ = false;
                    break;
                case "--no-vscode":
                    createVSCode = false;
                    break;
                case "--no-ide":
                    createEclipse = false;
                    createIntelliJ = false;
                    createVSCode = false;
                    break;
            }
        }
    }

    private void run(File baseDir) throws BuildException {
        System.out.println("============================================");
        System.out.println("  EaglerCraft " + MC_VERSION + " - Setup Workspace");
        System.out.println("============================================");
        System.out.println();

        // Verify init was run
        File initMarker = new File(baseDir, ".eaglercraft-init");
        if (!initMarker.exists()) {
            throw new BuildException(
                "Init marker not found! Please run 'init' first to decompile Minecraft."
            );
        }

        // Step 1: Create workspace directories
        System.out.println("[Workspace] Step 1: Creating workspace directories...");
        createWorkspaceDirectories(baseDir);
        System.out.println();

        // Step 2: Copy EaglerCraft source
        System.out.println("[Workspace] Step 2: Copying EaglerCraft source files...");
        int eaglerCount = copySourceFiles(
            new File(baseDir, DIR_EAGLERCRAFT_SRC),
            new File(baseDir, DIR_WS_SRC)
        );
        System.out.println("[Workspace] Copied " + eaglerCount + " EaglerCraft source files.");
        System.out.println();

        // Step 3: Copy TeaVM source
        System.out.println("[Workspace] Step 3: Copying TeaVM source files...");
        int teavmCount = copySourceFiles(
            new File(baseDir, DIR_TEAVM_SRC),
            new File(baseDir, DIR_WS_TEAVM)
        );
        System.out.println("[Workspace] Copied " + teavmCount + " TeaVM source files.");
        System.out.println();

        // Step 4: Copy decompiled Minecraft source
        System.out.println("[Workspace] Step 4: Copying decompiled Minecraft source...");
        int mcCount = copySourceFiles(
            new File(baseDir, DIR_MC_SRC),
            new File(baseDir, DIR_WS_SRC)
        );
        System.out.println("[Workspace] Copied " + mcCount + " Minecraft source files.");
        System.out.println();

        // Step 5: Copy resources and assets
        System.out.println("[Workspace] Step 5: Copying resources and assets...");
        int resCount = copyResources(baseDir);
        System.out.println("[Workspace] Copied " + resCount + " resource files.");
        System.out.println();

        // Step 6: Copy workspace template
        System.out.println("[Workspace] Step 6: Setting up workspace template...");
        copyTemplateFiles(baseDir);
        System.out.println();

        // Step 7: Create build configuration
        System.out.println("[Workspace] Step 7: Creating build configuration...");
        createBuildConfig(baseDir);
        System.out.println();

        // Step 8: Create IDE project files
        System.out.println("[Workspace] Step 8: Creating IDE project files...");
        if (createEclipse) {
            createEclipseProject(baseDir);
        }
        if (createIntelliJ) {
            createIntelliJProject(baseDir);
        }
        if (createVSCode) {
            createVSCodeConfig(baseDir);
        }
        System.out.println();

        // Step 9: Download TeaVM dependencies
        System.out.println("[Workspace] Step 9: Verifying TeaVM dependencies...");
        verifyTeaVMDependencies(baseDir);
        System.out.println();

        System.out.println("============================================");
        System.out.println("  Workspace setup complete!");
        System.out.println("  Open 'workspace/' in your IDE to start developing.");
        System.out.println("  Run 'compile' to build the EaglerCraft client.");
        System.out.println("============================================");
    }

    /**
     * Create all workspace directories
     */
    private void createWorkspaceDirectories(File baseDir) throws BuildException {
        String[] dirs = {
            DIR_WS_SRC,
            DIR_WS_TEAVM,
            DIR_WS_RESOURCES,
            DIR_WS_RESOURCES + "/assets",
            DIR_WS_RESOURCES + "/epk",
            DIR_WS_JAVASCRIPT,
            DIR_WS_LIB
        };

        for (String dir : dirs) {
            File f = new File(baseDir, dir);
            if (!f.exists() && !f.mkdirs()) {
                throw new BuildException("Failed to create directory: " + f.getAbsolutePath());
            }
        }

        System.out.println("[Workspace] Created workspace directory structure.");
    }

    /**
     * Copy source files from one directory to another, preserving structure
     */
    private int copySourceFiles(File srcDir, File destDir) throws BuildException {
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            System.out.println("[Workspace] Source directory not found: " + srcDir.getAbsolutePath());
            return 0;
        }

        int[] count = {0};
        copyRecursive(srcDir, destDir, count);
        return count[0];
    }

    private void copyRecursive(File src, File dest, int[] count) throws BuildException {
        if (src.isDirectory()) {
            if (!dest.exists() && !dest.mkdirs()) {
                throw new BuildException("Failed to create directory: " + dest.getAbsolutePath());
            }
            File[] children = src.listFiles();
            if (children != null) {
                for (File child : children) {
                    copyRecursive(child, new File(dest, child.getName()), count);
                }
            }
        } else if (src.getName().endsWith(".java")) {
            try {
                Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                count[0]++;
            } catch (IOException e) {
                throw new BuildException("Failed to copy " + src.getName() + ": " + e.getMessage(), e);
            }
        }
    }

    /**
     * Copy resources and assets to workspace
     */
    private int copyResources(File baseDir) throws BuildException {
        int count = 0;

        // Copy resources
        File resourcesSrc = new File(baseDir, DIR_RESOURCES);
        File resourcesDest = new File(baseDir, DIR_WS_RESOURCES);
        if (resourcesSrc.exists()) {
            count += copyAllFiles(resourcesSrc, resourcesDest);
        }

        // Copy assets
        File assetsSrc = new File(baseDir, DIR_ASSETS);
        File assetsDest = new File(baseDir, DIR_WS_RESOURCES + "/assets");
        if (assetsSrc.exists()) {
            count += copyAllFiles(assetsSrc, assetsDest);
        }

        return count;
    }

    private int copyAllFiles(File src, File dest) throws BuildException {
        int[] count = {0};
        copyAllRecursive(src, dest, count);
        return count[0];
    }

    private void copyAllRecursive(File src, File dest, int[] count) throws BuildException {
        if (src.isDirectory()) {
            if (!dest.exists() && !dest.mkdirs()) {
                throw new BuildException("Failed to create directory: " + dest.getAbsolutePath());
            }
            File[] children = src.listFiles();
            if (children != null) {
                for (File child : children) {
                    copyAllRecursive(child, new File(dest, child.getName()), count);
                }
            }
        } else {
            try {
                Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                count[0]++;
            } catch (IOException e) {
                throw new BuildException("Failed to copy " + src.getName() + ": " + e.getMessage(), e);
            }
        }
    }

    /**
     * Copy workspace template files
     */
    private void copyTemplateFiles(File baseDir) throws BuildException {
        File templateDir = new File(baseDir, DIR_TEMPLATE);
        if (!templateDir.exists()) {
            System.out.println("[Workspace] Template directory not found, skipping template copy.");
            return;
        }

        File jsDest = new File(baseDir, DIR_WS_JAVASCRIPT);
        int count = copyAllFiles(templateDir, jsDest);
        System.out.println("[Workspace] Copied " + count + " template files.");
    }

    /**
     * Create the Gradle build configuration
     */
    private void createBuildConfig(File baseDir) throws BuildException {
        File wsDir = new File(baseDir, DIR_WORKSPACE);

        // build.gradle
        File buildGradle = new File(wsDir, "build.gradle");
        try {
            String content = generateBuildGradle();
            Files.write(buildGradle.toPath(), content.getBytes());
            System.out.println("[Workspace] Created build.gradle");
        } catch (IOException e) {
            throw new BuildException("Failed to create build.gradle: " + e.getMessage(), e);
        }

        // gradle.properties
        File gradleProps = new File(wsDir, "gradle.properties");
        try {
            String content = "mcVersion=" + MC_VERSION + "\n" +
                "teavmVersion=" + TEAVM_VERSION + "\n" +
                "projectGroup=net.lax1dude.eaglercraft\n" +
                "projectVersion=2.6.0\n";
            Files.write(gradleProps.toPath(), content.getBytes());
            System.out.println("[Workspace] Created gradle.properties");
        } catch (IOException e) {
            throw new BuildException("Failed to create gradle.properties: " + e.getMessage(), e);
        }
    }

    /**
     * Generate the build.gradle content
     */
    private String generateBuildGradle() {
        return "plugins {\n" +
            "    id 'java'\n" +
            "}\n\n" +
            "group = 'net.lax1dude.eaglercraft'\n" +
            "version = '2.6.0'\n\n" +
            "repositories {\n" +
            "    mavenCentral()\n" +
            "    maven { url 'https://teavm.org/maven/repository' }\n" +
            "}\n\n" +
            "dependencies {\n" +
            "    implementation \"org.teavm:teavm-core:${teavmVersion}\"\n" +
            "    implementation \"org.teavm:teavm-jso:${teavmVersion}\"\n" +
            "    implementation \"org.teavm:teavm-jso-apis:${teavmVersion}\"\n" +
            "    implementation \"org.teavm:teavm-classlib:${teavmVersion}\"\n" +
            "    implementation \"org.teavm:teavm-tooling:${teavmVersion}\"\n" +
            "}\n\n" +
            "sourceSets {\n" +
            "    main {\n" +
            "        java {\n" +
            "            srcDirs = ['src/main/java', 'src/teavm/java']\n" +
            "        }\n" +
            "        resources {\n" +
            "            srcDirs = ['resources']\n" +
            "        }\n" +
            "    }\n" +
            "}\n\n" +
            "java {\n" +
            "    sourceCompatibility = JavaVersion.VERSION_25\n" +
            "    targetCompatibility = JavaVersion.VERSION_25\n" +
            "}\n";
    }

    /**
     * Create Eclipse project files
     */
    private void createEclipseProject(File baseDir) throws BuildException {
        File wsDir = new File(baseDir, DIR_WORKSPACE);

        // .project
        try {
            String projectXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<projectDescription>\n" +
                "  <name>eaglercraft-" + MC_VERSION + "</name>\n" +
                "  <comment>EaglerCraft " + MC_VERSION + " Workspace</comment>\n" +
                "  <projects></projects>\n" +
                "  <buildSpec>\n" +
                "    <buildCommand>\n" +
                "      <name>org.eclipse.jdt.core.javabuilder</name>\n" +
                "    </buildCommand>\n" +
                "  </buildSpec>\n" +
                "  <natures>\n" +
                "    <nature>org.eclipse.jdt.core.javanature</nature>\n" +
                "  </natures>\n" +
                "</projectDescription>\n";
            Files.write(new File(wsDir, ".project").toPath(), projectXml.getBytes());
        } catch (IOException e) {
            System.out.println("[Workspace] Warning: Could not create Eclipse .project");
        }

        // .classpath
        try {
            String classpathXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<classpath>\n" +
                "  <classpathentry kind=\"src\" path=\"src/main/java\"/>\n" +
                "  <classpathentry kind=\"src\" path=\"src/teavm/java\"/>\n" +
                "  <classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n" +
                "  <classpathentry kind=\"lib\" path=\"lib/teavm/\"/>\n" +
                "  <classpathentry kind=\"output\" path=\"bin\"/>\n" +
                "</classpath>\n";
            Files.write(new File(wsDir, ".classpath").toPath(), classpathXml.getBytes());
        } catch (IOException e) {
            System.out.println("[Workspace] Warning: Could not create Eclipse .classpath");
        }

        System.out.println("[Workspace] Created Eclipse project files.");
    }

    /**
     * Create IntelliJ IDEA project files
     */
    private void createIntelliJProject(File baseDir) throws BuildException {
        File wsDir = new File(baseDir, DIR_WORKSPACE);

        // .idea directory
        File ideaDir = new File(wsDir, ".idea");
        if (!ideaDir.exists() && !ideaDir.mkdirs()) {
            System.out.println("[Workspace] Warning: Could not create .idea directory");
            return;
        }

        // eaglercraft.iml
        try {
            String imlXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<module type=\"JAVA_MODULE\" version=\"4\">\n" +
                "  <component name=\"NewModuleRootManager\">\n" +
                "    <content url=\"file://$MODULE_DIR$\">\n" +
                "      <sourceFolder url=\"file://$MODULE_DIR$/src/main/java\" isTestSource=\"false\" />\n" +
                "      <sourceFolder url=\"file://$MODULE_DIR$/src/teavm/java\" isTestSource=\"false\" />\n" +
                "      <sourceFolder url=\"file://$MODULE_DIR$/resources\" type=\"java-resource\" />\n" +
                "      <excludeFolder url=\"file://$MODULE_DIR$/bin\" />\n" +
                "      <excludeFolder url=\"file://$MODULE_DIR$/.teavm\" />\n" +
                "    </content>\n" +
                "    <orderEntry type=\"inheritedJdk\" />\n" +
                "    <orderEntry type=\"sourceFolder\" forTests=\"false\" />\n" +
                "  </component>\n" +
                "</module>\n";
            Files.write(new File(wsDir, "eaglercraft.iml").toPath(), imlXml.getBytes());
        } catch (IOException e) {
            System.out.println("[Workspace] Warning: Could not create IntelliJ module file");
        }

        System.out.println("[Workspace] Created IntelliJ project files.");
    }

    /**
     * Create VS Code configuration
     */
    private void createVSCodeConfig(File baseDir) throws BuildException {
        File wsDir = new File(baseDir, DIR_WORKSPACE);
        File vscodeDir = new File(wsDir, ".vscode");

        if (!vscodeDir.exists() && !vscodeDir.mkdirs()) {
            System.out.println("[Workspace] Warning: Could not create .vscode directory");
            return;
        }

        // settings.json
        try {
            String settingsJson = "{\n" +
                "  \"java.project.sourcePaths\": [\"src/main/java\", \"src/teavm/java\"],\n" +
                "  \"java.project.resourcePaths\": [\"resources\"],\n" +
                "  \"java.configuration.runtimes\": [\n" +
                "    {\n" +
                "      \"name\": \"JavaSE-25\",\n" +
                "      \"path\": \"" + System.getProperty("java.home", "").replace("\\", "\\\\") + "\",\n" +
                "      \"default\": true\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
            Files.write(new File(vscodeDir, "settings.json").toPath(), settingsJson.getBytes());
        } catch (IOException e) {
            System.out.println("[Workspace] Warning: Could not create VS Code settings");
        }

        // launch.json for debugging
        try {
            String launchJson = "{\n" +
                "  \"version\": \"0.2.0\",\n" +
                "  \"configurations\": [\n" +
                "    {\n" +
                "      \"type\": \"java\",\n" +
                "      \"name\": \"EaglerCraft Build\",\n" +
                "      \"request\": \"launch\",\n" +
                "      \"mainClass\": \"net.lax1dude.eaglercraft.v2_6.EaglerBuildTools\",\n" +
                "      \"args\": \"compile\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
            Files.write(new File(vscodeDir, "launch.json").toPath(), launchJson.getBytes());
        } catch (IOException e) {
            System.out.println("[Workspace] Warning: Could not create VS Code launch config");
        }

        System.out.println("[Workspace] Created VS Code configuration.");
    }

    /**
     * Verify TeaVM dependencies are available
     */
    private void verifyTeaVMDependencies(File baseDir) {
        File libDir = new File(baseDir, DIR_WS_LIB);
        File teavmLibDir = new File(libDir, "teavm");

        if (teavmLibDir.exists() && teavmLibDir.isDirectory()) {
            File[] jars = teavmLibDir.listFiles((dir, name) -> name.startsWith("teavm") && name.endsWith(".jar"));
            if (jars != null && jars.length > 0) {
                System.out.println("[Workspace] TeaVM dependencies found (" + jars.length + " JARs).");
                return;
            }
        }

        System.out.println("[Workspace] TeaVM dependencies not found in lib/teavm/.");
        System.out.println("[Workspace] They will be downloaded during compilation via Gradle.");
        System.out.println("[Workspace] TeaVM version: " + TEAVM_VERSION);
    }
}
