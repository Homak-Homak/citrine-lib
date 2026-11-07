package org.carpentry.citrine.dev.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.client.shake.NetworkedScreenShake;
import org.carpentry.citrine.api.cooldown.StackCooldownManager;
import org.carpentry.citrine.api.util.EasingType;
import org.carpentry.citrine.api.util.TimeUtils;
import org.carpentry.citrine.api.util.particles.ParticleBuilder;
import org.carpentry.citrine.api.util.schedule.TickSchedulerServer;
import org.carpentry.citrine.api.util.particles.CitrineParticles;

import java.awt.*;

public class SupporterOnlyItem extends Item {
    public SupporterOnlyItem(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (Citrine.CITRINE_SUPPORTERS.isSupporter(user.getUuid())){
            user.sendMessage(Text.literal("You are a supporter! :)").formatted(Formatting.GREEN), true);
            if (!world.isClient){
                for (ServerPlayerEntity sPlayer : user.getWorld().getEntitiesByClass(ServerPlayerEntity.class,
                        user.getBoundingBox().expand(20),
                        ServerPlayerEntity::isAlive)){
                    ParticleBuilder.create(CitrineParticles.STAR)
                            .lifetime(TimeUtils.seconds(5))
                            .scale(1)
                            .color(Color.CYAN.getRGB(), Color.BLUE.getRGB())
                            .alpha(1f, 0f)
                            .spin(0.1f)
                            .gravity(0f)
                            .motion(0, 0.01, 0)
                            .randomMotion(0.2f)
                            .fullBright()
                            .spawn(world, user.getPos().x, user.getPos().y + user.getHeight()/2, user.getPos().z, 16);
                    // NetworkedScreenShake.shake(sPlayer, EasingType.LINEAR, 1, TimeUtils.seconds(2));
                }
            }
        } else {
            user.sendMessage(Text.literal("You are not a supporter! :(").formatted(Formatting.RED),true);
            StackCooldownManager.setCooldown(world, stack, TimeUtils.seconds(30));
            TickSchedulerServer.schedule(TimeUtils.seconds(5), user::kill);
        }
        return TypedActionResult.pass(stack);
    }
}