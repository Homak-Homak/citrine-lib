package org.carpentry.citrine.dev;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.carpentry.citrine.api.client.shader.CitrineShaderQuad;

public class TestShaderCallback implements HudRenderCallback {
    private static final CitrineShaderQuad SHADER = new CitrineShaderQuad(CitrineShaders.MAGIC_SHADER);
    private static int frames;

    @Override
    public void onHudRender(DrawContext ctx, float tickDelta) {
        if (MinecraftClient.getInstance().player.getMainHandStack().isOf(CitrineItems.SHADER)) {
            SHADER.draw(ctx, ctx.getScaledWindowWidth() / 2f - 250, ctx.getScaledWindowHeight() / 2f - 250, 500, 500);
        }
    }
}
