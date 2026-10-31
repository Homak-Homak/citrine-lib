package org.carpentry.citrine.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.carpentry.citrine.api.util.particles.ParticleProperties;

@Environment(EnvType.CLIENT)
public class CitrineParticle extends SpriteBillboardParticle {
    private final ParticleProperties p;
    private final float r0, g0, b0, r1, g1, b1;

    protected CitrineParticle(ClientWorld world, double x, double y, double z,
                              double vx, double vy, double vz,
                              CitrineParticleEffect effect, SpriteProvider sprites) {
        super(world, x, y, z); // not the velocity ctor: that one randomizes velocity
        this.p = effect.props();

        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;

        this.maxAge = Math.max(1, p.lifetime());
        this.scale = p.startScale();
        this.gravityStrength = p.gravity();
        this.velocityMultiplier = p.drag();
        this.collidesWithWorld = p.collision();
        this.angle = this.prevAngle = p.rotation();
        this.alpha = p.startAlpha();

        r0 = (p.startColor() >> 16 & 255) / 255f; g0 = (p.startColor() >> 8 & 255) / 255f; b0 = (p.startColor() & 255) / 255f;
        r1 = (p.endColor() >> 16 & 255) / 255f;   g1 = (p.endColor() >> 8 & 255) / 255f;   b1 = (p.endColor() & 255) / 255f;
        this.setColor(r0, g0, b0);

        this.setSprite(sprites);
    }

    @Override
    public void tick() {
        this.prevAngle = this.angle;
        this.angle += p.spin();
        super.tick(); // age, gravity, drag, movement, collision

        float t = Math.min(1f, (float) age / maxAge);
        this.setColor(MathHelper.lerp(t, r0, r1), MathHelper.lerp(t, g0, g1), MathHelper.lerp(t, b0, b1));
        this.alpha = MathHelper.lerp(t, p.startAlpha(), p.endAlpha());
    }

    @Override
    public float getSize(float tickDelta) {
        float t = MathHelper.clamp((age + tickDelta) / maxAge, 0f, 1f);
        return MathHelper.lerp(t, p.startScale(), p.endScale());
    }

    @Override
    public int getBrightness(float tint) {
        return p.fullBright() ? 0xF000F0 : super.getBrightness(tint);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<CitrineParticleEffect> {
        private final SpriteProvider sprites;
        public Factory(SpriteProvider sprites) { this.sprites = sprites; }
        @Override
        public Particle createParticle(CitrineParticleEffect effect, ClientWorld world,
                                       double x, double y, double z, double vx, double vy, double vz) {
            return new CitrineParticle(world, x, y, z, vx, vy, vz, effect, sprites);
        }
    }
}
