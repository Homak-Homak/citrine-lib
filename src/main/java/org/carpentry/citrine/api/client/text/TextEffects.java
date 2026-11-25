package org.carpentry.citrine.api.client.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class TextEffects {
    // hash from https://www.shadertoy.com/view/NdXyW7
    private static int k = 0x456789ab;
    private static int uhash11(int n){
        n ^= (n << 1);
        n ^= (n >> 1);
        n *= k;
        n ^= (n << 1);
        return n * k;
    }
    private static float hash11(float p){
        int n = (int) p;
        return uhash11(n) / (float) Integer.MAX_VALUE;
    }

    public static MutableText wave(Text t) {return t.copy().setStyle(t.getStyle().withInsertion("citrine/fx/wave"));}
    public static MutableText wobble(Text t) {return t.copy().setStyle(t.getStyle().withInsertion("citrine/fx/wobble"));}
    public static MutableText shake(Text t) {return t.copy().setStyle(t.getStyle().withInsertion("citrine/fx/shake"));}

    private static final Map<String, TextEffect> REGISTRY = new HashMap<>();
    public static void register(String fontId, TextEffect e) { REGISTRY.put(fontId, e); }
    public static @Nullable TextEffect get(String fontId) { return REGISTRY.get(fontId); }

    public static void init() {
        register("citrine/fx/wave", (ctx, out) -> out.dy = MathHelper.sin(ctx.timeSeconds() * 6f + ctx.index() * 0.6f) * 2f);
        register("citrine/fx/wobble", (ctx, out) -> {
            out.dx = (float) Math.sin(ctx.timeSeconds() * 4 + hash11(ctx.index()) * 10) / 3f;
            out.dy = (float) Math.cos(ctx.timeSeconds() * 4 + hash11(ctx.index()) * 10) / 2f;
        });
        register("citrine/fx/shake", (ctx, out) -> out.rotation = MathHelper.sin(ctx.timeSeconds() * 6f + ctx.index() * 0.6f + hash11(ctx.index())) * 0.15f);
    }
}
