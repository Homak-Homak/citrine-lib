package org.carpentry.citrine.api.util.model;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class ModelPresets {
    private ModelPresets() {}

    // Hand model by default, a different one in gui n stuff
    public static @Nullable Identifier handheld(ModelTransformationMode mode, Identifier guiModel) {
        return ModelUtils.isGui(mode) ? guiModel : null;
    }

    public static @Nullable Identifier whileUsing(@Nullable LivingEntity holder, ItemStack stack, Identifier usingModel) {
        return holder != null && holder.isUsingItem() && holder.getActiveItem().isOf(stack.getItem()) ? usingModel : null;
    }

    public static @Nullable Identifier whileSneaking(@Nullable LivingEntity holder, Identifier sneakModel) {
        return holder != null && holder.isSneaking() ? sneakModel : null;
    }
}