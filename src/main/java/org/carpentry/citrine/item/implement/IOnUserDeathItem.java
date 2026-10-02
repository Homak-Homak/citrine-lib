package org.carpentry.citrine.item.implement;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IOnUserDeathItem {
    // called when the player holding this item dies, returns if the holder should get killed or stay alive
    boolean onDeath(ItemStack stack, LivingEntity dead);
}
