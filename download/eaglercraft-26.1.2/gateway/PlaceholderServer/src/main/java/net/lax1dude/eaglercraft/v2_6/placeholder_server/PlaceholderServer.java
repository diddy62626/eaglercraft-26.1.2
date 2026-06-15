package net.lax1dude.eaglercraft.v2_6.placeholder_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * PlaceholderServer - A simple standalone WebSocket server for EaglerCraft testing.
 * <p>
 * This server provides a lightweight alternative to the full BungeeCord or Velocity
 * gateway plugins. It is designed for testing and development purposes, providing:
 * <ul>
 *   <li>MOTD display in the EaglerCraft server browser</li>
 *   <li>Server redirect functionality to configured backend servers</li>
 *   <li>Basic connection management without requiring a Minecraft server backend</li>
 *   <li>WebSocket listener compatible with EaglerCraft browser clients</li>
 * </ul>
 * </p>
 * <p>
 * The PlaceholderServer does NOT translate Minecraft protocol packets. It simply
 * shows an MOTD and can redirect clients to other servers that are running a
 * full EaglerCraft gateway plugin. This is useful for setting up server lists
 * or testing EaglerCraft client connections without a full proxy stack.
 * </p>
 *
 * <h3>Usage:</h3>
 * <pre>
 *   java -jar PlaceholderServer.jar [config.yml]
 * </pre>
 *
 * @author lax1dude
 * @version 1.0.0
 * @since 26.1.2
 */
public class PlaceholderServer {

    /** Server version string */
    public static final String SERVER_VERSION = "1.0.0";

    /** EaglerCraft protocol version number */
    public static final int PROTOCOL_VERSION = 775;

    /** Minecraft protocol version string */
    public static final String MC_PROTOCOL_VERSION = "1.21.4";

    /** Gson instance for JSON serialization */
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** Logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger("PlaceholderServer");

    /** Default bind address */
    private static final String DEFAULT_HOST = "0.0.0.0";

    /** Default WebSocket port */
    private static final int DEFAULT_PORT = 8081;

    /** Singleton server instance */
    private static PlaceholderServer instance;

    /** Server configuration */
    private ServerConfig config;

    /** WebSocket server instance */
    private PlaceholderWebSocketServer wsServer;

    /** HTTP file server instance */
    private SimpleHttpServer httpServer;

    /** Active client connections: connection ID to client info */
    private final Map<String, ClientInfo> activeClients = new ConcurrentHashMap<>();

    /** Rate limit tracking: IP to connection timestamps */
    private final Map<String, LinkedList<Long>> rateLimitMap = new ConcurrentHashMap<>();

    /** Scheduled executor for maintenance tasks */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    /** Connection ID counter */
    private final AtomicInteger connectionCounter = new AtomicInteger(0);

    /** Whether the server is running */
    private volatile boolean running = false;

    /** Start time for uptime calculation */
    private long startTime;

    /**
     * Main entry point for the PlaceholderServer application.
     *
     * @param args command-line arguments; optional path to config.yml
     */
    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : "config.yml";
        LOGGER.info("EaglerCraft PlaceholderServer v{} starting...", SERVER_VERSION);

        try {
            PlaceholderServer server = new PlaceholderServer();
            instance = server;
            server.start(configPath);
        } catch (Exception e) {
            LOGGER.error("Fatal error during server startup", e);
            System.exit(1);
        }
    }

    /**
     * Gets the singleton server instance.
     *
     * @return the active PlaceholderServer instance
     */
    public static PlaceholderServer getInstance() {
        return instance;
    }

    /**
     * Starts the PlaceholderServer with the given configuration file.
     * Initializes the WebSocket server, HTTP server, and maintenance tasks.
     *
     * @param configPath path to the YAML configuration file
     * @throws IOException if the configuration cannot be loaded
     */
    public void start(String configPath) throws IOException {
        loadConfiguration(configPath);
        startTime = System.currentTimeMillis();
        running = true;

        // Start WebSocket server
        InetSocketAddress wsAddr = new InetSocketAddress(config.getHost(), config.getPort());
        wsServer = new PlaceholderWebSocketServer(wsAddr);
        wsServer.setConnectionLostTimeout(30);
        wsServer.start();
        LOGGER.info("WebSocket server started on {}:{}", config.getHost(), config.getPort());

        // Start HTTP server if enabled
        if (config.isEnableHttpServer()) {
            try {
                httpServer = new SimpleHttpServer(config.getHttpHost(), config.getHttpPort(), config.getWebRoot());
                httpServer.start();
                LOGGER.info("HTTP server started on {}:{}", config.getHttpHost(), config.getHttpPort());
            } catch (IOException e) {
                LOGGER.warn("Failed to start HTTP server (non-fatal): {}", e.getMessage());
            }
        }

        // Start maintenance tasks
        scheduler.scheduleAtFixedRate(this::cleanupRateLimits, 60, 60, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::cleanupStaleConnections, 30, 30, TimeUnit.SECONDS);

        LOGGER.info("PlaceholderServer v{} is ready. MOTD: {}", SERVER_VERSION, config.getMotdLine1());

        // Register shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "PlaceholderServer-Shutdown"));
    }

    /**
     * Gracefully shuts down the server, stopping WebSocket and HTTP servers.
     */
    public void shutdown() {
        if (!running) return;
        running = false;
        LOGGER.info("PlaceholderServer shutting down...");

        // Disconnect all clients
        for (ClientInfo client : activeClients.values()) {
            if (client.getWebSocket() != null && client.getWebSocket().isOpen()) {
                client.getWebSocket().close(1001, "Server shutting down");
            }
        }
        activeClients.clear();

        // Stop WebSocket server
        if (wsServer != null) {
            try {
                wsServer.stop(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Stop HTTP server
        if (httpServer != null) {
            httpServer.stop();
        }

        // Shutdown scheduler
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        LOGGER.info("PlaceholderServer stopped.");
    }

    /**
     * Loads the server configuration from a YAML file.
     * Creates a default configuration if the file does not exist.
     *
     * @param configPath the path to the configuration file
     * @throws IOException if the configuration cannot be read
     */
    @SuppressWarnings("unchecked")
    private void loadConfiguration(String configPath) throws IOException {
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }

        try (InputStream is = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml();
            Map<String, Object> raw = yaml.load(is);
            config = ServerConfig.fromMap(raw);
            LOGGER.info("Loaded configuration from {}", configPath);
        }
    }

    /**
     * Creates a default configuration file.
     *
     * @param configFile the file to create
     * @throws IOException if the file cannot be written
     */
    private void createDefaultConfig(File configFile) throws IOException {
        String defaultYaml = "# PlaceholderServer Configuration v26.1.2\n"
            + "server:\n"
            + "  host: \"0.0.0.0\"\n"
            + "  port: 8081\n"
            + "  name: \"EaglerCraft Placeholder Server\"\n"
            + "  max_players: 60\n"
            + "\n"
            + "motd:\n"
            + "  line1: \"&6EaglerCraft Placeholder Server\"\n"
            + "  line2: \"&7Connect to a real server to play!\"\n"
            + "\n"
            + "redirect:\n"
            + "  enabled: false\n"
            + "  servers:\n"
            + "    - address: \"ws://localhost:8082\"\n"
            + "      name: \"Survival Server\"\n"
            + "    - address: \"ws://localhost:8083\"\n"
            + "      name: \"Creative Server\"\n"
            + "\n"
            + "rate_limit:\n"
            + "  connections_per_minute: 15\n"
            + "  window_ms: 60000\n"
            + "\n"
            + "http_server:\n"
            + "  enabled: true\n"
            + "  host: \"0.0.0.0\"\n"
            + "  port: 8080\n"
            + "  web_root: \"web\"\n"
            + "\n"
            + "debug:\n"
            + "  enabled: false\n"
            + "  log_connections: true\n";

        Files.write(configFile.toPath(), defaultYaml.getBytes(StandardCharsets.UTF_8));
        LOGGER.info("Created default configuration at {}", configFile.getAbsolutePath());
    }

    /**
     * Checks if an IP address has exceeded the rate limit.
     *
     * @param ipAddress the IP address to check
     * @return true if allowed, false if rate-limited
     */
    public boolean checkRateLimit(String ipAddress) {
        int limit = config != null ? config.getRateLimitConnections() : 15;
        long windowMs = config != null ? config.getRateLimitWindowMs() : 60000L;

        LinkedList<Long> timestamps = rateLimitMap.computeIfAbsent(ipAddress, k -> new LinkedList<>());
        long now = System.currentTimeMillis();
        timestamps.removeIf(ts -> (now - ts) > windowMs);

        if (timestamps.size() >= limit) {
            return false;
        }

        timestamps.add(now);
        return true;
    }

    /**
     * Periodically cleans up expired rate limit entries.
     */
    private void cleanupRateLimits() {
        long windowMs = config != null ? config.getRateLimitWindowMs() : 60000L;
        long now = System.currentTimeMillis();
        rateLimitMap.entrySet().removeIf(entry -> {
            entry.getValue().removeIf(ts -> (now - ts) > windowMs);
            return entry.getValue().isEmpty();
        });
    }

    /**
     * Periodically cleans up stale client connections.
     */
    private void cleanupStaleConnections() {
        long now = System.currentTimeMillis();
        activeClients.entrySet().removeIf(entry -> {
            ClientInfo client = entry.getValue();
            if (client.isExpired(now)) {
                if (client.getWebSocket() != null && client.getWebSocket().isOpen()) {
                    client.getWebSocket().close(1000, "Connection timed out");
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Gets the server uptime in seconds.
     *
     * @return uptime in seconds
     */
    public long getUptimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000L;
    }

    // ---- Inner classes ----

    /**
     * WebSocket server implementation for EaglerCraft browser client connections.
     * Handles MOTD queries and server redirects.
     */
    public class PlaceholderWebSocketServer extends WebSocketServer {

        private final Set<WebSocket> connections = ConcurrentHashMap.newKeySet();

        /**
         * Creates a new PlaceholderWebSocketServer.
         *
         * @param address the bind address
         */
        public PlaceholderWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            String ip = conn.getRemoteSocketAddress().getAddress().getHostAddress();

            if (!checkRateLimit(ip)) {
                conn.close(429, "Rate limited");
                LOGGER.warn("Rate-limited connection from: {}", ip);
                return;
            }

            String connId = "PLC-" + connectionCounter.incrementAndGet();
            conn.setAttachment(connId);
            connections.add(conn);

            ClientInfo client = new ClientInfo(connId, ip, conn, System.currentTimeMillis());
            activeClients.put(connId, client);

            LOGGER.debug("Client connected: {} from {}", connId, ip);
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            connections.remove(conn);
            String connId = conn.getAttachment() != null ? conn.getAttachment().toString() : "unknown";
            activeClients.remove(connId);
            LOGGER.debug("Client disconnected: {} code={}", connId, code);
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            try {
                JsonObject json = JsonParser.parseString(message).getAsJsonObject();
                String type = json.has("type") ? json.get("type").getAsString() : "";

                switch (type) {
                    case "motd":
                        handleMotdQuery(conn);
                        break;
                    case "version":
                        handleVersionQuery(conn);
                        break;
                    case "redirect_list":
                        handleRedirectListQuery(conn);
                        break;
                    case "connect":
                        handleConnectRequest(conn, json);
                        break;
                    case "ping":
                        handlePing(conn);
                        break;
                    default:
                        LOGGER.debug("Unknown packet type from client: {}", type);
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to process text message from client", e);
            }
        }

        @Override
        public void onMessage(WebSocket conn, ByteBuffer message) {
            // PlaceholderServer does not handle binary MC protocol packets
            LOGGER.debug("Received binary message (not supported in placeholder mode)");
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            if (conn != null) {
                String connId = conn.getAttachment() != null ? conn.getAttachment().toString() : "unknown";
                LOGGER.warn("WebSocket error on {}", connId, ex);
            } else {
                LOGGER.warn("WebSocket server error", ex);
            }
        }

        @Override
        public void onStart() {
            LOGGER.info("PlaceholderServer WebSocket started on {}", getAddress());
        }

        /**
         * Handles an MOTD query from a browser client.
         *
         * @param conn the WebSocket connection
         */
        private void handleMotdQuery(WebSocket conn) {
            JsonObject response = new JsonObject();
            response.addProperty("type", "motd");
            response.addProperty("name", config.getServerName());
            response.addProperty("version", PROTOCOL_VERSION + "/" + MC_PROTOCOL_VERSION);

            JsonObject players = new JsonObject();
            players.addProperty("max", config.getMaxPlayers());
            players.addProperty("online", activeClients.size());
            response.add("players", players);

            response.addProperty("motd", config.getMotdLine1());
            response.addProperty("motd2", config.getMotdLine2());

            // Add server icon placeholder
            response.addProperty("icon", "");

            // Add ping timestamp
            response.addProperty("ping", System.currentTimeMillis());

            conn.send(response.toString());
        }

        /**
         * Handles a version query from a browser client.
         *
         * @param conn the WebSocket connection
         */
        private void handleVersionQuery(WebSocket conn) {
            JsonObject response = new JsonObject();
            response.addProperty("type", "version");
            response.addProperty("eagler_protocol", PROTOCOL_VERSION);
            response.addProperty("mc_protocol", MC_PROTOCOL_VERSION);
            response.addProperty("server_version", SERVER_VERSION);
            response.addProperty("server_type", "placeholder");
            conn.send(response.toString());
        }

        /**
         * Handles a redirect list query - returns available servers to redirect to.
         *
         * @param conn the WebSocket connection
         */
        private void handleRedirectListQuery(WebSocket conn) {
            JsonObject response = new JsonObject();
            response.addProperty("type", "redirect_list");

            if (config.isRedirectEnabled()) {
                JsonArray servers = new JsonArray();
                for (RedirectServer rs : config.getRedirectServers()) {
                    JsonObject server = new JsonObject();
                    server.addProperty("address", rs.getAddress());
                    server.addProperty("name", rs.getName());
                    servers.add(server);
                }
                response.add("servers", servers);
            } else {
                response.add("servers", new JsonArray());
                response.addProperty("message", "No redirect servers configured");
            }

            conn.send(response.toString());
        }

        /**
         * Handles a connect request - redirects the client to a specified server.
         *
         * @param conn the WebSocket connection
         * @param json the connect request JSON object
         */
        private void handleConnectRequest(WebSocket conn, JsonObject json) {
            String targetAddress = json.has("address") ? json.get("address").getAsString() : null;

            if (!config.isRedirectEnabled()) {
                JsonObject response = new JsonObject();
                response.addProperty("type", "connect_result");
                response.addProperty("success", false);
                response.addProperty("message", "Redirect is not enabled on this server");
                conn.send(response.toString());
                return;
            }

            // Find the target server in the redirect list
            Optional<RedirectServer> target = config.getRedirectServers().stream()
                .filter(rs -> rs.getAddress().equals(targetAddress))
                .findFirst();

            if (target.isPresent()) {
                JsonObject response = new JsonObject();
                response.addProperty("type", "connect_result");
                response.addProperty("success", true);
                response.addProperty("address", target.get().getAddress());
                response.addProperty("name", target.get().getName());
                conn.send(response.toString());

                LOGGER.info("Redirecting client to: {}", target.get().getName());

                // Close connection after redirect
                conn.close(1001, "Redirecting to " + target.get().getName());
            } else {
                JsonObject response = new JsonObject();
                response.addProperty("type", "connect_result");
                response.addProperty("success", false);
                response.addProperty("message", "Target server not found: " + targetAddress);
                conn.send(response.toString());
            }
        }

        /**
         * Handles a keep-alive ping from a client.
         *
         * @param conn the WebSocket connection
         */
        private void handlePing(WebSocket conn) {
            String connId = conn.getAttachment() != null ? conn.getAttachment().toString() : null;
            if (connId != null) {
                ClientInfo client = activeClients.get(connId);
                if (client != null) {
                    client.updateLastActivity();
                }
            }

            JsonObject response = new JsonObject();
            response.addProperty("type", "pong");
            response.addProperty("timestamp", System.currentTimeMillis());
            conn.send(response.toString());
        }

        /**
         * Gets the number of active connections.
         *
         * @return active connection count
         */
        public int getActiveConnectionCount() {
            return connections.size();
        }
    }

    /**
     * Represents a connected client's information.
     */
    public static class ClientInfo {
        private final String connectionId;
        private final String ipAddress;
        private final WebSocket webSocket;
        private final long connectedTime;
        private volatile long lastActivity;

        public ClientInfo(String connectionId, String ipAddress, WebSocket webSocket, long connectedTime) {
            this.connectionId = connectionId;
            this.ipAddress = ipAddress;
            this.webSocket = webSocket;
            this.connectedTime = connectedTime;
            this.lastActivity = connectedTime;
        }

        /** Updates the last activity timestamp. */
        public void updateLastActivity() {
            this.lastActivity = System.currentTimeMillis();
        }

        /** Checks if the client connection has expired. */
        public boolean isExpired(long now) {
            return (now - lastActivity) > 120000L; // 2 minute timeout
        }

        public String getConnectionId() { return connectionId; }
        public String getIpAddress() { return ipAddress; }
        public WebSocket getWebSocket() { return webSocket; }
        public long getConnectedTime() { return connectedTime; }
        public long getLastActivity() { return lastActivity; }
    }

    /**
     * Simple HTTP server for serving web files (EaglerCraft client HTML/JS).
     * Uses Java's built-in HttpServer for lightweight file serving.
     */
    public static class SimpleHttpServer {
        private final String host;
        private final int port;
        private final String webRoot;
        private com.sun.net.httpserver.HttpServer httpServer;

        public SimpleHttpServer(String host, int port, String webRoot) throws IOException {
            this.host = host;
            this.port = port;
            this.webRoot = webRoot;
        }

        /**
         * Starts the HTTP file server.
         *
         * @throws IOException if the server cannot bind to the address
         */
        public void start() throws IOException {
            httpServer = com.sun.net.httpserver.HttpServer.create(
                new InetSocketAddress(host, port), 0);

            httpServer.createContext("/", exchange -> {
                String requestPath = exchange.getRequestURI().getPath();
                if (requestPath.equals("/") || requestPath.isEmpty()) {
                    requestPath = "/index.html";
                }

                // Security: prevent directory traversal
                Path filePath = Paths.get(webRoot, requestPath).normalize();
                Path webRootPath = Paths.get(webRoot).normalize();
                if (!filePath.startsWith(webRootPath)) {
                    sendResponse(exchange, 403, "Forbidden".getBytes(StandardCharsets.UTF_8), "text/plain");
                    return;
                }

                if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                    String contentType = guessContentType(filePath.toString());
                    byte[] content = Files.readAllBytes(filePath);
                    sendResponse(exchange, 200, content, contentType);
                } else {
                    byte[] notFound = ("404 Not Found: " + requestPath).getBytes(StandardCharsets.UTF_8);
                    sendResponse(exchange, 404, notFound, "text/plain");
                }
            });

            httpServer.setExecutor(Executors.newFixedThreadPool(4));
            httpServer.start();
        }

        /**
         * Sends an HTTP response.
         */
        private void sendResponse(com.sun.net.httpserver.HttpExchange exchange,
                                   int statusCode, byte[] body, String contentType) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Server", "EaglercraftPlaceholder/" + SERVER_VERSION);
            exchange.sendResponseHeaders(statusCode, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        }

        /**
         * Guesses the content type from a file path.
         */
        private String guessContentType(String path) {
            if (path.endsWith(".html") || path.endsWith(".htm")) return "text/html";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
            if (path.endsWith(".gif")) return "image/gif";
            if (path.endsWith(".svg")) return "image/svg+xml";
            if (path.endsWith(".json")) return "application/json";
            if (path.endsWith(".wasm")) return "application/wasm";
            if (path.endsWith(".ico")) return "image/x-icon";
            return "application/octet-stream";
        }

        /**
         * Stops the HTTP server.
         */
        public void stop() {
            if (httpServer != null) {
                httpServer.stop(2);
            }
        }
    }

    /**
     * Server configuration holder parsed from YAML.
     */
    public static class ServerConfig {
        private String host = DEFAULT_HOST;
        private int port = DEFAULT_PORT;
        private String serverName = "EaglerCraft Placeholder Server";
        private int maxPlayers = 60;
        private String motdLine1 = "&6EaglerCraft Placeholder Server";
        private String motdLine2 = "&7Connect to a real server to play!";
        private boolean redirectEnabled = false;
        private List<RedirectServer> redirectServers = new ArrayList<>();
        private int rateLimitConnections = 15;
        private long rateLimitWindowMs = 60000L;
        private boolean enableHttpServer = true;
        private String httpHost = "0.0.0.0";
        private int httpPort = 8080;
        private String webRoot = "web";
        private boolean debugEnabled = false;
        private boolean logConnections = true;

        @SuppressWarnings("unchecked")
        public static ServerConfig fromMap(Map<String, Object> map) {
            ServerConfig c = new ServerConfig();
            if (map == null) return c;

            if (map.containsKey("server")) {
                Map<String, Object> server = (Map<String, Object>) map.get("server");
                c.host = (String) server.getOrDefault("host", c.host);
                c.port = ((Number) server.getOrDefault("port", c.port)).intValue();
                c.serverName = (String) server.getOrDefault("name", c.serverName);
                c.maxPlayers = ((Number) server.getOrDefault("max_players", c.maxPlayers)).intValue();
            }

            if (map.containsKey("motd")) {
                Map<String, String> motd = (Map<String, String>) map.get("motd");
                c.motdLine1 = motd.getOrDefault("line1", c.motdLine1);
                c.motdLine2 = motd.getOrDefault("line2", c.motdLine2);
            }

            if (map.containsKey("redirect")) {
                Map<String, Object> redirect = (Map<String, Object>) map.get("redirect");
                c.redirectEnabled = Boolean.TRUE.equals(redirect.getOrDefault("enabled", false));
                List<Map<String, Object>> servers = (List<Map<String, Object>>) redirect.get("servers");
                if (servers != null) {
                    for (Map<String, Object> sMap : servers) {
                        c.redirectServers.add(new RedirectServer(
                            (String) sMap.getOrDefault("address", ""),
                            (String) sMap.getOrDefault("name", "Unknown Server")
                        ));
                    }
                }
            }

            if (map.containsKey("rate_limit")) {
                Map<String, Object> rl = (Map<String, Object>) map.get("rate_limit");
                c.rateLimitConnections = ((Number) rl.getOrDefault("connections_per_minute", 15)).intValue();
                c.rateLimitWindowMs = ((Number) rl.getOrDefault("window_ms", 60000)).longValue();
            }

            if (map.containsKey("http_server")) {
                Map<String, Object> hs = (Map<String, Object>) map.get("http_server");
                c.enableHttpServer = Boolean.TRUE.equals(hs.getOrDefault("enabled", true));
                c.httpHost = (String) hs.getOrDefault("host", "0.0.0.0");
                c.httpPort = ((Number) hs.getOrDefault("port", 8080)).intValue();
                c.webRoot = (String) hs.getOrDefault("web_root", "web");
            }

            if (map.containsKey("debug")) {
                Map<String, Object> dbg = (Map<String, Object>) map.get("debug");
                c.debugEnabled = Boolean.TRUE.equals(dbg.getOrDefault("enabled", false));
                c.logConnections = Boolean.TRUE.equals(dbg.getOrDefault("log_connections", true));
            }

            return c;
        }

        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getServerName() { return serverName; }
        public int getMaxPlayers() { return maxPlayers; }
        public String getMotdLine1() { return motdLine1; }
        public String getMotdLine2() { return motdLine2; }
        public boolean isRedirectEnabled() { return redirectEnabled; }
        public List<RedirectServer> getRedirectServers() { return redirectServers; }
        public int getRateLimitConnections() { return rateLimitConnections; }
        public long getRateLimitWindowMs() { return rateLimitWindowMs; }
        public boolean isEnableHttpServer() { return enableHttpServer; }
        public String getHttpHost() { return httpHost; }
        public int getHttpPort() { return httpPort; }
        public String getWebRoot() { return webRoot; }
        public boolean isDebugEnabled() { return debugEnabled; }
        public boolean isLogConnections() { return logConnections; }
    }

    /**
     * Represents a redirect server entry.
     */
    public static class RedirectServer {
        private final String address;
        private final String name;

        public RedirectServer(String address, String name) {
            this.address = address;
            this.name = name;
        }

        public String getAddress() { return address; }
        public String getName() { return name; }
    }

    // Public accessors
    public ServerConfig getConfig() { return config; }
    public PlaceholderWebSocketServer getWsServer() { return wsServer; }
    public Map<String, ClientInfo> getActiveClients() { return activeClients; }
    public boolean isRunning() { return running; }
}
