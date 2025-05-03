package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.datagen.loot.TorqueLootTableProvider;
import dev.shinyepo.torquecraft.datagen.models.TorqueModelProvider;
import dev.shinyepo.torquecraft.datagen.particle.TorqueParticleProvider;
import dev.shinyepo.torquecraft.datagen.recipe.TorqueRecipeProvider;
import dev.shinyepo.torquecraft.datagen.tag.TorqueBlockTagGenerator;
import dev.shinyepo.torquecraft.datagen.tag.TorqueItemTagGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TorqueCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        var lProvider = event.getLookupProvider();

        gen.addProvider(true, TorqueLootTableProvider.create(packOutput,lProvider));


        TorqueBlockTagGenerator blockTagGenerator = gen.addProvider(true, new TorqueBlockTagGenerator(packOutput, lProvider));
        gen.addProvider(true, new TorqueItemTagGenerator(packOutput, lProvider, blockTagGenerator.contentsGetter()));
        gen.addProvider(true, new TorqueModelProvider(packOutput));
        gen.addProvider(true, new TorqueParticleProvider(packOutput));
        gen.addProvider(true, new TorqueRecipeProvider.Runner(packOutput, lProvider));
    }
}
