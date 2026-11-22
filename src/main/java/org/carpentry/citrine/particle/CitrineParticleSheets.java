package org.carpentry.citrine.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;

@Environment(EnvType.CLIENT)
public final class CitrineParticleSheets {
    private CitrineParticleSheets() {}

    public static final ParticleTextureSheet TRANSLUCENT_NO_DEPTH = new ParticleTextureSheet() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.setShader(GameRenderer::getParticleProgram);
            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        }

        @Override
        public void draw(Tessellator tessellator) {
            tessellator.draw();
        }

        @Override
        public String toString() {
            return "CITRINE_TRANSLUCENT_NO_DEPTH";
        }
    };
}
