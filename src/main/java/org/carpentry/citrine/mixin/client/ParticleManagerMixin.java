package org.carpentry.citrine.mixin.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.carpentry.citrine.particle.CitrineParticleSheets;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Mutable
    @Final
    @Shadow
    private static List<ParticleTextureSheet> PARTICLE_TEXTURE_SHEETS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void citrine$addSheets(CallbackInfo ci) {
        PARTICLE_TEXTURE_SHEETS = ImmutableList.<ParticleTextureSheet>builder()
                .addAll(PARTICLE_TEXTURE_SHEETS)
                .add(CitrineParticleSheets.TRANSLUCENT_NO_DEPTH)
                .add(CitrineParticleSheets.TRANSLUCENT_NO_DEPTH)
                .add(CitrineParticleSheets.ADDITIVE)
                .build();
    }
}
