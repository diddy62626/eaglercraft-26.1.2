package net.lax1dude.eaglercraft.v2_6.internal.teavm;

/**
 * Entry point class for the EaglerCraft 26.1.2 TeaVM client.
 * The TeaVM compiler generates JavaScript that calls main() when the page loads.
 * This class simply delegates to ClientMain._main() which handles all initialization.
 *
 * <p>TeaVM uses this class as the main class configured in the build system.
 * The generated JavaScript bundle will automatically invoke main() on page load.</p>
 *
 * <p>Architecture notes for 26.1.2:</p>
 * <ul>
 *   <li>WebGL2 is required - no WebGL1 fallback</li>
 *   <li>All platform-specific code is in the Platform* classes</li>
 *   <li>Browser JS interop uses TeaVM's @JSBody and JSO APIs</li>
 *   <li>WebGPU can be added later through the same Platform interface</li>
 * </ul>
 */
public class MainClass {

	/**
	 * TeaVM entry point. Called by the generated JavaScript when the page loads.
	 * Delegates immediately to ClientMain._main() for all initialization logic.
	 *
	 * @param args Command-line arguments (unused in browser context)
	 */
	public static void main(String[] args) {
		ClientMain._main();
	}
}
