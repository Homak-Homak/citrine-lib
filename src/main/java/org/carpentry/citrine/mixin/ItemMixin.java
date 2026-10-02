package org.carpentry.citrine.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.carpentry.citrine.item.implement.IOnCritItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "postHit", at = @At("TAIL"))
    private void onCrit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (attacker.fallDistance > 0.0f && !attacker.getStatusEffects().stream().map(StatusEffectInstance::getEffectType).toList().contains(StatusEffects.SLOW_FALLING)) {
            if (((Item) (Object) this) instanceof IOnCritItem i) {
                i.onCrit(stack, attacker, target);
            }
        }
    }
}
