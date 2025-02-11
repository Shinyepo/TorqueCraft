package dev.shinyepo.torquecraft.particle.sprinkler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SprinklerWaterProvider implements ParticleProvider<SimpleParticleType> {

    public SprinklerWaterProvider() {
    }

    public static TextureSheetParticle createSprinklerParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        return new SprinklerWater(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        return new SprinklerWater(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
    }
}
