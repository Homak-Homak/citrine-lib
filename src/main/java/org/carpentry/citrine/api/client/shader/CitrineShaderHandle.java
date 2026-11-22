package org.carpentry.citrine.api.client.shader;

import net.minecraft.client.gl.ShaderProgram;

import java.util.function.Supplier;

public final class CitrineShaderHandle implements Supplier<ShaderProgram> {
    private volatile ShaderProgram program;

    @Override
    public ShaderProgram get() { return program; }

    void set(ShaderProgram program) { this.program = program; }
}