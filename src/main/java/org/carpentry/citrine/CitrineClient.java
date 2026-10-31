package org.carpentry.citrine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;
import org.carpentry.citrine.api.client.flash.ScreenFlashHudElement;
import org.carpentry.citrine.api.client.flash.ScreenFlashManager;
import org.carpentry.citrine.api.client.shake.ScreenShakeInstance;
import org.carpentry.citrine.api.client.shake.ScreenShakeManager;
import org.carpentry.citrine.api.render.CubeRenderer;
import org.carpentry.citrine.api.render.QuadRenderer;
import org.carpentry.citrine.api.util.EasingType;
import org.carpentry.citrine.api.util.particles.CitrineParticles;
import org.carpentry.citrine.particle.CitrineParticle;

import java.awt.*;

import static org.carpentry.citrine.api.client.shake.NetworkedScreenShake.SHAKE_PACKET;

public class CitrineClient implements ClientModInitializer {
    public static final Identifier FLASH_IMG_PACKET = Citrine.id("flashi");
    public static final Identifier FLASH_COL_PACKET = Citrine.id("flashc");
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ScreenFlashHudElement());
        ClientPlayNetworking.registerGlobalReceiver(FLASH_IMG_PACKET, (client, handler, buf, responseSender) -> {
            int ticks = buf.readInt();
            Identifier img = buf.readIdentifier();
            int easing = buf.readInt();
            client.execute(() -> {
                EasingType type = EasingType.values()[easing - 1];
                ScreenFlashManager.addFlash((float) ticks, img, type);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(FLASH_COL_PACKET, (client, handler, buf, responseSender) -> {
            int ticks = buf.readInt();
            int color = buf.readInt();
            int easing = buf.readInt();
            client.execute(() -> {
                EasingType type = EasingType.values()[easing - 1];
                ScreenFlashManager.addFlash((float) ticks, new Color(color), type);
            });
        });
        CubeRenderer.init();
        QuadRenderer.init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) ScreenShakeManager.tick();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ScreenShakeManager.clear());
        ClientPlayNetworking.registerGlobalReceiver(SHAKE_PACKET, (client, handler, buf, sender) -> {
            EasingType easing = buf.readEnumConstant(EasingType.class);
            float intensity = buf.readFloat();
            int ticks = buf.readVarInt();
            client.execute(() -> ScreenShakeManager.add(new ScreenShakeInstance(easing, intensity, ticks)));
        });
        ParticleFactoryRegistry.getInstance().register(CitrineParticles.GENERIC, CitrineParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CitrineParticles.GENERIC_ALT, CitrineParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CitrineParticles.SQUARE, CitrineParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CitrineParticles.DUST, CitrineParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CitrineParticles.WISP, CitrineParticle.Factory::new);
    }
}
