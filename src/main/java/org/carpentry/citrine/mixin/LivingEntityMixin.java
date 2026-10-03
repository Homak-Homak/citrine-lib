package org.carpentry.citrine.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.carpentry.citrine.api.effect.EffectClearResult;
import org.carpentry.citrine.api.item.CustomKillSourceItem;
import org.carpentry.citrine.dev.UnclearableEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract Collection<StatusEffectInstance> getStatusEffects();

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

    // TODO make this shit actually work lumi please help
    @Inject(method = "removeStatusEffect", at = @At("HEAD"), cancellable = true)
    private void citrine$unclearableEff(StatusEffect type, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        StatusEffectInstance existing = entity.getStatusEffect(type);

        if (existing != null && type instanceof UnclearableEffect e) {
            EffectClearResult result = e.tryClear(entity, existing);

            if (!result.doClear()) {
                if (result.effect() != null) {
                    this.addStatusEffect(result.effect());
                }
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"), cancellable = true)
    private void citrine$unclearableEff(StatusEffectInstance effect, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        StatusEffect type = effect.getEffectType();

        if (effect != null && type instanceof UnclearableEffect e) {
            EffectClearResult result = e.tryClear(entity, effect);

            if (!result.doClear()) {
                if (result.effect() != null) {
                    this.addStatusEffect(result.effect());
                }
                ci.cancel();
            }
        }
    }
}
