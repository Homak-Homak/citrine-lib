package org.carpentry.citrine.api.util.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;

public record ParticleProperties(
        int lifetime,
        float startScale, float endScale,
        int startColor, int endColor,       // 0xRRGGBB
        float startAlpha, float endAlpha,
        float rotation, float spin,         // radians, radians per tick
        float gravity, float drag,
        boolean fullBright, boolean collision
) {
    public static final ParticleProperties DEFAULT = new ParticleProperties(
            20, 0.1f, 0.1f, 0xFFFFFF, 0xFFFFFF, 1f, 1f, 0f, 0f, 0f, 0.98f, false, true);

    public static final Codec<ParticleProperties> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("lifetime").forGetter(ParticleProperties::lifetime),
            Codec.FLOAT.fieldOf("start_scale").forGetter(ParticleProperties::startScale),
            Codec.FLOAT.fieldOf("end_scale").forGetter(ParticleProperties::endScale),
            Codec.INT.fieldOf("start_color").forGetter(ParticleProperties::startColor),
            Codec.INT.fieldOf("end_color").forGetter(ParticleProperties::endColor),
            Codec.FLOAT.fieldOf("start_alpha").forGetter(ParticleProperties::startAlpha),
            Codec.FLOAT.fieldOf("end_alpha").forGetter(ParticleProperties::endAlpha),
            Codec.FLOAT.fieldOf("rotation").forGetter(ParticleProperties::rotation),
            Codec.FLOAT.fieldOf("spin").forGetter(ParticleProperties::spin),
            Codec.FLOAT.fieldOf("gravity").forGetter(ParticleProperties::gravity),
            Codec.FLOAT.fieldOf("drag").forGetter(ParticleProperties::drag),
            Codec.BOOL.fieldOf("full_bright").forGetter(ParticleProperties::fullBright),
            Codec.BOOL.fieldOf("collision").forGetter(ParticleProperties::collision)
    ).apply(i, ParticleProperties::new));

    public void write(PacketByteBuf buf) {
        buf.writeVarInt(lifetime);
        buf.writeFloat(startScale); buf.writeFloat(endScale);
        buf.writeInt(startColor);   buf.writeInt(endColor);
        buf.writeFloat(startAlpha); buf.writeFloat(endAlpha);
        buf.writeFloat(rotation);   buf.writeFloat(spin);
        buf.writeFloat(gravity);    buf.writeFloat(drag);
        buf.writeBoolean(fullBright); buf.writeBoolean(collision);
    }

    public static ParticleProperties read(PacketByteBuf buf) {
        return new ParticleProperties(
                buf.readVarInt(),
                buf.readFloat(), buf.readFloat(),
                buf.readInt(), buf.readInt(),
                buf.readFloat(), buf.readFloat(),
                buf.readFloat(), buf.readFloat(),
                buf.readFloat(), buf.readFloat(),
                buf.readBoolean(), buf.readBoolean());
    }
}
