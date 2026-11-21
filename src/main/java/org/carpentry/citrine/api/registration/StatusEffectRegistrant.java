package org.carpentry.citrine.api.registration;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import org.carpentry.citrine.api.registration.base.RegistrantBase;

public class StatusEffectRegistrant extends RegistrantBase<StatusEffect> {

    public StatusEffectRegistrant(String modId) {
        super(modId, Registries.STATUS_EFFECT);
    }

    public RegistryEntry<StatusEffect> registerRef(String name, StatusEffect effect) {
        this.toRegister.add(effect);
        return Registry.registerReference(Registries.STATUS_EFFECT, this.id(name), effect);
    }

    public StatusEffect register(String name, StatusEffect object) {
        return (StatusEffect)this.registerRef(name, object).value();
    }
}
