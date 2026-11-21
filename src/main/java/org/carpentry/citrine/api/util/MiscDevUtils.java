package org.carpentry.citrine.api.util;

import net.fabricmc.loader.api.FabricLoader;

public class MiscDevUtils {
    /**
     * Returns whether or not the mod is in development environment.
     * @return whether or not the mod is in development environment
     */
    public static boolean isDev() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    /**
     * Returns whether or not a mod of a specified ID is loaded.
     * @param modId The mod id to check for.
     * @return whether or not the mod is loaded
     */
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
