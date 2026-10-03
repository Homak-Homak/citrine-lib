package org.carpentry.citrine.dev;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.carpentry.citrine.api.item.OnKillItem;

public class CustomKillItem extends Item implements OnKillItem {
    public CustomKillItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onKill(ItemStack stack, LivingEntity attacker, LivingEntity target) {
        if (attacker instanceof PlayerEntity p) p.sendMessage(Text.literal("This is a kill action!"));
        return false;
    }
}
