package net.lax1dude.eaglercraft.v2_6.internal;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;

/**
 * Platform application lifecycle implementation for EaglerCraft 26.1.2 on the TeaVM/browser platform.
 * Handles application-level events: beforeunload, clipboard, fullscreen, resize, errors, version.
 *
 * <p>Key 26.1.2 improvements:</p>
 * <ul>
 *   <li>BeforeUnload event for save prompts when leaving the page</li>
 *   <li>Async Clipboard API (navigator.clipboard) with fallback</li>
 *   <li>Fullscreen API with vendor prefix handling</li>
 *   <li>Window resize handling with device pixel ratio awareness</li>
 *   <li>Global error reporting with stack trace capture</li>
 *   <li>Version checking against the server</li>
 *   <li>Page title and favicon control</li>
 *   <li>Keyboard shortcut interception</li>
 * </ul>
 */
public class PlatformApplication {

	private static final Window window = Window.current();
	private static final HTMLDocument document = HTMLDocument.current();

	/** Whether the application has been initialized. */
	private static boolean initialized = false;

	/** Whether the user has unsaved changes (for beforeunload prompt). */
	private static boolean hasUnsavedChanges = false;

	/** Whether the app is currently in fullscreen mode. */
	private static boolean fullscreenActive = false;

	/** The application version string. */
	private static final String VERSION = "26.1.2";

	/** The application brand string. */
	private static final String BRAND = "EaglerCraftX";

	// ========== Callback Interfaces ==========

	@JSFunctor
	public interface BeforeUnloadCallback extends JSObject {
		String call();
	}

	@JSFunctor
	public interface ErrorCallback extends JSObject {
		void call(String message, String source, int lineno, int colno, String stack);
	}

	@JSFunctor
	public interface FullscreenChangeCallback extends JSObject {
		void call(boolean isFullscreen);
	}

	@JSFunctor
	public interface ResizeCallback extends JSObject {
		void call(int width, int height);
	}

	// ========== Registered Callbacks ==========

	private static BeforeUnloadCallback beforeUnloadHandler = null;
	private static ErrorCallback errorHandler = null;
	private static FullscreenChangeCallback fullscreenChangeHandler = null;
	private static ResizeCallback resizeHandler = null;

	/**
	 * Initializes the application platform. Called by ClientMain during startup.
	 */
	public static void _init() {
		if (initialized) return;

		// Register beforeunload handler for save prompts
		registerBeforeUnloadListener();

		// Register global error handler
		registerErrorListener();

		// Register fullscreen change listener
		registerFullscreenChangeListener();

		// Register resize listener
		registerResizeListener();

		// Set page title
		setPageTitle(BRAND + " " + VERSION);

		initialized = true;
		ClientMain.log("[PlatformApplication] Application lifecycle initialized (v" + VERSION + ")");
	}

	// ========== BeforeUnload ==========

	/**
	 * Registers a beforeunload handler to warn the user about unsaved changes.
	 */
	private static void registerBeforeUnloadListener() {
		window.addEventListener("beforeunload", (EventListener<Event>) event -> {
			if (hasUnsavedChanges) {
				setBeforeUnloadMessage(event, "You have unsaved changes. Are you sure you want to leave?");
			}
			if (beforeUnloadHandler != null) {
				String msg = beforeUnloadHandler.call();
				if (msg != null) {
					setBeforeUnloadMessage(event, msg);
				}
			}
		});
	}

	/**
	 * Sets whether there are unsaved changes that should trigger a beforeunload prompt.
	 */
	public static void setHasUnsavedChanges(boolean hasChanges) {
		hasUnsavedChanges = hasChanges;
	}

	/**
	 * Sets a custom beforeunload handler.
	 */
	public static void setBeforeUnloadHandler(BeforeUnloadCallback handler) {
		beforeUnloadHandler = handler;
	}

	// ========== Error Reporting ==========

	/**
	 * Registers a global error handler to capture JavaScript errors.
	 */
	private static void registerErrorListener() {
		window.addEventListener("error", (EventListener<Event>) event -> {
			String message = getErrorMessage(event);
			String source = getErrorSource(event);
			int lineno = getErrorLineNo(event);
			int colno = getErrorColNo(event);
			String stack = getErrorStack(event);

			ClientMain.error("[PlatformApplication] Uncaught error: " + message
					+ " at " + source + ":" + lineno + ":" + colno);
			if (stack != null && !stack.isEmpty()) {
				ClientMain.error("[PlatformApplication] Stack: " + stack);
			}

			if (errorHandler != null) {
				errorHandler.call(message, source, lineno, colno, stack);
			}
		});

		window.addEventListener("unhandledrejection", (EventListener<Event>) event -> {
			String reason = getPromiseRejectionReason(event);
			ClientMain.error("[PlatformApplication] Unhandled promise rejection: " + reason);
		});
	}

	/**
	 * Sets a custom error handler.
	 */
	public static void setErrorHandler(ErrorCallback handler) {
		errorHandler = handler;
	}

	// ========== Fullscreen ==========

	/**
	 * Registers a fullscreen change listener.
	 */
	private static void registerFullscreenChangeListener() {
		documentAddFullscreenChangeListener((event) -> {
			fullscreenActive = isFullscreenActive0();
			if (fullscreenChangeHandler != null) {
				fullscreenChangeHandler.call(fullscreenActive);
			}
		});
	}

	/**
	 * Enters fullscreen mode.
	 *
	 * @return true if the fullscreen request was made
	 */
	public static boolean enterFullscreen() {
		if (!ClientMain.fullscreenSupported) return false;

		HTMLCanvasElement canvas = ClientMain.getCanvas();
		if (canvas == null) return false;

		requestFullscreen0(canvas);
		return true;
	}

	/**
	 * Exits fullscreen mode.
	 */
	public static void exitFullscreen() {
		exitFullscreen0();
	}

	/**
	 * Toggles fullscreen mode.
	 */
	public static void toggleFullscreen() {
		if (fullscreenActive) {
			exitFullscreen();
		} else {
			enterFullscreen();
		}
	}

	/**
	 * Checks if the app is currently in fullscreen mode.
	 */
	public static boolean isFullscreen() {
		return fullscreenActive;
	}

	/**
	 * Sets a fullscreen change handler.
	 */
	public static void setFullscreenChangeHandler(FullscreenChangeCallback handler) {
		fullscreenChangeHandler = handler;
	}

	// ========== Clipboard ==========

	/**
	 * Copies text to the clipboard.
	 *
	 * @param text The text to copy
	 */
	public static void copyToClipboard(String text) {
		PlatformInput.setClipboardText(text);
	}

	/**
	 * Reads text from the clipboard (async).
	 *
	 * @return The clipboard text, or null if unavailable
	 */
	public static String readClipboard() {
		return PlatformInput.getClipboardText();
	}

	// ========== Window / Page ==========

	/**
	 * Sets the page title.
	 */
	public static void setPageTitle(String title) {
		setDocumentTitle0(title);
	}

	/**
	 * Gets the page title.
	 */
	public static String getPageTitle() {
		return getDocumentTitle0();
	}

	/**
	 * Sets the page favicon.
	 *
	 * @param dataUrl The favicon as a data URL
	 */
	public static void setFavicon(String dataUrl) {
		setFavicon0(dataUrl);
	}

	/**
	 * Registers a window resize handler.
	 */
	private static void registerResizeListener() {
		window.addEventListener("resize", (EventListener<Event>) event -> {
			if (resizeHandler != null) {
				resizeHandler.call(PlatformRuntime.getCanvasWidth(), PlatformRuntime.getCanvasHeight());
			}
		});
	}

	/**
	 * Sets a resize handler.
	 */
	public static void setResizeHandler(ResizeCallback handler) {
		resizeHandler = handler;
	}

	/**
	 * Gets the window inner width.
	 */
	public static int getWindowWidth() {
		return getWindowInnerWidth0();
	}

	/**
	 * Gets the window inner height.
	 */
	public static int getWindowHeight() {
		return getWindowInnerHeight0();
	}

	// ========== Version ==========

	/**
	 * Gets the application version string.
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * Gets the application brand string.
	 */
	public static String getBrand() {
		return BRAND;
	}

	/**
	 * Gets the full application identifier (brand + version).
	 */
	public static String getFullIdentifier() {
		return BRAND + "/" + VERSION;
	}

	/**
	 * Checks if the client version matches a required version.
	 *
	 * @param requiredVersion The minimum required version string
	 * @return true if this client meets the version requirement
	 */
	public static boolean checkVersion(String requiredVersion) {
		return compareVersions(VERSION, requiredVersion) >= 0;
	}

	/**
	 * Compares two version strings.
	 *
	 * @return negative if v1 < v2, 0 if equal, positive if v1 > v2
	 */
	private static int compareVersions(String v1, String v2) {
		String[] parts1 = v1.split("\\.");
		String[] parts2 = v2.split("\\.");
		int len = Math.max(parts1.length, parts2.length);
		for (int i = 0; i < len; i++) {
			int p1 = i < parts1.length ? parseVersionPart(parts1[i]) : 0;
			int p2 = i < parts2.length ? parseVersionPart(parts2[i]) : 0;
			if (p1 != p2) return p1 - p2;
		}
		return 0;
	}

	private static int parseVersionPart(String part) {
		try {
			return Integer.parseInt(part);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	// ========== Debug ==========

	/**
	 * Opens the browser debugger (if available).
	 */
	public static void openDebugger() {
		debugger0();
	}

	/**
	 * Gets performance statistics.
	 *
	 * @return A formatted string with performance info
	 */
	public static String getPerformanceStats() {
		StringBuilder sb = new StringBuilder();
		sb.append("Version: ").append(getFullIdentifier()).append("\n");
		sb.append("FPS: ").append(String.format("%.1f", PlatformRuntime.getFPS())).append("\n");
		sb.append("Canvas: ").append(PlatformRuntime.getCanvasWidth()).append("x")
				.append(PlatformRuntime.getCanvasHeight()).append("\n");
		sb.append("DPR: ").append(PlatformRuntime.getDevicePixelRatio()).append("\n");
		sb.append("Audio: ").append(PlatformAudio.isAvailable() ? "OK" : "N/A").append("\n");

		if (PlatformNetworking.isConnected()) {
			sb.append("Network: Connected (").append(PlatformNetworking.getLatency()).append("ms)\n");
		} else {
			sb.append("Network: Disconnected\n");
		}

		long[] mem = PlatformRuntime.getMemoryInfo();
		if (mem != null) {
			sb.append("Memory: ").append(mem[0] / 1024 / 1024).append("MB / ")
					.append(mem[1] / 1024 / 1024).append("MB\n");
		}

		sb.append("User Agent: ").append(PlatformRuntime.getUserAgent()).append("\n");

		return sb.toString();
	}

	// ========== Native JS Methods ==========

	@JSBody(params = { "event", "message" }, script = ""
			+ "event.preventDefault();"
			+ "event.returnValue = message;"
			+ "return message;")
	private static native void setBeforeUnloadMessage(Event event, String message);

	@JSBody(params = { "event" }, script = "return event.message || '';")
	private static native String getErrorMessage(Event event);

	@JSBody(params = { "event" }, script = "return event.filename || '';")
	private static native String getErrorSource(Event event);

	@JSBody(params = { "event" }, script = "return event.lineno || 0;")
	private static native int getErrorLineNo(Event event);

	@JSBody(params = { "event" }, script = "return event.colno || 0;")
	private static native int getErrorColNo(Event event);

	@JSBody(params = { "event" }, script = "return event.error ? (event.error.stack || '') : '';")
	private static native String getErrorStack(Event event);

	@JSBody(params = { "event" }, script = "return String(event.reason || 'Unknown');")
	private static native String getPromiseRejectionReason(Event event);

	@JSBody(params = { "callback" }, script = ""
			+ "document.addEventListener('fullscreenchange', callback);"
			+ "document.addEventListener('webkitfullscreenchange', callback);")
	private static native void documentAddFullscreenChangeListener(EventListener<Event> callback);

	@JSBody(params = { "element" }, script = ""
			+ "if (element.requestFullscreen) element.requestFullscreen();"
			+ "else if (element.webkitRequestFullscreen) element.webkitRequestFullscreen();"
			+ "else if (element.msRequestFullscreen) element.msRequestFullscreen();")
	private static native void requestFullscreen0(HTMLElement element);

	@JSBody(params = {}, script = ""
			+ "if (document.exitFullscreen) document.exitFullscreen();"
			+ "else if (document.webkitExitFullscreen) document.webkitExitFullscreen();"
			+ "else if (document.msExitFullscreen) document.msExitFullscreen();")
	private static native void exitFullscreen0();

	@JSBody(params = {}, script = ""
			+ "return !!(document.fullscreenElement || document.webkitFullscreenElement || document.msFullscreenElement);")
	private static native boolean isFullscreenActive0();

	@JSBody(params = { "title" }, script = "document.title = title;")
	private static native void setDocumentTitle0(String title);

	@JSBody(params = {}, script = "return document.title;")
	private static native String getDocumentTitle0();

	@JSBody(params = { "dataUrl" }, script = ""
			+ "var link = document.querySelector('link[rel=\"icon\"]') || document.createElement('link');"
			+ "link.rel = 'icon';"
			+ "link.href = dataUrl;"
			+ "document.head.appendChild(link);")
	private static native void setFavicon0(String dataUrl);

	@JSBody(params = {}, script = "return window.innerWidth;")
	private static native int getWindowInnerWidth0();

	@JSBody(params = {}, script = "return window.innerHeight;")
	private static native int getWindowInnerHeight0();

	@JSBody(params = {}, script = "debugger;")
	private static native void debugger0();
}
