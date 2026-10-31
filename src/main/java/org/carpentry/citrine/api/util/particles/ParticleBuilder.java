package org.carpentry.citrine.api.util.particles;

import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.carpentry.citrine.particle.CitrineParticleEffect;

public final class ParticleBuilder {
    private final ParticleType<CitrineParticleEffect> type;
    private int lifetime = 20;
    private float startScale = 0.1f, endScale = 0.1f;
    private int startColor = 0xFFFFFF, endColor = 0xFFFFFF;
    private float startAlpha = 1f, endAlpha = 1f;
    private float rotation, spin, gravity, drag = 0.98f;
    private boolean fullBright, collision = true;
    private double mx, my, mz, randomMotion;

    private ParticleBuilder(ParticleType<CitrineParticleEffect> type) { this.type = type; }

    public static ParticleBuilder create(ParticleType<CitrineParticleEffect> type) { return new ParticleBuilder(type); }

    public ParticleBuilder lifetime(int ticks)              { this.lifetime = ticks; return this; }
    public ParticleBuilder scale(float s)                   { return scale(s, s); }
    public ParticleBuilder scale(float start, float end)    { startScale = start; endScale = end; return this; }
    public ParticleBuilder color(int rgb)                   { return color(rgb, rgb); }
    public ParticleBuilder color(int start, int end)        { startColor = start; endColor = end; return this; }
    public ParticleBuilder alpha(float a)                   { return alpha(a, a); }
    public ParticleBuilder alpha(float start, float end)    { startAlpha = start; endAlpha = end; return this; }
    public ParticleBuilder rotation(float radians)          { this.rotation = radians; return this; }
    public ParticleBuilder spin(float radiansPerTick)       { this.spin = radiansPerTick; return this; }
    public ParticleBuilder gravity(float g)                 { this.gravity = g; return this; }
    public ParticleBuilder drag(float multiplier)           { this.drag = multiplier; return this; }
    public ParticleBuilder fullBright()                     { this.fullBright = true; return this; }
    public ParticleBuilder noCollision()                    { this.collision = false; return this; }
    public ParticleBuilder motion(double x, double y, double z) { mx = x; my = y; mz = z; return this; }
    public ParticleBuilder randomMotion(double amount)      { this.randomMotion = amount; return this; }

    /** The effect only: motion isn't part of it, it's a spawn argument. */
    public CitrineParticleEffect build() {
        return new CitrineParticleEffect(type, new ParticleProperties(
                lifetime, startScale, endScale, startColor, endColor, startAlpha, endAlpha,
                rotation, spin, gravity, drag, fullBright, collision));
    }

    public void spawn(World world, double x, double y, double z) { spawn(world, x, y, z, 1); }

    public void spawn(World world, double x, double y, double z, int count) {
        CitrineParticleEffect effect = build();
        for (int i = 0; i < count; i++) {
            double vx = mx + (world.random.nextDouble() * 2 - 1) * randomMotion;
            double vy = my + (world.random.nextDouble() * 2 - 1) * randomMotion;
            double vz = mz + (world.random.nextDouble() * 2 - 1) * randomMotion;

            if (world instanceof ServerWorld serverWorld) {
                // count 0 means delta is the velocity
                serverWorld.spawnParticles(effect, x, y, z, 0, vx, vy, vz, 1.0);
            } else {
                world.addParticle(effect, x, y, z, vx, vy, vz);
            }
        }
    }
}
