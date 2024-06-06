package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.datagen.block.TorqueBlockStateProvider;
import dev.shinyepo.torquecraft.datagen.item.TorqueItemModelProvider;
import dev.shinyepo.torquecraft.datagen.loot.TorqueLootTableProvider;
import dev.shinyepo.torquecraft.datagen.recipe.TorqueRecipeProvider;
import dev.shinyepo.torquecraft.datagen.tag.TorqueBlockTagGenerator;
import dev.shinyepo.torquecraft.datagen.tag.TorqueItemTagGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TorqueCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new TorqueRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), TorqueLootTableProvider.create(packOutput, lookupProvider));

        generator.addProvider(event.includeClient(), new TorqueBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new TorqueItemModelProvider(packOutput, existingFileHelper));

        TorqueBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(), new TorqueBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new TorqueItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));


    }
}
