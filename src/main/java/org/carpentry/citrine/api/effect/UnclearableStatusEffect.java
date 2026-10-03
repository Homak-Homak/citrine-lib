package org.carpentry.citrine.api.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public interface UnclearableStatusEffect {
    // returns if the effect should be cleared on this call
    EffectClearResult tryClear(LivingEntity applied, StatusEffectInstance cleared);
}
