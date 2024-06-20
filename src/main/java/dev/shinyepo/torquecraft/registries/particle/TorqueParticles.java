package dev.shinyepo.torquecraft.registries.particle;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TorqueParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, TorqueCraft.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPRINKLER_WATER = PARTICLE_TYPES.register("sprinkler_water", () -> new SimpleParticleType(false));
}
