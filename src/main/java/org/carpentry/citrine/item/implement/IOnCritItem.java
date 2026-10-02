package org.carpentry.citrine.item.implement;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IOnCritItem {
    // this runs when the implementing item is used to crit
    void onCrit(ItemStack stack, LivingEntity attacker, LivingEntity target);
}
