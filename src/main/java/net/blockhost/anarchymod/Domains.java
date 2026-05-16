package net.blockhost.anarchymod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Domains {

    private static final Logger LOGGER = Logger.getLogger("AnarchyMod-Domains");
    private static final String DOMAINS_URL = "https://www.6b6t.org/api/anarchy-mod.json";

    private static final Set<String> DEFAULT_BLOCKED_PATTERNS = Set.of(
        "*.6b6t.org", "6b6t.org",
        "*.10b10t.org", "10b10t.org",
        "*.6b6t.cc", "6b6t.cc",
        "*.6b6t.me", "6b6t.me",
        "*.7b7t.me", "7b7t.me",
        "*.8b8t.org", "8b8t.org",
        "*.alacity.net", "alacity.net",
        "*.anarchypvp.pw", "anarchypvp.pw",
        "*.l2x9.org", "l2x9.org",
        "*.simpleanarchy.org", "simpleanarchy.org"
    );

    private static final List<ServerEntry> DEFAULT_FEATURED_SERVERS = List.of(
        new ServerEntry("6b6t", "6b6t.org"),
        new ServerEntry("10b10t", "10b10t.org"),
        new ServerEntry("SimpleAnarchy", "simpleanarchy.org")
    );

    private static final Set<String> exactDomains = new HashSet<>();
    private static final Set<String> wildcardDomains = new HashSet<>();
    private static final Map<String, ServerEntry> featuredServers = new LinkedHashMap<>();
    private static final Gson gson = new Gson();

    static {
        DEFAULT_BLOCKED_PATTERNS.forEach(Domains::addDomainPattern);
        DEFAULT_FEATURED_SERVERS.forEach(entry -> addFeaturedServer(entry.name(), entry.address()));
        loadRemote();
    }

    public record ServerEntry(String name, String address) {
    }

    private static void loadRemote() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DOMAINS_URL))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            if (json == null) return;

            JsonArray domainsArray = json.getAsJsonArray("domains");
            if (domainsArray != null) {
                domainsArray.forEach(element ->
                    addDomainPattern(element.getAsString())
                );
            }

            JsonArray featuredArray = json.getAsJsonArray("featured_servers");
            if (featuredArray != null) {
                featuredArray.forEach(element -> {
                    if (element.isJsonPrimitive()) {
                        addFeaturedServer(element.getAsString(), element.getAsString());
                        return;
                    }

                    JsonObject object = element.getAsJsonObject();
                    if (!object.has("address")) return;

                    String name = object.has("name")
                        ? object.get("name").getAsString()
                        : object.get("address").getAsString();
                    addFeaturedServer(name, object.get("address").getAsString());
                });
            }
        } catch (Throwable t) {
            LOGGER.warning("Failed to load domains from remote, using defaults: " + t.getMessage());
        }
    }

    public static boolean contains(String input) {
        String domain = normalizeDomain(input);
        if (domain.isEmpty()) return false;

        if (exactDomains.contains(domain)) return true;

        for (String wildcardBase : wildcardDomains) {
            if (domain.equals(wildcardBase) || domain.endsWith("." + wildcardBase)) {
                return true;
            }
        }

        return false;
    }

    public static List<ServerEntry> getFeaturedServersToAdd(List<String> existingServers) {
        Set<String> existingDomains = new HashSet<>();
        for (String existing : existingServers) {
            String normalized = normalizeDomain(existing);
            if (!normalized.isEmpty()) {
                existingDomains.add(normalized);
            }
        }

        List<ServerEntry> pending = new ArrayList<>();
        for (ServerEntry entry : featuredServers.values()) {
            String normalizedAddress = normalizeDomain(entry.address());
            if (!normalizedAddress.isEmpty() && !existingDomains.contains(normalizedAddress)) {
                pending.add(entry);
            }
        }
        return pending;
    }

    private static void addDomainPattern(String pattern) {
        if (pattern == null) return;

        String normalized = pattern.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) return;

        if (normalized.startsWith("*.")) {
            String wildcardBase = normalizeDomain(normalized.substring(2));
            if (!wildcardBase.isEmpty()) {
                wildcardDomains.add(wildcardBase);
            }
            return;
        }

        String exact = normalizeDomain(normalized);
        if (!exact.isEmpty()) {
            exactDomains.add(exact);
        }
    }

    private static void addFeaturedServer(String name, String address) {
        if (address == null || name == null) return;

        String cleanName = name.trim();
        String cleanAddress = normalizeDomain(address);
        if (cleanName.isEmpty() || cleanAddress.isEmpty()) return;

        featuredServers.putIfAbsent(cleanAddress, new ServerEntry(cleanName, cleanAddress));
        addDomainPattern(cleanAddress);
    }

    private static String normalizeDomain(String value) {
        if (value == null) return "";

        String domain = value.trim().toLowerCase(Locale.ROOT);
        if (domain.isEmpty()) return "";

        if (domain.startsWith("*.")) {
            domain = domain.substring(2);
        }

        if (domain.startsWith("[")) {
            int closingBracket = domain.indexOf(']');
            if (closingBracket > 0) {
                domain = domain.substring(1, closingBracket);
            }
            return domain;
        }

        int firstColon = domain.indexOf(':');
        int lastColon = domain.lastIndexOf(':');
        if (firstColon != -1 && firstColon == lastColon) {
            domain = domain.substring(0, firstColon);
        }

        if (domain.endsWith(".")) {
            domain = domain.substring(0, domain.length() - 1);
        }

        return domain;
    }
}
