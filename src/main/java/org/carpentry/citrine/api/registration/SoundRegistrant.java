package org.carpentry.citrine.api.registration;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import org.carpentry.citrine.api.registration.base.RegistrantBase;

public class SoundRegistrant extends RegistrantBase<SoundEvent> {
    public SoundRegistrant(String modId) {
        super(modId, Registries.SOUND_EVENT);
    }

    public SoundEvent register(String name) {
        SoundEvent soundEvent = SoundEvent.of(this.id(name));
        return (SoundEvent)this.register(name, soundEvent);
    }
}
