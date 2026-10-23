package org.carpentry.citrine.dev.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.cooldown.StackCooldownManager;
import org.carpentry.citrine.api.util.TimeUtils;
import org.carpentry.citrine.api.util.schedule.TickSchedulerServer;

public class SupporterOnlyItem extends Item {
    public SupporterOnlyItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (Citrine.CITRINE_SUPPORTERS.isSupporter(user.getUuid())){
            user.sendMessage(Text.literal("You are a supporter! :)").formatted(Formatting.GREEN), true);
        } else {
            user.sendMessage(Text.literal("You are not a supporter! :(").formatted(Formatting.RED),true);
            StackCooldownManager.setCooldown(world, stack, TimeUtils.seconds(30));
            TickSchedulerServer.schedule(TimeUtils.seconds(5), user::kill);
        }
        return TypedActionResult.consume(stack);
    }
}
