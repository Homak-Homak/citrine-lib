package org.carpentry.citrine.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityUtils {
    /**
     * Returns a list of entities around the specified entity.
     * @param living The entity to calculate for.
     * @param expansion The diameter of the checked area.
     */
    public static List<Entity> getEntitiesAroundEntity(Entity living, int expansion) {
        return living.getWorld().getEntitiesByClass(Entity.class, new Box(living.getBlockPos()).expand(expansion), entity -> true);
    }

    /**
     * Returns a list of entities around the specified BlockPos.
     * @param pos The position to check at.
     * @param world The world to use.
     * @param expansion The diameter of the checked area.
     */
    // public static List<Entity> getEntitiesAroundPos(BlockPos pos, World world, int expansion) {
    //     return world.getEntitiesByClass(Entity.class, new Box(pos).expand(expansion), entity -> true);
    // }

    // import blockpos and world

    /**
     * Applies a riptide effect on a specified player in the same style Minecraft does it.
     * @param player The player to apply the riptide to.
     * @param strength The strength of the riptide.
     * @param riptideTicks The amount of ticks the riptide is active.
     */
    public static void applyRiptide(PlayerEntity player, float strength, int riptideTicks) {
        float yaw = player.getYaw();
        float pitch = player.getPitch();

        float h = -MathHelper.sin(yaw * ((float) Math.PI / 180f)) * MathHelper.cos(pitch * ((float) Math.PI / 180f));
        float k = -MathHelper.sin(pitch * ((float) Math.PI / 180f));
        float l = MathHelper.cos(yaw * ((float) Math.PI / 180f)) * MathHelper.cos(pitch * ((float) Math.PI / 180f));
        float m = MathHelper.sqrt(h * h + k * k + l * l);

        float n = 3.0f * ((1.0f + strength) / 4.0f);
        h *= n / m;
        k *= n / m;
        l *= n / m;
        player.addVelocity(h, k, l);
        player.useRiptide(riptideTicks);
        if (player.isOnGround()) {
            player.move(MovementType.SELF, new Vec3d(0.0f, 1.1999999f, 0.0f));
        }
    }

    /**
     * Spawns the sweep attack particles in the same style Minecraft does.
     * @param living The entity to spawn particles in front of.
     */
    public static void spawnSweepAttackParticles(LivingEntity living) {
        double d = -MathHelper.sin(living.getYaw() * ((float) Math.PI / 180f));
        double e = MathHelper.cos(living.getYaw() * ((float) Math.PI / 180f));

        if (living.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, living.getX() + d, living.getBodyY(0.5f), living.getZ() + e, 0, d, 0.0f, e, 0.0f);
        }
    }

    /**
     * Spawns a custom particle as a sweep attack.
     * @param living The entity to spawn particles in front of.
     * @param type The particle to spawn.
     * @param <T> The particle to spawn.
     */
    public static <T extends ParticleEffect> void spawnCustomSweepAttackParticles(LivingEntity living, T type) {
        double d = -MathHelper.sin(living.getYaw() * ((float) Math.PI / 180f));
        double e = MathHelper.cos(living.getYaw() * ((float) Math.PI / 180f));

        if (living.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(type, living.getX() + d, living.getBodyY(0.5f), living.getZ() + e, 0, d, 0.0f, e, 0.0f);
        }
    }

    /**
     * Applies a cooldown to the player on a specified item.
     * @param player The player to apply to.
     * @param item The item to apply for.
     * @param duration The duration in ticks.
     */
    public static void applyCooldown(PlayerEntity player, Item item, int duration) {
        if (!player.isCreative()) {
            player.getItemCooldownManager().set(item, duration);
        }
    }

    /**
     * Swings the hand of the player.
     * @param player The player to apply to.
     * @param hand The hand to swing.
     */
    public static void swingHand(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) {
            player.swingHand(hand);
        }
    }

    /**
     * Checks if a specified player has an item in either of their hands.
     * @param player The player to check.
     * @param item The item to check for.
     */
    public static boolean hasItemInHands(PlayerEntity player, Item item) {
        return player.getMainHandStack().isOf(item) || player.getOffHandStack().isOf(item);
    }
}
