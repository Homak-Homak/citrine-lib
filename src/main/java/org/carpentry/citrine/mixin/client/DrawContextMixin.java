package org.carpentry.citrine.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.carpentry.citrine.api.cooldown.StackCooldownManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Inject(
            method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At("TAIL")
    )
    private void citrine$renderStackCooldown(
            TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci
    ) {
        if (stack.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;
        if (world == null) return;

        float progress = StackCooldownManager.getProgress(world, stack, client.getTickDelta());
        if (progress <= 0f) return;

        DrawContext context = (DrawContext) (Object) this;
        int top = y + MathHelper.floor(16.0f * (1.0f - progress));
        int bottom = top + MathHelper.ceil(16.0f * progress);

        context.fill(RenderLayer.getGuiOverlay(), x, top, x + 16, bottom, Integer.MAX_VALUE);
    }
}