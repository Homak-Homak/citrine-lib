package org.carpentry.citrine.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import org.carpentry.citrine.api.util.particles.ParticleProperties;

public class CitrineParticleEffect implements ParticleEffect {
    private final ParticleType<CitrineParticleEffect> type;
    private final ParticleProperties properties;

    public CitrineParticleEffect(ParticleType<CitrineParticleEffect> type, ParticleProperties properties) {
        this.type = type;
        this.properties = properties;
    }

    public ParticleProperties props() { return properties; }

    @Override public ParticleType<?> getType() { return type; }
    @Override public void write(PacketByteBuf buf) { properties.write(buf); }
    @Override public String asString() { return String.valueOf(Registries.PARTICLE_TYPE.getId(type)); }

    @SuppressWarnings("deprecation")
    public static final ParticleEffect.Factory<CitrineParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
        @Override
        public CitrineParticleEffect read(ParticleType<CitrineParticleEffect> type, StringReader reader) {
            return new CitrineParticleEffect(type, ParticleProperties.DEFAULT); // /particle command uses defaults
        }

        @Override
        public CitrineParticleEffect read(ParticleType<CitrineParticleEffect> type, PacketByteBuf buf) {
            return new CitrineParticleEffect(type, ParticleProperties.read(buf));
        }
    };

    public static Codec<CitrineParticleEffect> codec(ParticleType<CitrineParticleEffect> type) {
        return ParticleProperties.CODEC.xmap(p -> new CitrineParticleEffect(type, p), CitrineParticleEffect::props);
    }
}
