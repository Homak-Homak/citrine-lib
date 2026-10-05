package org.carpentry.citrine.api.item;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface CustomHeldPoseItem {
    // defines the held pose of the item in hand
    BipedEntityModel.ArmPose getArmPose(ItemStack stack, PlayerEntity player);
}
