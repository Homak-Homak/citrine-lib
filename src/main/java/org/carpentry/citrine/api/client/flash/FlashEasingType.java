package org.carpentry.citrine.api.client.flash;

import java.util.function.Function;

public enum FlashEasingType {
    LINEAR(in -> in),
    QUAD(in -> in * in),
    CUBIC(in -> in * in * in),
    TESS(in -> in * in * in * in),
    LOG10(in -> (float) (Math.log10(1 + 9 * in))),
    INVQUAD(in -> (float) Math.sqrt(in)),
    INVCUB(in -> (float) Math.cbrt(in));

    private final Function<Float, Float> func;

    FlashEasingType(Function<Float, Float> func) {
        this.func = func;
    }

    public float getEased(float in) {
        float clamped = Math.max(0f, Math.min(1f, in));
        return func.apply(clamped);
    }
}