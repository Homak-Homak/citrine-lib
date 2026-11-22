package org.carpentry.citrine.api.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.carpentry.citrine.Citrine;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class CitrineShaderQuad {
    private final Supplier<ShaderProgram> program;
    private Identifier[] textures = new Identifier[0];
    private Consumer<ShaderProgram> uniforms = p -> {};
    private final List<CitrineShaderUniform> uniformsToAdd;

    public CitrineShaderQuad(Supplier<ShaderProgram> program, List<CitrineShaderUniform> uniformsToAdd) {
        this.program = program;
        this.uniformsToAdd = uniformsToAdd;
    }

    public CitrineShaderQuad(Supplier<ShaderProgram> program) {
        this.program = program;
        this.uniformsToAdd = new ArrayList<>();
    }

    public CitrineShaderQuad textures(Identifier... textures) { this.textures = textures; return this; }
    public CitrineShaderQuad uniforms(Consumer<ShaderProgram> uniforms) { this.uniforms = uniforms; return this; }

    public void draw(DrawContext ctx, float x, float y, float w, float h) {
        ShaderProgram p = program.get();
        if (p == null) {
            Citrine.LOGGER.warn("Shader program is null! Shader not loaded yet.");
            return;
        }
        ctx.draw();
        draw(ctx.getMatrices().peek().getPositionMatrix(), x, y, w, h);
    }

    public void draw(Matrix4f mat, float x, float y, float w, float h) {
        ShaderProgram p = program.get();
        if (p == null) {
            Citrine.LOGGER.warn("Shader program is null");
            return;
        }

        RenderSystem.setShader(program);
        for (int i = 0; i < textures.length; i++) {
            RenderSystem.setShaderTexture(i, textures[i]);
        }
        p.getUniformOrDefault("Size").set(w, h);
        p.getUniformOrDefault("Time").set((Util.getMeasuringTimeMs() % 1000000L) / 1000f);
        for (CitrineShaderUniform u : uniformsToAdd) {
            switch(u.type()) {
                case MAT4 -> p.getUniformOrDefault(u.name()).set((Matrix4f) u.data());
                case FLOAT -> p.getUniformOrDefault(u.name()).set((float) u.data());
                case VEC4 -> p.getUniformOrDefault(u.name()).set((Vector4f) u.data());
            }
        }
        uniforms.accept(p);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        bb.vertex(mat, x, y + h, 0).color(1f, 1f, 1f, 1f).texture(0, 1).next();
        bb.vertex(mat, x + w, y + h, 0).color(1f, 1f, 1f, 1f).texture(1, 1).next();
        bb.vertex(mat, x + w, y, 0).color(1f, 1f, 1f, 1f).texture(1, 0).next();
        bb.vertex(mat, x, y, 0).color(1f, 1f, 1f, 1f).texture(0, 0).next();
        BufferRenderer.drawWithGlobalProgram(bb.end());

        RenderSystem.disableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
    }
}