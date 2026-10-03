package org.carpentry.citrine.dev;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.carpentry.citrine.api.effect.EffectClearResult;
import org.carpentry.citrine.api.effect.UnclearableStatusEffect;

public class UnclearableEffect extends StatusEffect implements UnclearableStatusEffect {
    protected UnclearableEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public EffectClearResult tryClear(LivingEntity applied, StatusEffectInstance cleared) {
        return new EffectClearResult(new StatusEffectInstance(cleared.getEffectType(), 80, cleared.getAmplifier()), false);
    }
}
