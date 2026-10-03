package org.carpentry.citrine.api.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface OnUserDeathItem {
    // called when the player holding this item dies, returns if the holder should get killed or stay alive
    boolean onDeath(ItemStack stack, LivingEntity dead);
}
