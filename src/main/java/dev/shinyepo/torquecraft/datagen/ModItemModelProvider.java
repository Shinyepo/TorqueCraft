package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.TorqueItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TorqueCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(TorqueItems.TUNGSTEN_INGOT.get());
        basicItem(TorqueItems.CANOLA_SEEDS.get());
        basicItem(TorqueItems.CANOLA_MEAL.get());

        withExistingParent(String.valueOf(TorqueItems.TUNGSTEN_BLOCK_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.TUNGSTEN_BLOCK.get()).getPath());

        withExistingParent(String.valueOf(TorqueItems.STEAM_ENGINE_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.STEAM_ENGINE.get()).getPath());

        withExistingParent(String.valueOf(TorqueItems.MECHANICAL_FAN_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.MECHANICAL_FAN_BLOCK.get()).getPath());
    }
}
