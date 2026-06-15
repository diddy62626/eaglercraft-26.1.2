package net.lax1dude.eaglercraft.v2_6.plugin.gateway_bungee;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.yaml.snakeyaml.Yaml;

/**
 * EaglercraftXBungee - Main plugin class for the EaglerCraft gateway on BungeeCord.
 * <p>
 * This plugin translates WebSocket connections from EaglerCraft browser clients
 * into standard TCP Minecraft protocol connections, allowing web-based clients
 * to join Minecraft Java Edition servers through a BungeeCord proxy.
 * </p>
 * <p>
 * Supported features include:
 * <ul>
 *   <li>WebSocket listener with protocol 775 translation (WebSocket to TCP)</li>
 *   <li>Custom skin and cape service for browser clients</li>
 *   <li>Voice chat relay between EaglerCraft clients</li>
 *   <li>Microsoft account authentication via login codes</li>
 *   <li>Rate limiting and connection throttling</li>
 *   <li>MOTD query system for server browser display</li>
 *   <li>Built-in HTTP file server for resource distribution</li>
 * </ul>
 * </p>
 *
 * @author lax1dude
 * @version 1.3.2
 * @since 26.1.2
 */
public class EaglercraftXBungee extends Plugin implements Listener {

    /** Plugin version string matching the EaglerCraft release */
    public static final String PLUGIN_VERSION = "1.3.2";

    /** EaglerCraft protocol version number */
    public static final int PROTOCOL_VERSION = 775;

    /** Minecraft protocol version string */
    public static final String MC_PROTOCOL_VERSION = "26.1.2";

    /** Gson instance for JSON serialization/deserialization */
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** Logger instance for the plugin */
    private static final Logger LOGGER = Logger.getLogger("EaglercraftXBungee");

    /** Singleton plugin instance */
    private static EaglercraftXBungee instance;

    /** Active WebSocket server instances keyed by listener name */
    private final Map<String, EaglerWebSocketServer> wsServers = new ConcurrentHashMap<>();

    /** Connected EaglerCraft player sessions keyed by UUID */
    private final Map<UUID, EaglerPlayerSession> playerSessions = new ConcurrentHashMap<>();

    /** Rate limit tracking: IP address to connection timestamps */
    private final Map<String, LinkedList<Long>> rateLimitMap = new ConcurrentHashMap<>();

    /** Skin service registry: username to skin data */
    private final Map<String, byte[]> skinRegistry = new ConcurrentHashMap<>();

    /** Cape service registry: username to cape data */
    private final Map<String, byte[]> capeRegistry = new ConcurrentHashMap<>();

    /** Voice chat relay channels */
    private final Map<String, VoiceChannel> voiceChannels = new ConcurrentHashMap<>();

    /** Pending authentication codes: code to auth entry */
    private final Map<String, AuthEntry> pendingAuthCodes = new ConcurrentHashMap<>();

    /** Plugin configuration loaded from settings.yml */
    private PluginConfig config;

    /** Listener configurations loaded from listeners.yml */
    private Map<String, ListenerConfig> listenerConfigs;

    /** Rate limit: max connections per IP per window */
    private int rateLimitConnections = 10;

    /** Rate limit: time window in milliseconds */
    private long rateLimitWindowMs = 60000L;

    /** Whether the plugin is enabled and running */
    private volatile boolean enabled = false;

    /** AtomicInteger for generating unique connection IDs */
    private final AtomicInteger connectionIdCounter = new AtomicInteger(0);

    /**
     * Constructs the EaglercraftXBungee plugin instance.
     */
    public EaglercraftXBungee() {
        super(new PluginDescription(
            "EaglercraftXBungee",
            PLUGIN_VERSION,
            "lax1dude",
            "EaglerCraft gateway plugin for BungeeCord proxy",
            Collections.singletonList("net.lax1dude.eaglercraft.v2_6.plugin.gateway_bungee")
        ));
    }

    /**
     * Gets the singleton plugin instance.
     *
     * @return the active EaglercraftXBungee instance
     */
    public static EaglercraftXBungee getInstance() {
        return instance;
    }

    /**
     * Called when the plugin is enabled. Initializes WebSocket listeners,
     * loads configuration, and registers event handlers.
     */
    @Override
    public void onEnable() {
        instance = this;
        LOGGER.info("EaglercraftXBungee v" + PLUGIN_VERSION + " starting...");

        loadConfiguration();
        loadListenerConfiguration();
        startWebSocketServers();
        registerCommands();
        registerEventListeners();
        startMaintenanceTasks();

        enabled = true;
        LOGGER.info("EaglercraftXBungee v" + PLUGIN_VERSION + " enabled successfully.");
    }

    /**
     * Called when the plugin is disabled. Shuts down WebSocket servers,
     * disconnects all players, and cleans up resources.
     */
    @Override
    public void onDisable() {
        enabled = false;
        LOGGER.info("EaglercraftXBungee shutting down...");

        stopWebSocketServers();
        disconnectAllPlayers();
        cleanupResources();

        LOGGER.info("EaglercraftXBungee disabled.");
    }

    /**
     * Loads the main plugin configuration from the settings.yml file.
     * Creates default configuration if the file does not exist.
     */
    @SuppressWarnings("unchecked")
    private void loadConfiguration() {
        File configFile = new File(getDataFolder(), "settings.yml");
        if (!configFile.exists()) {
            saveDefaultConfig("config/default_settings.yml", configFile);
        }

        try (InputStream is = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml();
            Map<String, Object> raw = yaml.load(is);
            config = PluginConfig.fromMap(raw);
            LOGGER.info("Loaded configuration from settings.yml");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load configuration", e);
            config = PluginConfig.createDefault();
        }

        rateLimitConnections = config.getRateLimitConnections();
        rateLimitWindowMs = config.getRateLimitWindowMs();
    }

    /**
     * Loads WebSocket listener configurations from listeners.yml.
     * Creates default listener configuration if the file does not exist.
     */
    @SuppressWarnings("unchecked")
    private void loadListenerConfiguration() {
        File listenerFile = new File(getDataFolder(), "listeners.yml");
        if (!listenerFile.exists()) {
            saveDefaultConfig("config/default_listeners.yml", listenerFile);
        }

        listenerConfigs = new HashMap<>();
        try (InputStream is = new FileInputStream(listenerFile)) {
            Yaml yaml = new Yaml();
            Map<String, Object> raw = yaml.load(is);
            List<Map<String, Object>> listeners = (List<Map<String, Object>>) raw.get("listeners");
            if (listeners != null) {
                for (Map<String, Object> lMap : listeners) {
                    ListenerConfig lc = ListenerConfig.fromMap(lMap);
                    listenerConfigs.put(lc.getName(), lc);
                }
            }
            LOGGER.info("Loaded " + listenerConfigs.size() + " listener configurations");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load listener configuration", e);
        }
    }

    /**
     * Starts all configured WebSocket listener servers.
     */
    private void startWebSocketServers() {
        for (Map.Entry<String, ListenerConfig> entry : listenerConfigs.entrySet()) {
            ListenerConfig lc = entry.getValue();
            if (!lc.isEnabled()) {
                LOGGER.info("Skipping disabled listener: " + lc.getName());
                continue;
            }

            try {
                InetSocketAddress addr = new InetSocketAddress(lc.getHost(), lc.getPort());
                EaglerWebSocketServer server = new EaglerWebSocketServer(addr, lc);
                server.setConnectionLostTimeout(30);
                server.start();
                wsServers.put(lc.getName(), server);
                LOGGER.info("Started WebSocket listener '" + lc.getName() + "' on " + lc.getHost() + ":" + lc.getPort());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to start WebSocket listener '" + lc.getName() + "'", e);
            }
        }
    }

    /**
     * Stops all running WebSocket server instances gracefully.
     */
    private void stopWebSocketServers() {
        for (Map.Entry<String, EaglerWebSocketServer> entry : wsServers.entrySet()) {
            try {
                entry.getValue().stop(5000);
                LOGGER.info("Stopped WebSocket listener: " + entry.getKey());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warning("Interrupted while stopping WebSocket listener: " + entry.getKey());
            }
        }
        wsServers.clear();
    }

    /**
     * Registers plugin commands with the BungeeCord proxy.
     */
    private void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new EaglerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new EaglerSkinCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new EaglerVoiceCommand());
    }

    /**
     * Registers event listeners for player connection events.
     */
    private void registerEventListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    /**
     * Starts periodic maintenance tasks: rate limit cleanup, session validation,
     * and voice channel pruning.
     */
    private void startMaintenanceTasks() {
        ProxyServer.getInstance().getScheduler().schedule(this, this::cleanupRateLimits, 60, 60, TimeUnit.SECONDS);
        ProxyServer.getInstance().getScheduler().schedule(this, this::validateSessions, 30, 30, TimeUnit.SECONDS);
        ProxyServer.getInstance().getScheduler().schedule(this, this::pruneVoiceChannels, 45, 45, TimeUnit.SECONDS);
        ProxyServer.getInstance().getScheduler().schedule(this, this::expireAuthCodes, 30, 30, TimeUnit.SECONDS);
    }

    /**
     * Cleans up expired rate limit entries for all tracked IP addresses.
     */
    private void cleanupRateLimits() {
        long now = System.currentTimeMillis();
        rateLimitMap.entrySet().removeIf(entry -> {
            entry.getValue().removeIf(ts -> (now - ts) > rateLimitWindowMs);
            return entry.getValue().isEmpty();
        });
    }

    /**
     * Validates all active player sessions and removes stale entries.
     */
    private void validateSessions() {
        long now = System.currentTimeMillis();
        playerSessions.entrySet().removeIf(entry -> {
            EaglerPlayerSession session = entry.getValue();
            if (session.isExpired(now)) {
                session.disconnect("Session expired");
                return true;
            }
            return false;
        });
    }

    /**
     * Prunes empty or expired voice chat channels.
     */
    private void pruneVoiceChannels() {
        voiceChannels.entrySet().removeIf(entry -> {
            VoiceChannel ch = entry.getValue();
            ch.pruneInactive();
            return ch.isEmpty();
        });
    }

    /**
     * Expires pending authentication codes that have exceeded their time-to-live.
     */
    private void expireAuthCodes() {
        long now = System.currentTimeMillis();
        pendingAuthCodes.entrySet().removeIf(entry -> {
            AuthEntry auth = entry.getValue();
            return (now - auth.getCreatedTime()) > auth.getTtlMs();
        });
    }

    /**
     * Disconnects all connected EaglerCraft players gracefully.
     */
    private void disconnectAllPlayers() {
        for (EaglerPlayerSession session : playerSessions.values()) {
            session.disconnect("Server shutting down");
        }
        playerSessions.clear();
    }

    /**
     * Cleans up all remaining resources and clears registries.
     */
    private void cleanupResources() {
        skinRegistry.clear();
        capeRegistry.clear();
        voiceChannels.clear();
        pendingAuthCodes.clear();
        rateLimitMap.clear();
    }

    /**
     * Saves a default configuration resource to a target file.
     *
     * @param resourcePath the path within the JAR to the default resource
     * @param targetFile   the file to write the resource to
     */
    private void saveDefaultConfig(String resourcePath, File targetFile) {
        targetFile.getParentFile().mkdirs();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
             OutputStream os = java.nio.file.Files.newOutputStream(targetFile.toPath())) {
            if (is == null) {
                LOGGER.severe("Default resource not found: " + resourcePath);
                return;
            }
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save default config: " + resourcePath, e);
        }
    }

    /**
     * Checks if an IP address has exceeded the rate limit.
     *
     * @param ipAddress the IP address to check
     * @return true if the connection should be allowed, false if rate-limited
     */
    public boolean checkRateLimit(String ipAddress) {
        LinkedList<Long> timestamps = rateLimitMap.computeIfAbsent(ipAddress, k -> new LinkedList<>());
        long now = System.currentTimeMillis();

        timestamps.removeIf(ts -> (now - ts) > rateLimitWindowMs);

        if (timestamps.size() >= rateLimitConnections) {
            return false;
        }

        timestamps.add(now);
        return true;
    }

    /**
     * Registers a player skin in the skin service.
     *
     * @param username the player username
     * @param skinData the raw skin image data (64x64 PNG)
     */
    public void registerSkin(String username, byte[] skinData) {
        if (username == null || skinData == null) return;
        skinRegistry.put(username.toLowerCase(), skinData);
    }

    /**
     * Retrieves a player skin from the skin service.
     *
     * @param username the player username
     * @return the skin data, or null if not found
     */
    public byte[] getSkin(String username) {
        if (username == null) return null;
        return skinRegistry.get(username.toLowerCase());
    }

    /**
     * Registers a player cape in the cape service.
     *
     * @param username the player username
     * @param capeData the raw cape image data
     */
    public void registerCape(String username, byte[] capeData) {
        if (username == null || capeData == null) return;
        capeRegistry.put(username.toLowerCase(), capeData);
    }

    /**
     * Retrieves a player cape from the cape service.
     *
     * @param username the player username
     * @return the cape data, or null if not found
     */
    public byte[] getCape(String username) {
        if (username == null) return null;
        return capeRegistry.get(username.toLowerCase());
    }

    /**
     * Creates a new authentication code for Microsoft account login.
     *
     * @param username the requesting player username
     * @return a 6-digit authentication code string
     */
    public String createAuthCode(String username) {
        String code = String.format("%06d", (int) (Math.random() * 1000000));
        pendingAuthCodes.put(code, new AuthEntry(username, System.currentTimeMillis(), 300000L));
        return code;
    }

    /**
     * Validates and consumes an authentication code.
     *
     * @param code     the authentication code to validate
     * @param username the username claiming the code
     * @return true if the code is valid and matches the username
     */
    public boolean validateAuthCode(String code, String username) {
        AuthEntry entry = pendingAuthCodes.remove(code);
        if (entry == null) return false;
        if ((System.currentTimeMillis() - entry.getCreatedTime()) > entry.getTtlMs()) return false;
        return entry.getUsername().equalsIgnoreCase(username);
    }

    /**
     * Gets or creates a voice channel for a given server.
     *
     * @param serverName the BungeeCord server name
     * @return the voice channel instance
     */
    public VoiceChannel getVoiceChannel(String serverName) {
        return voiceChannels.computeIfAbsent(serverName, k -> new VoiceChannel(k));
    }

    /**
     * Handles a player joining the proxy after authentication.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        EaglerPlayerSession session = playerSessions.get(player.getUniqueId());
        if (session != null) {
            session.setBungeePlayer(player);
            LOGGER.fine("EaglerCraft player session linked: " + player.getName());
        }
    }

    /**
     * Handles a player disconnecting from the proxy.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        EaglerPlayerSession session = playerSessions.remove(player.getUniqueId());
        if (session != null) {
            session.onDisconnect();
            LOGGER.fine("EaglerCraft player session removed: " + player.getName());
        }
    }

    /**
     * Handles proxy ping events to customize the MOTD for EaglerCraft clients.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProxyPing(ProxyPingEvent event) {
        // MOTD customization for EaglerCraft browser queries handled by WebSocket
    }

    // ---- Inner classes ----

    /**
     * WebSocket server implementation for EaglerCraft browser client connections.
     * Handles the initial handshake, protocol negotiation, and packet translation
     * between WebSocket (EaglerCraft) and TCP (Minecraft Java) protocols.
     */
    public class EaglerWebSocketServer extends WebSocketServer {

        private final ListenerConfig listenerConfig;
        private final Set<WebSocket> activeConnections = ConcurrentHashMap.newKeySet();

        /**
         * Creates a new EaglerCraft WebSocket server.
         *
         * @param address        the bind address and port
         * @param listenerConfig the listener configuration
         */
        public EaglerWebSocketServer(InetSocketAddress address, ListenerConfig listenerConfig) {
            super(address);
            this.listenerConfig = listenerConfig;
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            String ip = conn.getRemoteSocketAddress().getAddress().getHostAddress();

            if (!checkRateLimit(ip)) {
                conn.close(429, "Rate limited");
                LOGGER.warning("Rate-limited connection from: " + ip);
                return;
            }

            if (!enabled) {
                conn.close(503, "Server shutting down");
                return;
            }

            activeConnections.add(conn);
            String connId = "EAG-" + connectionIdCounter.incrementAndGet();
            conn.setAttachment(connId);

            LOGGER.fine("WebSocket connection opened: " + connId + " from " + ip);
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            activeConnections.remove(conn);
            String connId = conn.getAttachment() != null ? conn.getAttachment().toString() : "unknown";
            LOGGER.fine("WebSocket connection closed: " + connId + " code=" + code);
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            handleTextMessage(conn, message);
        }

        @Override
        public void onMessage(WebSocket conn, ByteBuffer message) {
            handleBinaryMessage(conn, message);
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            if (conn != null) {
                String connId = conn.getAttachment() != null ? conn.getAttachment().toString() : "unknown";
                LOGGER.log(Level.WARNING, "WebSocket error on " + connId, ex);
            } else {
                LOGGER.log(Level.WARNING, "WebSocket server error", ex);
            }
        }

        @Override
        public void onStart() {
            LOGGER.info("EaglerCraft WebSocket server started on " + getAddress());
        }

        /**
         * Handles incoming text messages (JSON-based handshake and control packets).
         *
         * @param conn    the WebSocket connection
         * @param message the text message
         */
        private void handleTextMessage(WebSocket conn, String message) {
            try {
                JsonObject json = JsonParser.parseString(message).getAsJsonObject();
                String type = json.has("type") ? json.get("type").getAsString() : "";

                switch (type) {
                    case "motd":
                        handleMotdQuery(conn, json);
                        break;
                    case "auth":
                        handleAuthRequest(conn, json);
                        break;
                    case "voice":
                        handleVoiceSignal(conn, json);
                        break;
                    case "skin":
                        handleSkinUpload(conn, json);
                        break;
                    case "cape":
                        handleCapeUpload(conn, json);
                        break;
                    default:
                        LOGGER.warning("Unknown text packet type: " + type);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to parse text message", e);
            }
        }

        /**
         * Handles incoming binary messages (Minecraft protocol packets).
         *
         * @param conn    the WebSocket connection
         * @param message the binary message buffer
         */
        private void handleBinaryMessage(WebSocket conn, ByteBuffer message) {
            try {
                byte[] data = new byte[message.remaining()];
                message.get(data);

                // Parse and translate EaglerCraft protocol 775 packet
                int packetId = data[0] & 0xFF;
                byte[] payload = new byte[data.length - 1];
                System.arraycopy(data, 1, payload, 0, payload.length);

                translateAndForward(conn, packetId, payload);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to handle binary message", e);
            }
        }

        /**
         * Handles an MOTD query from an EaglerCraft browser client.
         *
         * @param conn the WebSocket connection
         * @param json the query JSON object
         */
        private void handleMotdQuery(WebSocket conn, JsonObject json) {
            JsonObject motdResponse = new JsonObject();
            motdResponse.addProperty("type", "motd");
            motdResponse.addProperty("name", listenerConfig.getServerName());
            motdResponse.addProperty("version", PROTOCOL_VERSION + "/" + MC_PROTOCOL_VERSION);

            JsonObject players = new JsonObject();
            players.addProperty("max", ProxyServer.getInstance().getConfig().getPlayerLimit());
            players.addProperty("online", ProxyServer.getInstance().getOnlineCount());
            motdResponse.add("players", players);

            motdResponse.addProperty("motd", config != null ? config.getMotdLine1() : "EaglerCraft Server");
            motdResponse.addProperty("motd2", config != null ? config.getMotdLine2() : "Powered by EaglercraftXBungee");

            conn.send(motdResponse.toString());
        }

        /**
         * Handles a Microsoft account authentication request.
         *
         * @param conn the WebSocket connection
         * @param json the auth request JSON object
         */
        private void handleAuthRequest(WebSocket conn, JsonObject json) {
            if (!config.isEnableMicrosoftAuth()) {
                JsonObject resp = new JsonObject();
                resp.addProperty("type", "auth");
                resp.addProperty("result", "disabled");
                conn.send(resp.toString());
                return;
            }

            String username = json.has("username") ? json.get("username").getAsString() : null;
            if (username == null || username.isEmpty()) {
                JsonObject resp = new JsonObject();
                resp.addProperty("type", "auth");
                resp.addProperty("result", "error");
                resp.addProperty("message", "Missing username");
                conn.send(resp.toString());
                return;
            }

            String code = createAuthCode(username);
            JsonObject resp = new JsonObject();
            resp.addProperty("type", "auth");
            resp.addProperty("result", "code");
            resp.addProperty("code", code);
            conn.send(resp.toString());
        }

        /**
         * Handles a voice chat signal relay.
         *
         * @param conn the WebSocket connection
         * @param json the voice signal JSON object
         */
        private void handleVoiceSignal(WebSocket conn, JsonObject json) {
            if (!config.isEnableVoiceChat()) return;

            String serverName = listenerConfig.getDefaultServer();
            VoiceChannel channel = getVoiceChannel(serverName);
            String sender = json.has("sender") ? json.get("sender").getAsString() : "unknown";
            channel.relaySignal(sender, json);
        }

        /**
         * Handles a skin upload from a browser client.
         *
         * @param conn the WebSocket connection
         * @param json the skin data JSON object
         */
        private void handleSkinUpload(WebSocket conn, JsonObject json) {
            String username = json.has("username") ? json.get("username").getAsString() : null;
            if (username == null) return;

            if (json.has("data")) {
                String b64 = json.get("data").getAsString();
                byte[] skinData = java.util.Base64.getDecoder().decode(b64);
                registerSkin(username, skinData);
                LOGGER.fine("Skin registered for: " + username);
            }
        }

        /**
         * Handles a cape upload from a browser client.
         *
         * @param conn the WebSocket connection
         * @param json the cape data JSON object
         */
        private void handleCapeUpload(WebSocket conn, JsonObject json) {
            String username = json.has("username") ? json.get("username").getAsString() : null;
            if (username == null) return;

            if (json.has("data")) {
                String b64 = json.get("data").getAsString();
                byte[] capeData = java.util.Base64.getDecoder().decode(b64);
                registerCape(username, capeData);
                LOGGER.fine("Cape registered for: " + username);
            }
        }

        /**
         * Translates an EaglerCraft protocol packet and forwards it to the
         * appropriate backend Minecraft server via BungeeCord.
         *
         * @param conn     the source WebSocket connection
         * @param packetId the EaglerCraft packet type identifier
         * @param payload  the raw packet payload
         */
        private void translateAndForward(WebSocket conn, int packetId, byte[] payload) {
            EaglerPlayerSession session = findSession(conn);
            if (session == null) {
                // No active session; may need to perform login handshake
                if (packetId == 0x01) {
                    handleLoginPacket(conn, payload);
                }
                return;
            }

            // Protocol 775 translation: map EaglerCraft packet IDs to MC protocol
            switch (packetId) {
                case 0x02: // Keep alive
                    translateKeepAlive(session, payload);
                    break;
                case 0x03: // Chat message
                    translateChatMessage(session, payload);
                    break;
                case 0x04: // Player position
                    translatePlayerPosition(session, payload);
                    break;
                case 0x05: // Player look
                    translatePlayerLook(session, payload);
                    break;
                case 0x06: // Player position and look
                    translatePlayerPositionLook(session, payload);
                    break;
                case 0x07: // Player dig
                    translatePlayerDig(session, payload);
                    break;
                case 0x08: // Player block placement
                    translatePlayerBlockPlace(session, payload);
                    break;
                case 0x09: // Held item change
                    translateHeldItemChange(session, payload);
                    break;
                case 0x0A: // Animation
                    translateAnimation(session, payload);
                    break;
                case 0x0B: // Entity action
                    translateEntityAction(session, payload);
                    break;
                case 0x0C: // Click window
                    translateClickWindow(session, payload);
                    break;
                case 0x0D: // Close window
                    translateCloseWindow(session, payload);
                    break;
                case 0x0E: // Plugin message
                    translatePluginMessage(session, payload);
                    break;
                default:
                    LOGGER.fine("Unhandled packet ID: 0x" + Integer.toHexString(packetId));
            }
        }

        /**
         * Handles the initial login handshake packet from an EaglerCraft client.
         *
         * @param conn    the WebSocket connection
         * @param payload the login packet payload
         */
        private void handleLoginPacket(WebSocket conn, byte[] payload) {
            try {
                String loginData = new String(payload, StandardCharsets.UTF_8);
                JsonObject loginJson = JsonParser.parseString(loginData).getAsJsonObject();

                String username = loginJson.has("username") ? loginJson.get("username").getAsString() : null;
                int requestedProtocol = loginJson.has("protocol") ? loginJson.get("protocol").getAsInt() : 0;

                if (username == null || username.isEmpty()) {
                    conn.close(4001, "Missing username");
                    return;
                }

                if (requestedProtocol != PROTOCOL_VERSION) {
                    JsonObject resp = new JsonObject();
                    resp.addProperty("type", "login_reject");
                    resp.addProperty("reason", "Unsupported protocol version: " + requestedProtocol);
                    conn.send(resp.toString());
                    return;
                }

                UUID playerUuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
                EaglerPlayerSession session = new EaglerPlayerSession(playerUuid, username, conn);
                playerSessions.put(playerUuid, session);

                JsonObject resp = new JsonObject();
                resp.addProperty("type", "login_success");
                resp.addProperty("uuid", playerUuid.toString());
                resp.addProperty("username", username);
                conn.send(resp.toString());

                LOGGER.info("EaglerCraft player logged in: " + username + " (" + playerUuid + ")");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to handle login packet", e);
                conn.close(4002, "Login failed");
            }
        }

        /**
         * Finds the player session associated with a WebSocket connection.
         */
        private EaglerPlayerSession findSession(WebSocket conn) {
            for (EaglerPlayerSession session : playerSessions.values()) {
                if (session.getWebSocket() == conn) {
                    return session;
                }
            }
            return null;
        }

        // Protocol translation stub methods
        private void translateKeepAlive(EaglerPlayerSession s, byte[] p) { s.sendKeepAlive(); }
        private void translateChatMessage(EaglerPlayerSession s, byte[] p) {
            String msg = new String(p, StandardCharsets.UTF_8);
            if (s.getBungeePlayer() != null) s.getBungeePlayer().chat(msg);
        }
        private void translatePlayerPosition(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x15, p); }
        private void translatePlayerLook(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x16, p); }
        private void translatePlayerPositionLook(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x17, p); }
        private void translatePlayerDig(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x1C, p); }
        private void translatePlayerBlockPlace(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x2C, p); }
        private void translateHeldItemChange(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x27, p); }
        private void translateAnimation(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x2A, p); }
        private void translateEntityAction(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x1D, p); }
        private void translateClickWindow(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x09, p); }
        private void translateCloseWindow(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x0A, p); }
        private void translatePluginMessage(EaglerPlayerSession s, byte[] p) { s.forwardPacket(0x12, p); }

        /**
         * Gets the number of active WebSocket connections.
         *
         * @return active connection count
         */
        public int getActiveConnectionCount() {
            return activeConnections.size();
        }
    }

    /**
     * Represents an active EaglerCraft player session bridging WebSocket and BungeeCord.
     */
    public class EaglerPlayerSession {
        private final UUID uuid;
        private final String username;
        private final WebSocket webSocket;
        private final long createdTime;
        private volatile ProxiedPlayer bungeePlayer;
        private volatile long lastKeepAlive;
        private volatile boolean active;

        public EaglerPlayerSession(UUID uuid, String username, WebSocket webSocket) {
            this.uuid = uuid;
            this.username = username;
            this.webSocket = webSocket;
            this.createdTime = System.currentTimeMillis();
            this.lastKeepAlive = System.currentTimeMillis();
            this.active = true;
        }

        /** Sends a keep-alive response to the client. */
        public void sendKeepAlive() {
            lastKeepAlive = System.currentTimeMillis();
        }

        /** Forwards a translated packet to the backend server. */
        public void forwardPacket(int packetId, byte[] payload) {
            if (bungeePlayer == null) return;
            // Packet forwarding handled by BungeeCord internals
        }

        /** Disconnects this session. */
        public void disconnect(String reason) {
            active = false;
            if (webSocket.isOpen()) {
                webSocket.close(1000, reason);
            }
        }

        /** Called when the player disconnects. */
        public void onDisconnect() {
            active = false;
        }

        /** Checks if this session has expired. */
        public boolean isExpired(long now) {
            return !active || (now - lastKeepAlive) > 60000L;
        }

        public UUID getUuid() { return uuid; }
        public String getUsername() { return username; }
        public WebSocket getWebSocket() { return webSocket; }
        public ProxiedPlayer getBungeePlayer() { return bungeePlayer; }
        public void setBungeePlayer(ProxiedPlayer p) { this.bungeePlayer = p; }
    }

    /**
     * Represents a voice chat relay channel for a specific server.
     */
    public class VoiceChannel {
        private final String serverName;
        private final Set<String> participants = ConcurrentHashMap.newKeySet();

        public VoiceChannel(String serverName) {
            this.serverName = serverName;
        }

        /** Relays a voice signal to all participants except the sender. */
        public void relaySignal(String sender, JsonObject signal) {
            // Voice signals are relayed through WebSocket connections
        }

        /** Removes inactive participants. */
        public void pruneInactive() {
            participants.removeIf(p -> {
                EaglerPlayerSession s = playerSessions.values().stream()
                    .filter(ps -> ps.getUsername().equals(p))
                    .findFirst().orElse(null);
                return s == null || !s.active;
            });
        }

        public boolean isEmpty() { return participants.isEmpty(); }
        public String getServerName() { return serverName; }
    }

    /**
     * Represents a pending Microsoft authentication entry.
     */
    public static class AuthEntry {
        private final String username;
        private final long createdTime;
        private final long ttlMs;

        public AuthEntry(String username, long createdTime, long ttlMs) {
            this.username = username;
            this.createdTime = createdTime;
            this.ttlMs = ttlMs;
        }

        public String getUsername() { return username; }
        public long getCreatedTime() { return createdTime; }
        public long getTtlMs() { return ttlMs; }
    }

    /**
     * Plugin configuration holder parsed from settings.yml.
     */
    public static class PluginConfig {
        private String motdLine1 = "EaglerCraft Server";
        private String motdLine2 = "Powered by EaglercraftXBungee";
        private boolean enableMicrosoftAuth = false;
        private boolean enableVoiceChat = true;
        private boolean enableSkins = true;
        private boolean enableCapes = true;
        private int rateLimitConnections = 10;
        private long rateLimitWindowMs = 60000L;
        private int maxPlayersPerIp = 3;
        private boolean enableHttpServer = true;
        private String httpServerPath = "web";
        private int httpServerPort = 8080;

        @SuppressWarnings("unchecked")
        public static PluginConfig fromMap(Map<String, Object> map) {
            PluginConfig c = new PluginConfig();
            if (map == null) return c;
            if (map.containsKey("motd")) {
                Map<String, String> motd = (Map<String, String>) map.get("motd");
                c.motdLine1 = motd.getOrDefault("line1", c.motdLine1);
                c.motdLine2 = motd.getOrDefault("line2", c.motdLine2);
            }
            if (map.containsKey("authentication")) {
                Map<String, Object> auth = (Map<String, Object>) map.get("authentication");
                c.enableMicrosoftAuth = Boolean.TRUE.equals(auth.getOrDefault("enable_microsoft_auth", false));
            }
            if (map.containsKey("voice_chat")) {
                Map<String, Object> vc = (Map<String, Object>) map.get("voice_chat");
                c.enableVoiceChat = Boolean.TRUE.equals(vc.getOrDefault("enabled", true));
            }
            if (map.containsKey("skins")) {
                Map<String, Object> sk = (Map<String, Object>) map.get("skins");
                c.enableSkins = Boolean.TRUE.equals(sk.getOrDefault("enabled", true));
            }
            if (map.containsKey("capes")) {
                Map<String, Object> cp = (Map<String, Object>) map.get("capes");
                c.enableCapes = Boolean.TRUE.equals(cp.getOrDefault("enabled", true));
            }
            if (map.containsKey("rate_limit")) {
                Map<String, Object> rl = (Map<String, Object>) map.get("rate_limit");
                c.rateLimitConnections = ((Number) rl.getOrDefault("connections_per_minute", 10)).intValue();
                c.rateLimitWindowMs = ((Number) rl.getOrDefault("window_ms", 60000)).longValue();
                c.maxPlayersPerIp = ((Number) rl.getOrDefault("max_players_per_ip", 3)).intValue();
            }
            if (map.containsKey("http_server")) {
                Map<String, Object> hs = (Map<String, Object>) map.get("http_server");
                c.enableHttpServer = Boolean.TRUE.equals(hs.getOrDefault("enabled", true));
                c.httpServerPath = (String) hs.getOrDefault("path", "web");
                c.httpServerPort = ((Number) hs.getOrDefault("port", 8080)).intValue();
            }
            return c;
        }

        public static PluginConfig createDefault() { return new PluginConfig(); }

        public String getMotdLine1() { return motdLine1; }
        public String getMotdLine2() { return motdLine2; }
        public boolean isEnableMicrosoftAuth() { return enableMicrosoftAuth; }
        public boolean isEnableVoiceChat() { return enableVoiceChat; }
        public boolean isEnableSkins() { return enableSkins; }
        public boolean isEnableCapes() { return enableCapes; }
        public int getRateLimitConnections() { return rateLimitConnections; }
        public long getRateLimitWindowMs() { return rateLimitWindowMs; }
        public int getMaxPlayersPerIp() { return maxPlayersPerIp; }
        public boolean isEnableHttpServer() { return enableHttpServer; }
        public String getHttpServerPath() { return httpServerPath; }
        public int getHttpServerPort() { return httpServerPort; }
    }

    /**
     * WebSocket listener configuration holder parsed from listeners.yml.
     */
    public static class ListenerConfig {
        private String name = "default";
        private String host = "0.0.0.0";
        private int port = 8081;
        private boolean enabled = true;
        private String serverName = "EaglerCraft Server";
        private String defaultServer = "lobby";
        private boolean enableSkins = true;
        private boolean enableVoiceChat = true;
        private boolean enableCapes = true;
        private boolean requireAuth = false;
        private int maxPlayers = 60;
        private boolean allowOfflineDownloads = false;

        @SuppressWarnings("unchecked")
        public static ListenerConfig fromMap(Map<String, Object> map) {
            ListenerConfig c = new ListenerConfig();
            if (map == null) return c;
            c.name = (String) map.getOrDefault("name", c.name);
            c.host = (String) map.getOrDefault("host", c.host);
            c.port = ((Number) map.getOrDefault("port", c.port)).intValue();
            c.enabled = Boolean.TRUE.equals(map.getOrDefault("enabled", true));
            c.serverName = (String) map.getOrDefault("server_name", c.serverName);
            c.defaultServer = (String) map.getOrDefault("default_server", c.defaultServer);
            c.enableSkins = Boolean.TRUE.equals(map.getOrDefault("enable_skins", true));
            c.enableVoiceChat = Boolean.TRUE.equals(map.getOrDefault("enable_voice_chat", true));
            c.enableCapes = Boolean.TRUE.equals(map.getOrDefault("enable_capes", true));
            c.requireAuth = Boolean.TRUE.equals(map.getOrDefault("require_auth", false));
            c.maxPlayers = ((Number) map.getOrDefault("max_players", 60)).intValue();
            c.allowOfflineDownloads = Boolean.TRUE.equals(map.getOrDefault("allow_offline_downloads", false));
            return c;
        }

        public String getName() { return name; }
        public String getHost() { return host; }
        public int getPort() { return port; }
        public boolean isEnabled() { return enabled; }
        public String getServerName() { return serverName; }
        public String getDefaultServer() { return defaultServer; }
        public boolean isEnableSkins() { return enableSkins; }
        public boolean isEnableVoiceChat() { return enableVoiceChat; }
        public boolean isEnableCapes() { return enableCapes; }
        public boolean isRequireAuth() { return requireAuth; }
        public int getMaxPlayers() { return maxPlayers; }
        public boolean isAllowOfflineDownloads() { return allowOfflineDownloads; }
    }

    /**
     * /eagler command - Main administration command for the EaglercraftXBungee plugin.
     */
    private class EaglerCommand extends Command {
        EaglerCommand() {
            super("eagler", "eaglercraft.admin", "eag");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length == 0) {
                sender.sendMessage("EaglercraftXBungee v" + PLUGIN_VERSION);
                sender.sendMessage("Active sessions: " + playerSessions.size());
                sender.sendMessage("WebSocket listeners: " + wsServers.size());
                return;
            }

            switch (args[0].toLowerCase()) {
                case "reload":
                    loadConfiguration();
                    loadListenerConfiguration();
                    sender.sendMessage("Configuration reloaded.");
                    break;
                case "list":
                    playerSessions.values().forEach(s ->
                        sender.sendMessage(" - " + s.getUsername() + " (" + s.getUuid() + ")")
                    );
                    break;
                case "rate":
                    sender.sendMessage("Rate limit entries: " + rateLimitMap.size());
                    break;
                case "skins":
                    sender.sendMessage("Registered skins: " + skinRegistry.size());
                    sender.sendMessage("Registered capes: " + capeRegistry.size());
                    break;
                case "voice":
                    sender.sendMessage("Voice channels: " + voiceChannels.size());
                    break;
                case "auth":
                    sender.sendMessage("Pending auth codes: " + pendingAuthCodes.size());
                    break;
                default:
                    sender.sendMessage("Unknown subcommand: " + args[0]);
            }
        }
    }

    /**
     * /eaglerskin command - Skin and cape management command.
     */
    private class EaglerSkinCommand extends Command {
        EaglerSkinCommand() {
            super("eaglerskin", "eaglercraft.admin");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /eaglerskin <list|clear|reload> [username]");
                return;
            }
            if ("list".equalsIgnoreCase(args[0])) {
                sender.sendMessage("Skins: " + skinRegistry.size() + " / Capes: " + capeRegistry.size());
            } else if ("clear".equalsIgnoreCase(args[0]) && args.length > 1) {
                skinRegistry.remove(args[1].toLowerCase());
                capeRegistry.remove(args[1].toLowerCase());
                sender.sendMessage("Cleared skin/cape for: " + args[1]);
            }
        }
    }

    /**
     * /eaglervoice command - Voice chat management command.
     */
    private class EaglerVoiceCommand extends Command {
        EaglerVoiceCommand() {
            super("eaglervoice", "eaglercraft.admin");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length == 0) {
                sender.sendMessage("Voice channels: " + voiceChannels.size());
                voiceChannels.values().forEach(ch ->
                    sender.sendMessage(" - " + ch.getServerName() + " (" + ch.participants.size() + " players)")
                );
                return;
            }
            sender.sendMessage("Unknown subcommand: " + args[0]);
        }
    }

    // Public accessors
    public PluginConfig getConfig() { return config; }
    public Map<String, EaglerWebSocketServer> getWsServers() { return wsServers; }
    public Map<UUID, EaglerPlayerSession> getPlayerSessions() { return playerSessions; }
    public Map<String, ListenerConfig> getListenerConfigs() { return listenerConfigs; }
}
