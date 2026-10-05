package org.carpentry.citrine.dev.item;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.carpentry.citrine.api.item.CustomHeldPoseItem;
import org.carpentry.citrine.api.item.OnHitSoundItem;
import org.carpentry.citrine.api.item.UseWhileSprintingItem;
import org.carpentry.citrine.api.item.VaryingModelItem;
import org.carpentry.citrine.api.util.model.ModelPresets;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManyModelsItem extends SwordItem implements VaryingModelItem, UseWhileSprintingItem, CustomHeldPoseItem, OnHitSoundItem {
    private static final Identifier GUI = new Identifier("citrine", "dev_gui");
    private static final Identifier POINTING = new Identifier("citrine", "dev_pointing");
    private static final Identifier BLOCKING = new Identifier("citrine", "dev_blocking");

    public ManyModelsItem(Settings settings) {
        super(ToolMaterials.DIAMOND, 3, -2.5f, settings);
    }

    // model selection

    @Override
    public List<Identifier> getModels() {
        return List.of(GUI, POINTING, BLOCKING); // include every id that getModel can return
    }

    @Override
    public @Nullable Identifier getModel(ItemStack stack, @Nullable LivingEntity holder, ModelTransformationMode mode) {
        Identifier id = ModelPresets.handheld(mode, GUI);
        if (id != null) return id;

        id = ModelPresets.whileUsing(holder, stack, BLOCKING);
        if (id != null) return id;

        return ModelPresets.whileSneaking(holder, POINTING); // null uses the default model
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

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
        if (player.isSneaking()){
         return BipedEntityModel.ArmPose.SPYGLASS;
        }
        if (player.isUsingItem()){
            return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
        }
        return BipedEntityModel.ArmPose.ITEM;
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
        return 2;
    }
}