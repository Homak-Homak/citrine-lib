package org.carpentry.citrine.dev;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.carpentry.citrine.Citrine;

public class CitrineEffects {
    public static final StatusEffect UNCLEARABLE = registerEffect("unclearable", new UnclearableEffect(StatusEffectCategory.NEUTRAL, 0xffffffff));

    private static StatusEffect registerEffect(String name, StatusEffect eff) {
        return Registry.register(Registries.STATUS_EFFECT, Citrine.id(name), eff);
    }

    public static void init() {}
}
