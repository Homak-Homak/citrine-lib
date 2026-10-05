package org.carpentry.citrine.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.carpentry.citrine.api.item.ItemWithSkins;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), argsOnly = true)

    public BakedModel addModels(BakedModel value, ItemStack stack, ModelTransformationMode mode,
                                boolean leftHanded, MatrixStack matrices,
                                VertexConsumerProvider providers, int light, int overlay) {
        if (!(stack.getItem() instanceof ItemWithSkins skins)) return value;

        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return value;

        int index = skins.getCurrentSkin(nbt);
        List<Identifier> list = skins.getSkins();
        if (index < 0 || index >= list.size()) return value;

        Identifier skin = list.get(index);
        return ((ItemRendererAccessor) this).citrine$getModels().getModelManager()
                .getModel(new ModelIdentifier(skin.getNamespace(), skin.getPath(), "inventory"));
    }


}
