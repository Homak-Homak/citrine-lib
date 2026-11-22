package org.carpentry.citrine.api.client.shader;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class CitrineShaderRegistry {
    public static CitrineShaderHandle register(Identifier id) {
        return register(id, VertexFormats.POSITION_COLOR_TEXTURE);
    }

    public static CitrineShaderHandle register(Identifier id, VertexFormat format) {
        CitrineShaderHandle handle = new CitrineShaderHandle();
        CoreShaderRegistrationCallback.EVENT.register(ctx -> ctx.register(id, format, handle::set));
        return handle;
    }
}
