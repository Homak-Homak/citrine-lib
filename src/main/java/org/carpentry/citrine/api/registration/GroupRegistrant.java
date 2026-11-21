package org.carpentry.citrine.api.registration;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.carpentry.citrine.api.registration.base.RegistrantBase;

public class GroupRegistrant extends RegistrantBase<ItemGroup> {
    public GroupRegistrant(String modId) {
        super(modId, Registries.ITEM_GROUP);
    }

    public ItemGroup register(String name, ItemGroup itemGroup, ItemGroupEvents.ModifyEntries entries) {
        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, this.id(name))).register(entries);
        return (ItemGroup)super.register(name, itemGroup);
    }
}
