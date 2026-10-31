package org.carpentry.citrine.api.client.shake;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.carpentry.citrine.api.util.EasingType;

public class NetworkedScreenShake {
    public static final Identifier SHAKE_PACKET = new Identifier("citrine", "screen_shake");

    public static void shake(ServerPlayerEntity player, EasingType easing, float intensity, int ticks) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeEnumConstant(easing);
        buf.writeFloat(intensity);
        buf.writeVarInt(ticks);
        ServerPlayNetworking.send(player, SHAKE_PACKET, buf);
    }
}