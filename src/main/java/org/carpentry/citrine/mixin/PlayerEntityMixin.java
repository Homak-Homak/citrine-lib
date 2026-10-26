package org.carpentry.citrine.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.cooldown.StackCooldownManager;
import org.carpentry.citrine.api.item.OnCritItem;
import org.carpentry.citrine.api.item.ShieldBreakerItem;
import org.carpentry.citrine.api.util.SupporterUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void citrine$cleanupCooldowns(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient) return;

        for (ItemStack s : player.getInventory().main) cleanup(player, s);
        for (ItemStack s : player.getInventory().armor) cleanup(player, s);
        for (ItemStack s : player.getInventory().offHand) cleanup(player, s);
    }

    private static void cleanup(PlayerEntity player, ItemStack stack) {
        if (!stack.isEmpty()) StackCooldownManager.clearIfExpired(player.getWorld(), stack);
    }

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"
            )
    )
    private void citrine$customCritEffectItem(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (target instanceof LivingEntity living) {
            if (player.getMainHandStack().getItem() instanceof OnCritItem item) {
                item.onCrit(player.getMainHandStack(), player, living);
            }
        }
    }

    @Inject(method = "takeShieldHit", at = @At("HEAD"))
    private void citrine$shieldBreaker(LivingEntity attacker, CallbackInfo ci) {
        if (!(attacker.getMainHandStack().getItem() instanceof ShieldBreakerItem breaker)) return;

        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.getActiveItem().getItem() instanceof ShieldItem) {
            player.clearActiveItem();
            player.getWorld().sendEntityStatus(player, (byte) 30);
            player.getItemCooldownManager().set(Items.SHIELD, breaker.getShieldCooldown());
        }
    }
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    public Text citrine$getStyledName(Text original) {
        if (Citrine.CITRINE_SUPPORTERS.isSupporter(this.getUuid())){
            return SupporterUtils.getStyledName(this.getUuid(), original);
        }
        return original;
    }
}