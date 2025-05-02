package dev.shinyepo.torquecraft.particle.sprinkler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SprinklerWaterProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public SprinklerWaterProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

//    public static TextureSheetParticle createSprinklerParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
//        return new SprinklerWater(pLevel, pX, pY, pZ,pType, pXSpeed, pYSpeed, pZSpeed);
//    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        return new SprinklerWater(pLevel, pX, pY, pZ, spriteSet, pXSpeed, pYSpeed, pZSpeed);
    }
}
