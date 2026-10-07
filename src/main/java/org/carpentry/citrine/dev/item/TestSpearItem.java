package org.carpentry.citrine.dev.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.carpentry.citrine.api.item.*;
import org.carpentry.citrine.api.util.model.ModelPresets;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TestSpearItem extends SwordItem implements VaryingModelItem, UseWhileSprintingItem, CustomHeldPoseItem, OnHitSoundItem, ItemWithSkins {
    private static final Identifier GUI = new Identifier("citrine", "dev_gui");
    private static final Identifier POINTING = new Identifier("citrine", "dev_pointing");
    private static final Identifier SKIN     = new Identifier("citrine", "dev_skin");

    private static Identifier suffix(Identifier id, String s) {
        return new Identifier(id.getNamespace(), id.getPath() + s);
    }

    public TestSpearItem(Settings settings) {
        super(ToolMaterials.DIAMOND, 3, -2.5f, settings);
    }

    // model selection

    @Override
    public List<Identifier> getSkins() {
        return List.of(SKIN); // add more skin bases later
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
    public List<Identifier> getModels() {
        List<Identifier> all = new ArrayList<>(List.of(GUI, POINTING));
        for (Identifier skin : getSkins()) {
            all.add(skin);
            all.add(suffix(skin, "_gui"));
            all.add(suffix(skin, "_pointing"));
        }
        return all; // every id getModel() can return
    }

    @Override
    public @Nullable Identifier getModel(ItemStack stack, @Nullable LivingEntity holder, ModelTransformationMode mode) {
        NbtCompound nbt = stack.getNbt();
        int skin = nbt == null ? -1 : getCurrentSkin(nbt);
        List<Identifier> skins = getSkins();
        boolean skinned = skin >= 0 && skin < skins.size();

        Identifier base     = skinned ? skins.get(skin) : null;
        Identifier gui      = skinned ? suffix(base, "_gui") : GUI;
        Identifier pointing = skinned ? suffix(base, "_pointing") : POINTING;

        Identifier id = ModelPresets.handheld(mode, gui);
        if (id != null) return id;

        id = ModelPresets.whileUsing(holder, stack, pointing);
        if (id != null) return id;

        return base;
    }
    // black magic fuckery

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public boolean canSprint(PlayerEntity user) {
        return true;
    }

    // arm pose & hit sound stuff

    @Override
    public BipedEntityModel.ArmPose getArmPose(ItemStack stack, PlayerEntity player) {
        if (player.getActiveItem().isOf(this)){
            return BipedEntityModel.ArmPose.SPYGLASS;
        }
        return BipedEntityModel.ArmPose.BLOCK;
    }

    @Override
    public SoundEvent getHitSound(World world, ItemStack stack, LivingEntity attacker, LivingEntity target) {
        return SoundEvents.BLOCK_CHAIN_BREAK;
    }

    @Override
    public float getPitch(ItemStack stack) {
        return 1;
    }

    @Override
    public float getVolume(ItemStack stack) {
        return 1;
    }

    //skin switching

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        World world = context.getWorld();
        NbtCompound nbt = stack.getOrCreateNbt();
        PlayerEntity user = context.getPlayer();
        int next = getCurrentSkin(stack.getOrCreateNbt()) +1;

        if (state.isOf(Blocks.SMITHING_TABLE) && !world.isClient && user != null &&user.isSneaking()){
            if (next >=getSkins().size()){
                nbt.remove("skin");
                if (nbt.isEmpty()) stack.setNbt(null);
            } else {
                setSkin(nbt, next);
            }
            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }
}
