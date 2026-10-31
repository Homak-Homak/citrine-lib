package org.carpentry.citrine.api.client.flash;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.carpentry.citrine.CitrineClient;
import org.carpentry.citrine.api.util.EasingType;

import java.awt.*;
import java.util.Collection;

public class NetworkedScreenFlashManager {
    public static void sendFlash(ServerPlayerEntity player, float ticks, Color color, EasingType type) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt((int) ticks);
        buf.writeInt(color.getRGB());
        buf.writeInt(type.ordinal());
        ServerPlayNetworking.send(player, CitrineClient.FLASH_COL_PACKET, buf);
    }

    public static void sendFlash(ServerPlayerEntity player, float ticks, Identifier img, EasingType type) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt((int) ticks);
        buf.writeIdentifier(img);
        buf.writeInt(type.ordinal());
        ServerPlayNetworking.send(player, CitrineClient.FLASH_IMG_PACKET, buf);
    }

    public static void sendFlash(Collection<? extends PlayerEntity> players, float ticks, Color color, EasingType type) {
        for (PlayerEntity p : players) {
            if (p instanceof ServerPlayerEntity sp) sendFlash(sp, ticks, color, type);
        }
    }

    public static void sendFlash(Collection<? extends PlayerEntity> players, float ticks, Identifier img, EasingType type) {
        for (PlayerEntity p : players) {
            if (p instanceof ServerPlayerEntity sp) sendFlash(sp, ticks, img, type);
        }
    }
}
