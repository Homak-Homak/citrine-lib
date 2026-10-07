package org.carpentry.citrine.api.item;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VaryingModelItem {
    // Every model this item can use
    List<Identifier> getModels();

    // Return one of getModels(), or null for the item's normal model. holder is null in the GUI.
    @Nullable
    Identifier getModel(ItemStack stack, @Nullable LivingEntity holder, ModelTransformationMode mode);
}