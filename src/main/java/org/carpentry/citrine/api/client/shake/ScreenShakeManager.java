package org.carpentry.citrine.api.client.shake;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Environment(EnvType.CLIENT)
public final class ScreenShakeManager {
    private static final float MAX_STRENGTH = 15f; // total degrees so stacking can't make you vomit lmao

    private static final List<ScreenShakeInstance> ACTIVE = new ArrayList<>();
    private static float prevX, prevY, curX, curY;

    private ScreenShakeManager() {}

    public static void add(ScreenShakeInstance instance) { ACTIVE.add(instance); }
    public static void clear() { ACTIVE.clear(); prevX = prevY = curX = curY = 0f; }

    public static void tick() {
        prevX = curX;
        prevY = curY;

        if (ACTIVE.isEmpty()) {
            curX = curY = 0f;
            return;
        }

        curX = ThreadLocalRandom.current().nextFloat() * 2f - 1f;
        curY = ThreadLocalRandom.current().nextFloat() * 2f - 1f;

        for (int i = ACTIVE.size() - 1; i >= 0; i--) {
            if (ACTIVE.get(i).tick()) ACTIVE.remove(i);
        }
    }

    // Total shake strength this frame, 0 when idle
    public static float getStrength(float tickDelta) {
        if (ACTIVE.isEmpty()) return 0f;
        float sum = 0f;
        for (int i = 0; i < ACTIVE.size(); i++) sum += ACTIVE.get(i).getStrength(tickDelta);
        return Math.min(sum, MAX_STRENGTH);
    }

    public static float noiseX(float tickDelta) { return MathHelper.lerp(tickDelta, prevX, curX); }
    public static float noiseY(float tickDelta) { return MathHelper.lerp(tickDelta, prevY, curY); }
}
