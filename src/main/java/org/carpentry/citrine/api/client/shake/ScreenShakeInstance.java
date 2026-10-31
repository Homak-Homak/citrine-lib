package org.carpentry.citrine.api.client.shake;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.carpentry.citrine.api.util.EasingType;

@Environment(EnvType.CLIENT)
public final class ScreenShakeInstance {
    private final EasingType easing;
    private final float intensity;
    private final int duration;
    private int age;

    public ScreenShakeInstance(EasingType easing, float intensity, int durationTicks) {
        this.easing = easing;
        this.intensity = intensity;
        this.duration = Math.max(1, durationTicks);
    }

    // Returns true when finished
    boolean tick() {
        return ++age >= duration;
    }

    // Starts at full intensity and decays to 0. Easing shapes the fall-off (QUAD = fast drop, then a long tail).
    float getStrength(float tickDelta) {
        float progress = Math.min(1f, (age + tickDelta) / duration);
        return intensity * easing.getEased(1f - progress);
    }
}
