package org.carpentry.citrine.api.supporter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import net.fabricmc.loader.api.FabricLoader;

public final class SupporterList {
    private static final Gson GSON = new Gson();

    private final String id;
    private final URI uri;
    private final Path cachePath;

    private final AtomicReference<Set<UUID>> supporters =
            new AtomicReference<>(Collections.emptySet());

    public SupporterList(String id, URI uri) {
        this.id = id;
        this.uri = uri;

        this.cachePath = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("citrine")
                .resolve("supporters")
                .resolve(id + ".json");

        loadCache();
    }

    public String getId() {
        return id;
    }

    public URI getUri() {
        return uri;
    }

    /**
     * Checks whether a UUID is in this list
     */
    public boolean isSupporter(UUID uuid) {
        return uuid != null && supporters.get().contains(uuid);
    }

    /**
     * Returns an immutable snapshot of the current supporter UUIDs
     */
    public Set<UUID> getSupporters() {
        return supporters.get();
    }

    /**
     * Requests an immediate refresh
     * @return true if the remote list was fetched and applied successfully
     */
    public java.util.concurrent.CompletableFuture<Boolean> refresh() {
        return SupporterRefreshManager.refresh(this);
    }

    void replaceSupporters(Set<UUID> updated) {
        Set<UUID> immutable = Collections.unmodifiableSet(
                new HashSet<>(updated)
        );

        // Replace the whole set
        supporters.set(immutable);
        saveCache(immutable);
    }

    private void loadCache() {
        if (!Files.isRegularFile(cachePath)) {
            return;
        }

        try {
            String json = Files.readString(
                    cachePath,
                    StandardCharsets.UTF_8
            );

            Set<UUID> loaded = parseSupporters(json);
            supporters.set(Collections.unmodifiableSet(loaded));
        } catch (Exception exception) {
            System.err.println(
                    "[Citrine] Could not load supporter cache for "
                            + id + ": " + exception.getMessage()
            );
        }
    }

    private void saveCache(Set<UUID> uuids) {
        try {
            Files.createDirectories(cachePath.getParent());

            JsonObject root = new JsonObject();
            root.addProperty("version", 1);

            JsonArray array = new JsonArray();
            for (UUID uuid : uuids) {
                array.add(uuid.toString());
            }

            root.add("supporters", array);

            Files.writeString(
                    cachePath,
                    GSON.toJson(root),
                    StandardCharsets.UTF_8
            );
        } catch (IOException exception) {
            System.err.println(
                    "[Citrine] Could not save supporter cache for "
                            + id + ": " + exception.getMessage()
            );
        }
    }

    static Set<UUID> parseSupporters(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        if (!root.has("version")
                || root.get("version").getAsInt() != 1
                || !root.has("supporters")
                || !root.get("supporters").isJsonArray()) {
            throw new IllegalArgumentException(
                    "Invalid supporter list format."
            );
        }

        Set<UUID> parsed = new HashSet<>();

        for (var element : root.getAsJsonArray("supporters")) {
            if (!element.isJsonPrimitive()
                    || !element.getAsJsonPrimitive().isString()) {
                throw new IllegalArgumentException(
                        "Every supporter entry must be a UUID string."
                );
            }

            parsed.add(UUID.fromString(element.getAsString()));
        }

        return parsed;
    }
}
// more black magic fml (Look at SupporterUtils.java)