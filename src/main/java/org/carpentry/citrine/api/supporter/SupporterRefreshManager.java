package org.carpentry.citrine.api.supporter;

import java.io.InputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class SupporterRefreshManager {
    private static final int MAX_RESPONSE_BYTES = 1_048_576; // 1 MiB

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

    private static final ScheduledExecutorService EXECUTOR =
            Executors.newScheduledThreadPool(2, runnable -> {
                Thread thread = new Thread(
                        runnable,
                        "Citrine-Supporter-Refresh"
                );
                thread.setDaemon(true);
                return thread;
            });

    private static final Map<String, SupporterList> LISTS =
            new ConcurrentHashMap<>();

    private static final Map<String, ScheduledFuture<?>> TASKS =
            new ConcurrentHashMap<>();

    private SupporterRefreshManager() {}

    public static void register(
            SupporterList list,
            Duration refreshInterval
    ) {
        SupporterList existing = LISTS.putIfAbsent(list.getId(), list);

        if (existing != null) {
            throw new IllegalArgumentException(
                    "A supporter list with ID '" + list.getId()
                            + "' is already registered."
            );
        }

        long intervalMillis = refreshInterval.toMillis();

        ScheduledFuture<?> task = EXECUTOR.scheduleWithFixedDelay(
                () -> refresh(list),
                0,
                intervalMillis,
                TimeUnit.MILLISECONDS
        );

        TASKS.put(list.getId(), task);
    }

    static CompletableFuture<Boolean> refresh(SupporterList list) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder(list.getUri())
                        .timeout(Duration.ofSeconds(15))
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<InputStream> response = HTTP_CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofInputStream()
                );

                try (InputStream body = response.body()) {
                    if (response.statusCode() != 200) {
                        throw new IOException(
                                "HTTP status " + response.statusCode()
                        );
                    }

                    byte[] bytes = body.readNBytes(
                            MAX_RESPONSE_BYTES + 1
                    );

                    if (bytes.length > MAX_RESPONSE_BYTES) {
                        throw new IOException(
                                "Supporter response exceeds 1 MiB."
                        );
                    }

                    String json = new String(
                            bytes,
                            java.nio.charset.StandardCharsets.UTF_8
                    );

                    Set<UUID> updated =
                            SupporterList.parseSupporters(json);

                    list.replaceSupporters(updated);
                    return true;
                }
            } catch (Exception exception) {
                System.err.println(
                        "[Citrine] Failed to refresh supporter list '"
                                + list.getId() + "': "
                                + exception.getMessage()
                );

                // Preserve the last known-good list.
                return false;
            }
        }, EXECUTOR);
    }

    public static void shutdown() {
        for (ScheduledFuture<?> task : TASKS.values()) {
            task.cancel(false);
        }

        TASKS.clear();
        LISTS.clear();
        EXECUTOR.shutdownNow();
    }
}
// Black magic^3