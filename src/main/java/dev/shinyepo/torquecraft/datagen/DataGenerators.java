package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.datagen.loot.TorqueLootTableProvider;
import dev.shinyepo.torquecraft.datagen.models.TorqueModelProvider;
import dev.shinyepo.torquecraft.datagen.particle.TorqueParticleProvider;
import dev.shinyepo.torquecraft.datagen.recipe.TorqueRecipeProvider;
import dev.shinyepo.torquecraft.datagen.tag.TorqueBlockTagProvider;
import dev.shinyepo.torquecraft.datagen.tag.TorqueItemTagProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TorqueCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new TorqueModelProvider(packOutput));
        generator.addProvider(true, new TorqueParticleProvider(packOutput));
        generator.addProvider(true, TorqueLootTableProvider.create(packOutput, lookupProvider));
        TorqueBlockTagProvider blockTagGenerator = generator.addProvider(true, new TorqueBlockTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new TorqueItemTagProvider(packOutput, lookupProvider, blockTagGenerator.contentsGetter()));

        generator.addProvider(true, new TorqueRecipeProvider.Runner(packOutput, lookupProvider));
    }
}
