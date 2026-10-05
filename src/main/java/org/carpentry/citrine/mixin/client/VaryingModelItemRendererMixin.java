package org.carpentry.citrine.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.carpentry.citrine.api.item.VaryingModelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class VaryingModelItemRendererMixin {

    @Unique
    private static final ThreadLocal<LivingEntity> citrine$holder = new ThreadLocal<>();

    @Inject(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
            at = @At("HEAD")
    )
    private void citrine$captureHolder(LivingEntity entity, ItemStack stack, ModelTransformationMode mode,
                                       boolean leftHanded, MatrixStack matrices, VertexConsumerProvider providers,
                                       World world, int light, int overlay, int seed, CallbackInfo ci) {
        citrine$holder.set(entity);
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
            at = @At("RETURN")
    )
    private void citrine$releaseHolder(LivingEntity entity, ItemStack stack, ModelTransformationMode mode,
                                       boolean leftHanded, MatrixStack matrices, VertexConsumerProvider providers,
                                       World world, int light, int overlay, int seed, CallbackInfo ci) {
        citrine$holder.remove();
    }

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"), argsOnly = true
    )
    private BakedModel citrine$varyingModel(BakedModel original, ItemStack stack, ModelTransformationMode mode,
                                            boolean leftHanded, MatrixStack matrices, VertexConsumerProvider providers,
                                            int light, int overlay) {
        if (!(stack.getItem() instanceof VaryingModelItem item)) return original;

        Identifier id = item.getModel(stack, citrine$holder.get(), mode);
        if (id == null) return original;
        return ((ItemRendererAccessor) this).citrine$getModels().getModelManager()
                .getModel(new ModelIdentifier(id.getNamespace(), id.getPath(), "inventory"));
    }
}
