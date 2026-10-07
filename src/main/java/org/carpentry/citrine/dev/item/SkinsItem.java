package org.carpentry.citrine.dev.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.item.ItemWithSkins;

import java.util.List;

public class SkinsItem extends Item implements ItemWithSkins {
    public SkinsItem(Settings settings) {
        super(settings);
    }

    @Override
    public List<Identifier> getSkins() {
        return List.of(
                new Identifier("citrine", "scythe_skin"),
                new Identifier("minecraft", "stick"),
                new Identifier("minecraft", "blaze_rod"),
                new Identifier("minecraft", "netherite_sword")
        );
    }

    @Override
    public int getCurrentSkin(NbtCompound nbt) {
        return ItemWithSkins.super.getCurrentSkin(nbt);
    }

    @Override
    public void setSkin(NbtCompound nbt, int skin) {
        ItemWithSkins.super.setSkin(nbt, skin);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        NbtCompound nbt = stack.getOrCreateNbt();
        int next = getCurrentSkin(stack.getOrCreateNbt()) +1;

        if (!world.isClient) {
            if (next >= getSkins().size()){
                nbt.remove("skin");
                if (nbt.isEmpty()){ stack.setNbt(null);
                }
            } else{
                setSkin(nbt, (getCurrentSkin(nbt) + 1) % getSkins().size()); // cycles bewteen skins
            }
        }
        return TypedActionResult.success(stack, world.isClient);
    }
}
