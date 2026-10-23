package org.carpentry.citrine.api.util;

import org.carpentry.citrine.api.supporter.SupporterList;
import org.carpentry.citrine.api.supporter.SupporterRefreshManager;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

public final class SupporterUtils {
    private SupporterUtils() {}

    /**
     * Creates a remotely updated supporter list.
     *
     * @param id unique ID for this list, e.g. "examplemod"
     * @param uri HTTPS URL returning the supporter JSON
     * @param refreshInterval how often to check for updates
     */
    public static SupporterList createRemoteList(
            String id,
            URI uri,
            Duration refreshInterval
    ) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(uri, "uri");
        Objects.requireNonNull(refreshInterval, "refreshInterval");

        if (!id.matches("[a-zA-Z0-9_-]+")) {
            throw new IllegalArgumentException("Invalid supporter list ID: " + id);
        }

        if (!"https".equalsIgnoreCase(uri.getScheme())
                || uri.getHost() == null
                || uri.getUserInfo() != null) {
            throw new IllegalArgumentException(
                    "Supporter list URLs must use HTTPS and have a valid host."
            );
        }

        if (refreshInterval.compareTo(Duration.ofMinutes(1)) < 0) {
            throw new IllegalArgumentException(
                    "Refresh interval must be at least one minute."
            );
        }

        SupporterList list = new SupporterList(id, uri);

        SupporterRefreshManager.register(list, refreshInterval);
        return list;
    }

    /**
     * Stops automatic refreshes and shuts down the refresh executor.
     * Call during mod/server shutdown.
     */
    public static void shutdown() {
        SupporterRefreshManager.shutdown();
    }
}
// Actual black magic please let me never do this shit again
// I genuinely spent all day looking up how shit works just so I could do this :sob: