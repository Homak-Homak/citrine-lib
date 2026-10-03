package org.carpentry.citrine.api.effect;

import net.minecraft.entity.effect.StatusEffectInstance;

public record EffectClearResult(StatusEffectInstance effect, boolean doClear) {
}
