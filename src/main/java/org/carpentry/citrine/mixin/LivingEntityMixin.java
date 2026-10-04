package org.carpentry.citrine.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.carpentry.citrine.api.effect.UnclearableStatusEffect;
import org.carpentry.citrine.api.item.CustomKillSourceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @WrapOperation(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z"
            )
    )
    private boolean citrine$customKillSource(LivingEntity instance, DamageSource source, Operation<Boolean> original) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof PlayerEntity player) {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof CustomKillSourceItem item) {
                return original.call(instance, item.getKillSource(player, stack));
            }
        }
        return original.call(instance, source);
    }

    // use this you goofy goose
    @WrapMethod(method = "clearStatusEffects")
    private boolean citrine$unclearableEff(Operation<Boolean> original) {
        LivingEntity living = (LivingEntity)(Object)this;
        if (!living.getWorld().isClient()) {
            for (StatusEffectInstance instance : living.getActiveStatusEffects().values()) {
                if (instance.getEffectType() instanceof UnclearableStatusEffect) {
                    boolean result = original.call();
                    this.addStatusEffect(instance);
                    return result;
                }
            }
        }
        return original.call();
    }
}
