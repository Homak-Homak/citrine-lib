package org.carpentry.citrine.api.util.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.particle.CitrineParticleEffect;

public final class CitrineParticles {
    private CitrineParticles() {}

    // Mods call this once per texture set they want.
    public static ParticleType<CitrineParticleEffect> register(Identifier id) {
        ParticleType<CitrineParticleEffect> type = FabricParticleTypes.complex(
                true, CitrineParticleEffect.FACTORY);
        return Registry.register(Registries.PARTICLE_TYPE, id, type);
    }

    public static ParticleType<CitrineParticleEffect> GENERIC;
    public static ParticleType<CitrineParticleEffect> GENERIC_ALT;
    public static ParticleType<CitrineParticleEffect> SQUARE;
    public static ParticleType<CitrineParticleEffect> DUST;
    public static ParticleType<CitrineParticleEffect> WISP;

    public static void init() {
        GENERIC = register(Citrine.id("generic"));
        GENERIC_ALT = register(Citrine.id("generic_alt"));
        SQUARE = register(Citrine.id("square"));
        DUST = register(Citrine.id("dust"));
        WISP = register(Citrine.id("wisp"));
    }
}
