package org.carpentry.citrine.api.cooldown;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public final class StackCooldownManager {

    private static final String END_KEY = "CitrineCooldownEnd";
    private static final String DURATION_KEY = "CitrineCooldownDuration";

    private StackCooldownManager() {}

    public static void setCooldown(World world, ItemStack stack, int ticks) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putLong(END_KEY, world.getTime() + ticks);
        nbt.putInt(DURATION_KEY, ticks);
    }

    public static int getCooldown(World world, ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(END_KEY)) return 0;
        return (int) Math.max(0L, nbt.getLong(END_KEY) - world.getTime());
    }

    public static boolean isCoolingDown(World world, ItemStack stack) {
        return getCooldown(world, stack) > 0;
    }

    // 1.0 = just started, 0.0 = done. Smooth between ticks via the tickDelta.
    public static float getProgress(World world, ItemStack stack, float tickDelta) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(END_KEY)) return 0f;

        int duration = nbt.getInt(DURATION_KEY);
        if (duration <= 0) return 0f;

        float remaining = nbt.getLong(END_KEY) - (world.getTime() + tickDelta);
        return MathHelper.clamp(remaining / duration, 0f, 1f);
    }

    public static void clearCooldown(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return;

        nbt.remove(END_KEY);
        nbt.remove(DURATION_KEY);

        if (nbt.isEmpty()) {
            stack.setNbt(null);
        }
    }

    // Server-side cleanup so expired keys don't stick around and block stacking.
    public static void clearIfExpired(World world, ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(END_KEY) && nbt.getLong(END_KEY) <= world.getTime()) {
            clearCooldown(stack);
        }
    }
}