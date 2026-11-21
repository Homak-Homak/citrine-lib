package org.carpentry.citrine.api.registration;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import org.carpentry.citrine.api.registration.base.RegistrantBase;

import java.util.function.Function;

public class ItemRegistrant extends RegistrantBase<Item> {
    public ItemRegistrant(String modId) {
        super(modId, Registries.ITEM);
    }

    public Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item)factory.apply(settings);
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return (Item)this.register(name, item);
    }

    public Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings, RegistryKey<ItemGroup>... groups) {
        Item item = this.register(name, factory, settings);

        for(RegistryKey<ItemGroup> group : groups) {
            ItemGroupEvents.modifyEntriesEvent(group).register((ItemGroupEvents.ModifyEntries)(b) -> b.add(item));
        }

        return item;
    }
}
