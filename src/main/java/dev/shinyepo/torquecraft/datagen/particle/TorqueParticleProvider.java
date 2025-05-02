package dev.shinyepo.torquecraft.datagen.particle;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.particle.TorqueParticles;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorqueParticleProvider extends ParticleDescriptionProvider {
    public TorqueParticleProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void addDescriptions() {
        sprite(TorqueParticles.SPRINKLER_WATER.get(), fromNamespaceAndPath(TorqueCraft.MODID, "sprinkler_water"));
    }
}
