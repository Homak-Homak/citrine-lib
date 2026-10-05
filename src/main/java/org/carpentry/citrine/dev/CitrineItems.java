package org.carpentry.citrine.dev;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.dev.item.*;

public class CitrineItems {
    public static final Item CUSTOM_CRIT = registerItem("crit", new CustomCritItem(new Item.Settings()));
    public static final Item CUSTOM_KILL = registerItem("kill", new CustomKillItem(new Item.Settings()));
    public static final Item CUSTOM_DEATH = registerItem("death", new CustomDeathItem(new Item.Settings()));
    public static final Item STACK_COOLDOWN = registerItem("stack_cooldown", new StackCooldownItem(new Item.Settings()));
    public static final Item USE_SPRINT = registerItem("use_sprint", new UseWhileSprintItem(new Item.Settings()));
    public static final Item SKINS = registerItem("skins", new SkinsItem(new Item.Settings()));
    public static final Item MANY_MODELS = registerItem("models", new ManyModelsItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Citrine.id(name), item);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((entries -> {
            entries.add(CUSTOM_CRIT);
            entries.add(CUSTOM_KILL);
            entries.add(CUSTOM_DEATH);
            entries.add(STACK_COOLDOWN);
            entries.add(USE_SPRINT);
            entries.add(SKINS);
            entries.add(MANY_MODELS);
        }));
    }
}
