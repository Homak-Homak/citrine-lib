package org.carpentry.citrine.api.client.flash;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScreenFlashManager {
    private static final List<ScreenFlash> activeFlashes = new CopyOnWriteArrayList<>();

    public static void addFlash(float ticks, Color color, FlashEasingType type) {
        MinecraftClient c = MinecraftClient.getInstance();
        if (c.world == null) return;

        float startTime = c.world.getTime() + c.getTickDelta();
        ScreenFlash flash = new ScreenFlash(ticks, startTime, color, null, type);
        activeFlashes.add(flash);
    }

    public static void addFlash(float ticks, Identifier img, FlashEasingType type) {
        MinecraftClient c = MinecraftClient.getInstance();
        if (c.world == null) return;

        float startTime = c.world.getTime() + c.getTickDelta();
        ScreenFlash flash = new ScreenFlash(ticks, startTime, null, img, type);
        activeFlashes.add(flash);
    }

    public static List<ScreenFlash> getActiveFlashes() {
        return activeFlashes;
    }

    public static void removeFlash(ScreenFlash flash) {
        activeFlashes.remove(flash);
    }

    public static void clearExpired(float currentTime) {
        activeFlashes.removeIf(flash -> (currentTime - flash.startTime()) >= flash.ticks());
    }
}