package org.carpentry.citrine.dev.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.client.flash.NetworkedScreenFlashManager;
import org.carpentry.citrine.api.util.EasingType;
import org.carpentry.citrine.api.client.flash.ScreenFlashManager;
import org.carpentry.citrine.api.cooldown.StackCooldownManager;
import org.carpentry.citrine.api.item.OnCritItem;
import org.carpentry.citrine.api.item.ShieldBreakerItem;
import org.carpentry.citrine.api.util.TimeUtils;

import java.util.stream.Collectors;

public class StackCooldownItem extends SwordItem implements OnCritItem, ShieldBreakerItem{
    public StackCooldownItem(Settings settings) {
        super(ToolMaterials.DIAMOND, 3, -2.5f, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        StackCooldownManager.setCooldown(user.getWorld(), stack, TimeUtils.seconds(2));
        user.sendMessage(Text.literal("Augh"), true);

        if (world.isClient) {
            // ScreenFlashManager.addFlash(TimeUtils.seconds(6.7f), new Color(0xff004f), EasingType.LINEAR);
            //ScreenFlashManager.addFlash(40, Citrine.id("textures/test/gothamchess_whythefucknot.png"), EasingType.QUAD);
        }
        if (!world.isClient) {
            NetworkedScreenFlashManager.sendFlash(world.getPlayers().stream().collect(Collectors.toUnmodifiableList()), 40, Citrine.id("textures/test/gothamchess_whythefucknot.png"), EasingType.QUAD);
        }
        return TypedActionResult.consume(stack);
    }

    @Override
    public void onCrit(ItemStack stack, LivingEntity attacker, LivingEntity target) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, TimeUtils.minutes(3)));
        if (attacker instanceof PlayerEntity p)
            p.sendMessage(Text.literal("Crit!"), true);
    }

    @Override
    public int getShieldCooldown() {
        return TimeUtils.seconds(10);
    }

}