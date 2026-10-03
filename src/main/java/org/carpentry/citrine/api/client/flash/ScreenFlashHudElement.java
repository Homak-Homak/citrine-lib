package org.carpentry.citrine.api.client.flash;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ScreenFlashHudElement implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient c = MinecraftClient.getInstance();
        if (c.world == null) return;

        int width = c.getWindow().getScaledWidth();
        int height = c.getWindow().getScaledHeight();

        float currentTime = c.world.getTime() + tickDelta;

        ScreenFlashManager.clearExpired(currentTime);

        for (ScreenFlash flash : ScreenFlashManager.getActiveFlashes()) {
            float elapsed = currentTime - flash.startTime();
            if (elapsed < 0) continue;

            float progress = Math.min(1.0f, elapsed / flash.ticks());

            float alpha = 1.0f - flash.type().getEased(progress);

            drawContext.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

            if (flash.img() != null && flash.color() == null) {
                drawContext.drawTexture(flash.img(), 0, 0, 0, 0, width, height, width, height);
            } else if (flash.color() != null && flash.img() == null) {
                drawContext.fill(0, 0, width, height, flash.color().getRGB());
            }
        }

        drawContext.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}