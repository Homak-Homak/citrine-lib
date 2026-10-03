package org.carpentry.citrine.dev;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.client.flash.FlashEasingType;
import org.carpentry.citrine.api.client.flash.ScreenFlashManager;
import org.carpentry.citrine.api.cooldown.StackCooldownManager;
import org.carpentry.citrine.api.item.OnCritItem;
import org.carpentry.citrine.api.item.ShieldBreakerItem;
import org.carpentry.citrine.api.util.TimeUtils;

import java.awt.*;

public class StackCooldownItem extends SwordItem implements OnCritItem, ShieldBreakerItem {
    public StackCooldownItem(Settings settings) {
        super(ToolMaterials.DIAMOND, 3, -2.5f, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        StackCooldownManager.setCooldown(user.getWorld(), stack, 40);
        user.sendMessage(Text.literal("Augh"), true);

        if (world.isClient) {
            //ScreenFlashManager.addFlash(40, new Color(255, 255, 255, 255), FlashEasingType.LINEAR);
            ScreenFlashManager.addFlash(40, Citrine.id("textures/test/gothamchess_whythefucknot.png"), FlashEasingType.QUAD);
        }

        return TypedActionResult.consume(stack);
    }

    @Override
    public void onCrit(ItemStack stack, LivingEntity attacker, LivingEntity target) {
        if (attacker instanceof PlayerEntity p)
            p.sendMessage(Text.literal("Crit!"), true);
    }

    @Override
    public int getShieldCooldown() {
        return TimeUtils.seconds(20);
    }
}