package org.carpentry.citrine.dev;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.carpentry.citrine.item.implement.IOnCritItem;

public class CustomCritItem extends Item implements IOnCritItem {
    public CustomCritItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onCrit(ItemStack stack, LivingEntity attacker, LivingEntity target) {
        if (attacker instanceof PlayerEntity p) p.sendMessage(Text.literal("This is a crit action!"));
    }
}
