package dev.shinyepo.torquecraft.particle.sprinkler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SprinklerWater extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected SprinklerWater(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.spriteSet = spriteSet;
        this.gravity = 0.1F;
        setParticleSpeed(pXSpeed, pYSpeed, pZSpeed);
        setLifetime(40);
//        setPower(0.2F);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void tick() {

        this.setSpriteFromAge(spriteSet);
        super.tick();
    }

}
