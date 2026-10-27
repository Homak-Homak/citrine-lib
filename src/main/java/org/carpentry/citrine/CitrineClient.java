package org.carpentry.citrine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.carpentry.citrine.api.client.flash.ScreenFlashHudElement;
import org.carpentry.citrine.api.render.CubeRenderer;
import org.carpentry.citrine.api.render.QuadRenderer;

public class CitrineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ScreenFlashHudElement());
        CubeRenderer.init();
        QuadRenderer.init();
    }
}
