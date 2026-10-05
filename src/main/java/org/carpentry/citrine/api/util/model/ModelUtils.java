package org.carpentry.citrine.api.util.model;

import net.minecraft.client.render.model.json.ModelTransformationMode;

public class ModelUtils {

    public static boolean isGui(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.GROUND || mode == ModelTransformationMode.GUI || mode == ModelTransformationMode.FIXED;
    }
}