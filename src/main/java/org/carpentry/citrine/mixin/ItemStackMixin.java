package org.carpentry.citrine.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.carpentry.citrine.api.cooldown.StackCooldownManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void citrine$blockUse(
            World world, PlayerEntity user, Hand hand,
            CallbackInfoReturnable<TypedActionResult<ItemStack>> cir
    ) {
        ItemStack self = (ItemStack) (Object) this;

        if (StackCooldownManager.isCoolingDown(world, self)) {
            cir.setReturnValue(TypedActionResult.fail(self));
        }
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void citrine$blockUseOnBlock(
            ItemUsageContext context,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        ItemStack self = (ItemStack) (Object) this;

        if (StackCooldownManager.isCoolingDown(context.getWorld(), self)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    private void citrine$blockUseOnEntity(
            PlayerEntity user, LivingEntity entity, Hand hand,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        ItemStack self = (ItemStack) (Object) this;

        if (StackCooldownManager.isCoolingDown(user.getWorld(), self)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}