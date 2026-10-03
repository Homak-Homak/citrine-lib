package org.carpentry.citrine.api.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface OnCritItem {
    // this runs when the implementing item is used to crit
    void onCrit(ItemStack stack, LivingEntity attacker, LivingEntity target);
}
