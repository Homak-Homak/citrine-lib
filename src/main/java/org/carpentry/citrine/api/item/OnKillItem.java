package org.carpentry.citrine.api.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface OnKillItem {
    // executes when an entity is killed using this item, returns if the entity should die or stay alive
    boolean onKill(ItemStack stack, LivingEntity attacker, LivingEntity target);
}
