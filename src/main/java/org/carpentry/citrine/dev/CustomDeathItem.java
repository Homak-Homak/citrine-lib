package org.carpentry.citrine.dev;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.carpentry.citrine.api.item.OnUserDeathItem;

public class CustomDeathItem extends Item implements OnUserDeathItem {
    public CustomDeathItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onDeath(ItemStack stack, LivingEntity target) {
        if (target instanceof PlayerEntity p) p.sendMessage(Text.literal("This is a death action!"));
        return false;
    }
}
