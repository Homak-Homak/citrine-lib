package org.carpentry.citrine.dev;

import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.client.shader.CitrineShaderHandle;
import org.carpentry.citrine.api.client.shader.CitrineShaderRegistry;

public class CitrineShaders {
    public static final CitrineShaderHandle MAGIC_SHADER = CitrineShaderRegistry.register(Citrine.id("magic"));

    public static void init() {}
}
