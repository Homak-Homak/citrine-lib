package org.carpentry.citrine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.carpentry.citrine.api.client.flash.ScreenFlashHudElement;

public class CitrineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ScreenFlashHudElement());
    }
}
