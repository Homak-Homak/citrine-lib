package org.carpentry.citrine.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.carpentry.citrine.api.item.UseWhileSprintingItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @ModifyExpressionValue(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
            )
    )
    private boolean citrine$useWhileSprinting(boolean usingItem) {
        if (!usingItem) return false;

        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        ItemStack active = player.getActiveItem();

        return !(active.getItem() instanceof UseWhileSprintingItem item) || !item.canSprint(player);
    }
}