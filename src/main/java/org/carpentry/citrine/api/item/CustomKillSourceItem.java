package org.carpentry.citrine.api.item;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface CustomKillSourceItem {
    DamageSource getKillSource(PlayerEntity player, ItemStack stack);
}
