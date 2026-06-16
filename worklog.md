---
Task ID: 1
Agent: main
Task: Port MC 26.1.2 classes to work through TeaVM in browser

Work Log:
- Downloaded MC 26.1.2 client JAR from Mojang's Piston API (38MB, 30,675 entries, 10,208 net.minecraft classes)
- Downloaded 27 MC dependency libraries (gson, guava, fastutil, log4j, netty, brigadier, datafixerupper, etc.)
- Installed JDK 25 (Temurin 25.0.3) for Java 25 class file compatibility
- Configured Gradle to use JDK 25 for compilation (Java 21 was insufficient for Java 25 class files)
- Removed conflicting NIO buffer stubs (TeaVM classlib provides these)
- Created browser-compatible SLF4J stubs (Logger, LoggerFactory, Marker, MarkerFactory, event.Level) to replace SLF4J JAR
- Created TeaVM classlib patches (StackWalker, CompletableFuture, Future, TimeoutException, CompletionException) compiled with --patch-module java.base
- Fixed LWJGL stub compilation errors (GL11 duplicate constants, GL15 Uint8Array.set type, GLFW parameter type)
- Configured build.gradle with all MC dependency libraries and teavm-patches.jar
- Updated CI/CD to auto-download MC JAR and libraries from Mojang API
- Successfully compiled TeaVM JavaScript: BUILD SUCCESSFUL
- Output: 293KB classes.js with 279 source files including 39 MC/Mojang classes
- Pushed to GitHub: diddy62626/eaglercraft-26.1.2

Stage Summary:
- TeaVM now successfully compiles MC 26.1.2 classes to JavaScript
- classes.js grew from 180KB (adapter-only) to 293KB (with MC class references)
- 39 MC/Mojang classes included so far (Minecraft, LogUtils, Identifier, Codec, Component, etc.)
- Full game (30K+ classes) not yet included - TeaVM only compiles reachable code from mainClass
- Next step: Create bootstrap class that directly references more MC game subsystems to pull them into the TeaVM call graph
