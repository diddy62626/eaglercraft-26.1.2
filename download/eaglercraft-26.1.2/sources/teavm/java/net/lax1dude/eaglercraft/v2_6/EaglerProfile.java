package net.lax1dude.eaglercraft.v2_6;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;

/**
 * Player profile and settings manager for EaglerCraft 26.1.2.
 * Stores and persists the player's username, skin selection, render distance,
 * audio preferences, and other client-side configuration through localStorage
 * via {@link PlatformRuntime}.
 *
 * <p>This class bridges Minecraft's options system with the browser's
 * localStorage. MC 26.1.2's options are normally stored in <code>options.txt</code>,
 * but in EaglerCraft we persist them using namespaced localStorage keys.</p>
 *
 * <h3>Storage layout:</h3>
 * <pre>
 *   eaglercraft.profile.username     - Player username
 *   eaglercraft.profile.skinModel    - Skin model ID (0=Steve, 1=Alex)
 *   eaglercraft.profile.skinURL      - Custom skin URL (or null for default)
 *   eaglercraft.profile.renderDist   - Render distance in chunks
 *   eaglercraft.profile.fov          - Field of view
 *   eaglercraft.profile.masterVol    - Master volume (0.0-1.0)
 *   eaglercraft.profile.musicVol     - Music volume (0.0-1.0)
 *   eaglercraft.profile.sfxVol       - SFX volume (0.0-1.0)
 *   eaglercraft.profile.ambientVol   - Ambient volume (0.0-1.0)
 *   eaglercraft.profile.fancyGfx     - Fancy graphics enabled
 *   eaglercraft.profile.smoothLt     - Smooth lighting enabled
 *   eaglercraft.profile.vsync        - VSync enabled
 *   eaglercraft.profile.lang         - Language code (e.g., "en_US")
 *   eaglercraft.profile.showFPS      - Show FPS in title bar
 *   eaglercraft.profile.heldItemTooltips - Held item tooltips
 * </pre>
 *
 * <p>Architecture note: This class is in the <code>main</code> source set.
 * It uses PlatformRuntime from the <code>teavm</code> source set for storage,
 * and provides settings that MC code will query through the adapter layer.</p>
 */
public class EaglerProfile {

	// ========== Profile Fields ==========

	/** The player's display username. */
	private static String username = "Player" + (int)(Math.random() * 1000);

	/** The player's skin model ID. 0 = Steve (classic), 1 = Alex (slim). */
	private static int skinModel = EaglerCraftConfig.DEFAULT_SKIN_MODEL;

	/** Custom skin URL, or null to use the default skin. */
	private static String skinURL = null;

	/** Custom skin pixel data (64x64 RGBA), or null for default. */
	private static byte[] skinData = null;

	/** Render distance in chunks. */
	private static int renderDistance = EaglerCraftConfig.DEFAULT_RENDER_DISTANCE;

	/** Field of view in degrees. */
	private static int fov = EaglerCraftConfig.DEFAULT_FOV;

	/** Master volume level (0.0 to 1.0). */
	private static float masterVolume = EaglerCraftConfig.DEFAULT_MASTER_VOLUME;

	/** Music volume level (0.0 to 1.0). */
	private static float musicVolume = EaglerCraftConfig.DEFAULT_MUSIC_VOLUME;

	/** Sound effects volume level (0.0 to 1.0). */
	private static float sfxVolume = EaglerCraftConfig.DEFAULT_SFX_VOLUME;

	/** Ambient sounds volume level (0.0 to 1.0). */
	private static float ambientVolume = EaglerCraftConfig.DEFAULT_AMBIENT_VOLUME;

	/** Whether fancy graphics mode is enabled. */
	private static boolean fancyGraphics = EaglerCraftConfig.DEFAULT_FANCY_GRAPHICS;

	/** Whether smooth lighting is enabled. */
	private static boolean smoothLighting = EaglerCraftConfig.DEFAULT_SMOOTH_LIGHTING;

	/** Whether VSync is enabled. */
	private static boolean vsync = EaglerCraftConfig.DEFAULT_VSYNC;

	/** Selected language code. */
	private static String lang = "en_US";

	/** Whether to show FPS in the window title. */
	private static boolean showFPS = false;

	/** Whether to show held item tooltips. */
	private static boolean heldItemTooltips = true;

	/** Mipmap levels for textures (0-4). */
	private static int mipmapLevels = EaglerCraftConfig.DEFAULT_MIPMAP_LEVELS;

	/** Whether the profile has been loaded from storage. */
	private static boolean loaded = false;

	// ========== localStorage Key Helpers ==========

	private static final String KEY_PREFIX = "profile.";

	private static String key(String suffix) {
		return KEY_PREFIX + suffix;
	}

	// ========== Load / Save ==========

	/**
	 * Loads the player profile from localStorage.
	 * Called during EaglerCraft initialization before the game starts.
	 * If no saved profile exists, defaults are used.
	 */
	public static void load() {
		if (loaded) return;

		try {
			String val;

			val = PlatformRuntime.localStorageGet(key("username"));
			if (val != null) username = val;

			val = PlatformRuntime.localStorageGet(key("skinModel"));
			if (val != null) {
				skinModel = Integer.parseInt(val);
				skinModel = Math.max(0, Math.min(1, skinModel)); // Clamp to valid range
			}

			val = PlatformRuntime.localStorageGet(key("skinURL"));
			if (val != null && !val.isEmpty()) skinURL = val;

			val = PlatformRuntime.localStorageGet(key("renderDist"));
			if (val != null) {
				renderDistance = Integer.parseInt(val);
				renderDistance = clamp(renderDistance,
						EaglerCraftConfig.MIN_RENDER_DISTANCE,
						EaglerCraftConfig.MAX_RENDER_DISTANCE);
			}

			val = PlatformRuntime.localStorageGet(key("fov"));
			if (val != null) {
				fov = Integer.parseInt(val);
				fov = Math.max(30, Math.min(110, fov)); // Clamp FOV to reasonable range
			}

			val = PlatformRuntime.localStorageGet(key("masterVol"));
			if (val != null) masterVolume = clampFloat(Float.parseFloat(val), 0.0f, 1.0f);

			val = PlatformRuntime.localStorageGet(key("musicVol"));
			if (val != null) musicVolume = clampFloat(Float.parseFloat(val), 0.0f, 1.0f);

			val = PlatformRuntime.localStorageGet(key("sfxVol"));
			if (val != null) sfxVolume = clampFloat(Float.parseFloat(val), 0.0f, 1.0f);

			val = PlatformRuntime.localStorageGet(key("ambientVol"));
			if (val != null) ambientVolume = clampFloat(Float.parseFloat(val), 0.0f, 1.0f);

			val = PlatformRuntime.localStorageGet(key("fancyGfx"));
			if (val != null) fancyGraphics = Boolean.parseBoolean(val);

			val = PlatformRuntime.localStorageGet(key("smoothLt"));
			if (val != null) smoothLighting = Boolean.parseBoolean(val);

			val = PlatformRuntime.localStorageGet(key("vsync"));
			if (val != null) vsync = Boolean.parseBoolean(val);

			val = PlatformRuntime.localStorageGet(key("lang"));
			if (val != null && !val.isEmpty()) lang = val;

			val = PlatformRuntime.localStorageGet(key("showFPS"));
			if (val != null) showFPS = Boolean.parseBoolean(val);

			val = PlatformRuntime.localStorageGet(key("heldItemTooltips"));
			if (val != null) heldItemTooltips = Boolean.parseBoolean(val);

			val = PlatformRuntime.localStorageGet(key("mipmapLevels"));
			if (val != null) {
				mipmapLevels = Integer.parseInt(val);
				mipmapLevels = Math.max(0, Math.min(4, mipmapLevels));
			}

			loaded = true;

			if (EaglerCraftConfig.VERBOSE_INIT_LOGGING) {
				ClientMain.log("[EaglerProfile] Profile loaded: " + username
						+ " (skin=" + skinModel + ", renderDist=" + renderDistance
						+ ", fov=" + fov + ")");
			}

		} catch (NumberFormatException e) {
			ClientMain.warn("[EaglerProfile] Corrupted profile data, using defaults: " + e.getMessage());
		} catch (Exception e) {
			ClientMain.warn("[EaglerProfile] Failed to load profile: " + e.getMessage());
		}
	}

	/**
	 * Saves the current profile to localStorage.
	 * Should be called whenever settings change (e.g., from the options menu).
	 */
	public static void save() {
		try {
			PlatformRuntime.localStorageSet(key("username"), username);
			PlatformRuntime.localStorageSet(key("skinModel"), String.valueOf(skinModel));
			PlatformRuntime.localStorageSet(key("skinURL"), skinURL != null ? skinURL : "");
			PlatformRuntime.localStorageSet(key("renderDist"), String.valueOf(renderDistance));
			PlatformRuntime.localStorageSet(key("fov"), String.valueOf(fov));
			PlatformRuntime.localStorageSet(key("masterVol"), String.valueOf(masterVolume));
			PlatformRuntime.localStorageSet(key("musicVol"), String.valueOf(musicVolume));
			PlatformRuntime.localStorageSet(key("sfxVol"), String.valueOf(sfxVolume));
			PlatformRuntime.localStorageSet(key("ambientVol"), String.valueOf(ambientVolume));
			PlatformRuntime.localStorageSet(key("fancyGfx"), String.valueOf(fancyGraphics));
			PlatformRuntime.localStorageSet(key("smoothLt"), String.valueOf(smoothLighting));
			PlatformRuntime.localStorageSet(key("vsync"), String.valueOf(vsync));
			PlatformRuntime.localStorageSet(key("lang"), lang);
			PlatformRuntime.localStorageSet(key("showFPS"), String.valueOf(showFPS));
			PlatformRuntime.localStorageSet(key("heldItemTooltips"), String.valueOf(heldItemTooltips));
			PlatformRuntime.localStorageSet(key("mipmapLevels"), String.valueOf(mipmapLevels));
		} catch (Exception e) {
			ClientMain.warn("[EaglerProfile] Failed to save profile: " + e.getMessage());
		}
	}

	// ========== Getters and Setters ==========

	/**
	 * Gets the player's username.
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * Sets the player's username and persists it.
	 * Username is limited to 16 characters and alphanumeric + underscore.
	 *
	 * @param name The new username (will be sanitized)
	 */
	public static void setUsername(String name) {
		if (name == null || name.isEmpty()) {
			name = "Player";
		}
		// Sanitize: limit length and allowed characters
		name = name.replaceAll("[^A-Za-z0-9_]", "_");
		if (name.length() > 16) {
			name = name.substring(0, 16);
		}
		username = name;
		PlatformRuntime.localStorageSet(key("username"), username);
	}

	/**
	 * Gets the skin model ID.
	 * @return 0 for Steve (classic), 1 for Alex (slim)
	 */
	public static int getSkinModel() {
		return skinModel;
	}

	/**
	 * Sets the skin model.
	 * @param model 0 for Steve (classic), 1 for Alex (slim)
	 */
	public static void setSkinModel(int model) {
		skinModel = Math.max(0, Math.min(1, model));
		PlatformRuntime.localStorageSet(key("skinModel"), String.valueOf(skinModel));
	}

	/**
	 * Gets the custom skin URL, or null for default skin.
	 */
	public static String getSkinURL() {
		return skinURL;
	}

	/**
	 * Sets the custom skin URL.
	 * @param url The skin image URL, or null to use the default skin
	 */
	public static void setSkinURL(String url) {
		skinURL = (url != null && !url.isEmpty()) ? url : null;
		PlatformRuntime.localStorageSet(key("skinURL"), skinURL != null ? skinURL : "");
	}

	/**
	 * Gets the custom skin pixel data (64x64 RGBA), or null for default.
	 */
	public static byte[] getSkinData() {
		return skinData;
	}

	/**
	 * Sets the custom skin pixel data.
	 * @param data RGBA pixel data (must be 64*64*4 = 16384 bytes), or null for default
	 */
	public static void setSkinData(byte[] data) {
		if (data != null && data.length != EaglerCraftConfig.SKIN_WIDTH
				* EaglerCraftConfig.SKIN_HEIGHT * 4) {
			ClientMain.warn("[EaglerProfile] Invalid skin data size: " + data.length
					+ " (expected " + (EaglerCraftConfig.SKIN_WIDTH
					* EaglerCraftConfig.SKIN_HEIGHT * 4) + ")");
			return;
		}
		skinData = data;
		// Skin data is not persisted to localStorage (too large);
		// it should be loaded from a URL or the EPK on startup
	}

	/**
	 * Gets the render distance in chunks.
	 */
	public static int getRenderDistance() {
		return renderDistance;
	}

	/**
	 * Sets the render distance in chunks.
	 * Clamped to [{@link EaglerCraftConfig#MIN_RENDER_DISTANCE},
	 * {@link EaglerCraftConfig#MAX_RENDER_DISTANCE}].
	 */
	public static void setRenderDistance(int distance) {
		renderDistance = clamp(distance,
				EaglerCraftConfig.MIN_RENDER_DISTANCE,
				EaglerCraftConfig.MAX_RENDER_DISTANCE);
		PlatformRuntime.localStorageSet(key("renderDist"), String.valueOf(renderDistance));
	}

	/**
	 * Gets the field of view in degrees.
	 */
	public static int getFOV() {
		return fov;
	}

	/**
	 * Sets the field of view in degrees (clamped to 30-110).
	 */
	public static void setFOV(int fieldOfView) {
		fov = Math.max(30, Math.min(110, fieldOfView));
		PlatformRuntime.localStorageSet(key("fov"), String.valueOf(fov));
	}

	/**
	 * Gets the master volume level.
	 */
	public static float getMasterVolume() {
		return masterVolume;
	}

	/**
	 * Sets the master volume level (0.0 to 1.0).
	 */
	public static void setMasterVolume(float volume) {
		masterVolume = clampFloat(volume, 0.0f, 1.0f);
		PlatformRuntime.localStorageSet(key("masterVol"), String.valueOf(masterVolume));
	}

	/**
	 * Gets the music volume level.
	 */
	public static float getMusicVolume() {
		return musicVolume;
	}

	/**
	 * Sets the music volume level (0.0 to 1.0).
	 */
	public static void setMusicVolume(float volume) {
		musicVolume = clampFloat(volume, 0.0f, 1.0f);
		PlatformRuntime.localStorageSet(key("musicVol"), String.valueOf(musicVolume));
	}

	/**
	 * Gets the sound effects volume level.
	 */
	public static float getSFXVolume() {
		return sfxVolume;
	}

	/**
	 * Sets the sound effects volume level (0.0 to 1.0).
	 */
	public static void setSFXVolume(float volume) {
		sfxVolume = clampFloat(volume, 0.0f, 1.0f);
		PlatformRuntime.localStorageSet(key("sfxVol"), String.valueOf(sfxVolume));
	}

	/**
	 * Gets the ambient sounds volume level.
	 */
	public static float getAmbientVolume() {
		return ambientVolume;
	}

	/**
	 * Sets the ambient sounds volume level (0.0 to 1.0).
	 */
	public static void setAmbientVolume(float volume) {
		ambientVolume = clampFloat(volume, 0.0f, 1.0f);
		PlatformRuntime.localStorageSet(key("ambientVol"), String.valueOf(ambientVolume));
	}

	/**
	 * Checks if fancy graphics mode is enabled.
	 */
	public static boolean isFancyGraphics() {
		return fancyGraphics;
	}

	/**
	 * Sets fancy graphics mode.
	 */
	public static void setFancyGraphics(boolean enabled) {
		fancyGraphics = enabled;
		PlatformRuntime.localStorageSet(key("fancyGfx"), String.valueOf(fancyGraphics));
	}

	/**
	 * Checks if smooth lighting is enabled.
	 */
	public static boolean isSmoothLighting() {
		return smoothLighting;
	}

	/**
	 * Sets smooth lighting.
	 */
	public static void setSmoothLighting(boolean enabled) {
		smoothLighting = enabled;
		PlatformRuntime.localStorageSet(key("smoothLt"), String.valueOf(smoothLighting));
	}

	/**
	 * Checks if VSync is enabled.
	 */
	public static boolean isVsync() {
		return vsync;
	}

	/**
	 * Sets VSync.
	 */
	public static void setVsync(boolean enabled) {
		vsync = enabled;
		PlatformRuntime.localStorageSet(key("vsync"), String.valueOf(vsync));
	}

	/**
	 * Gets the selected language code.
	 */
	public static String getLang() {
		return lang;
	}

	/**
	 * Sets the language code.
	 */
	public static void setLang(String language) {
		if (language != null && !language.isEmpty()) {
			lang = language;
			PlatformRuntime.localStorageSet(key("lang"), lang);
		}
	}

	/**
	 * Checks if FPS display is enabled in the title bar.
	 */
	public static boolean isShowFPS() {
		return showFPS;
	}

	/**
	 * Sets whether to show FPS in the title bar.
	 */
	public static void setShowFPS(boolean show) {
		showFPS = show;
		PlatformRuntime.localStorageSet(key("showFPS"), String.valueOf(showFPS));
	}

	/**
	 * Checks if held item tooltips are enabled.
	 */
	public static boolean isHeldItemTooltips() {
		return heldItemTooltips;
	}

	/**
	 * Sets whether held item tooltips are shown.
	 */
	public static void setHeldItemTooltips(boolean enabled) {
		heldItemTooltips = enabled;
		PlatformRuntime.localStorageSet(key("heldItemTooltips"), String.valueOf(heldItemTooltips));
	}

	/**
	 * Gets the mipmap levels for textures.
	 */
	public static int getMipmapLevels() {
		return mipmapLevels;
	}

	/**
	 * Sets the mipmap levels for textures (0-4).
	 */
	public static void setMipmapLevels(int levels) {
		mipmapLevels = Math.max(0, Math.min(4, levels));
		PlatformRuntime.localStorageSet(key("mipmapLevels"), String.valueOf(mipmapLevels));
	}

	// ========== Utility ==========

	private static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	private static float clampFloat(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
}
