package org.carpentry.citrine.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.text.Style;
import net.minecraft.util.Util;
import org.carpentry.citrine.api.client.text.GlyphCtx;
import org.carpentry.citrine.api.client.text.GlyphTransform;
import org.carpentry.citrine.api.client.text.TextEffect;
import org.carpentry.citrine.api.client.text.TextEffects;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.font.TextRenderer$Drawer")
public abstract class TextRendererDrawerMixin {
    @Unique
    private @Nullable TextEffect citrine$effect;
    @Unique private int citrine$glyphIndex = 0;
    @Unique private static final GlyphTransform TRANSFORM = new GlyphTransform();
    @Unique private static final Matrix4f SCRATCH = new Matrix4f();

    @Inject(method = "accept", at = @At("HEAD"))
    private void citrine$capture(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir) {
        this.citrine$effect = TextEffects.get(style.getInsertion());
    }

    @Inject(method = "accept", at = @At("RETURN"))
    private void citrine$advance(int index, Style style, int codePoint, CallbackInfoReturnable<Boolean> cir) {
        this.citrine$glyphIndex++;
    }

    @WrapOperation(
            method = "accept",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawGlyph(Lnet/minecraft/client/font/GlyphRenderer;ZZFFFLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFFFI)V"))
    private void citrine$wrapDraw(TextRenderer self, GlyphRenderer glyph, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vc, float r, float g, float b, float a, int light, Operation<Void> original) {
        if (citrine$effect == null) {
            original.call(self, glyph, bold, italic, weight, x, y, matrix, vc, r, g, b, a, light);
            return;
        }
        GlyphTransform t = TRANSFORM;
        t.reset();
        citrine$effect.apply(new GlyphCtx(citrine$glyphIndex, Util.getMeasuringTimeMs() / 1000f), t);

        SCRATCH.set(matrix).translate(x + t.dx + 4f, y + t.dy + 4f, 0f).rotateZ(t.rotation).scale(t.scale).translate(-4f, -4f, 0f);
        original.call(self, glyph, bold, italic, weight, 0f, 0f, SCRATCH, vc, r, g, b, a, light);
    }
}
