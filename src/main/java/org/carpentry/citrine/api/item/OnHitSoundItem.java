package org.carpentry.citrine.api.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public interface OnHitSoundItem {
    // this could probably be rewritten im ngl i js used old code 😭
    SoundEvent getHitSound(World world, ItemStack stack, LivingEntity attacker, LivingEntity target);

    default float getPitch(ItemStack stack) {
        return 1.0f;
    }
    default float getVolume(ItemStack stack) {
        return 1.0f;
    }
}
