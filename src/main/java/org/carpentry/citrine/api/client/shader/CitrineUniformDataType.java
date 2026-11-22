package org.carpentry.citrine.api.client.shader;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public enum CitrineUniformDataType {
    FLOAT(Float.class),
    VEC4(Vector4f.class),
    MAT4(Matrix4f.class)
    ;
    private final Class<?> type;
    CitrineUniformDataType(Class<?> type) {
        this.type = type;
    }
}
