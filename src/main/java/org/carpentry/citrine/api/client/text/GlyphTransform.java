package org.carpentry.citrine.api.client.text;

public final class GlyphTransform {
    public float dx, dy, rotation, scale = 1f;
    public void reset() { dx = dy = rotation = 0; scale = 1f; }
}
