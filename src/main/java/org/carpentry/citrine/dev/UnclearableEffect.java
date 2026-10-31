package org.carpentry.citrine.dev;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.carpentry.citrine.api.effect.UnclearableStatusEffect;

public class UnclearableEffect extends StatusEffect implements UnclearableStatusEffect {
    protected UnclearableEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}
