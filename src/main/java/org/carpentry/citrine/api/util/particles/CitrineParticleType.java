package org.carpentry.citrine.api.util.particles;

import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public class CitrineParticleType extends ParticleType {
    protected CitrineParticleType(boolean alwaysShow, ParticleEffect.Factory parametersFactory) {
        super(alwaysShow, parametersFactory);
    }

    @Override
    public Codec getCodec() {
        return null;
    }
}
