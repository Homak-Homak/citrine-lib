package org.carpentry.citrine.api.client.flash;

import net.minecraft.util.Identifier;

import java.awt.*;

public record ScreenFlash(float ticks, float startTime, Color color, Identifier img, FlashEasingType type) {
}
