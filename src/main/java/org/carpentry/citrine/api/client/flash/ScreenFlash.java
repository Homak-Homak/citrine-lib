package org.carpentry.citrine.api.client.flash;

import net.minecraft.util.Identifier;
import org.carpentry.citrine.api.util.EasingType;

import java.awt.*;

public record ScreenFlash(float ticks, float startTime, Color color, Identifier img, EasingType type) {
}
