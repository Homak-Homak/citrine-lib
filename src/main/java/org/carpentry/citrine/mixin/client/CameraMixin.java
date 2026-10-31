package org.carpentry.citrine.mixin.client;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.carpentry.citrine.api.client.shake.ScreenShakeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    protected abstract void setRotation(float yaw, float pitch);
    @Shadow public abstract float getYaw();
    @Shadow public abstract float getPitch();

    @Inject(method = "update", at = @At("TAIL"))
    private void citrine$applyShake(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        float strength = ScreenShakeManager.getStrength(tickDelta);
        if (strength <= 0f) return;
        setRotation(getYaw() + ScreenShakeManager.noiseX(tickDelta) * strength,
                getPitch() + ScreenShakeManager.noiseY(tickDelta) * strength
        );
    }
}
