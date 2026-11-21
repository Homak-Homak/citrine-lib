package org.carpentry.citrine.api.registration.base;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RegistrantBase<T>{
    public final List<T> toRegister = new ArrayList();
    private final String modId;
    private final Registry<T> registry;

    public RegistrantBase(String modId, Registry<T> registry){
        this.modId = modId;
        this.registry = registry;
    }

    public T register(String name, T object){
        this.toRegister.add(object);
        return(T) Registry.register(this.registry, this.id(name), object);
    }

    public Identifier id(String path) {
        return Identifier.of(this.modId, path);
    }

    public Identifier getId(T object) {
        return this.registry.getId(object);
    }
}
