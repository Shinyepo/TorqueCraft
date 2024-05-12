package dev.shinyepo.torquecraft.datagen.item;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TorqueItemModelProvider extends ItemModelProvider {
    public TorqueItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TorqueCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(TorqueItems.TUNGSTEN_INGOT.get());
        basicItem(TorqueItems.CANOLA_SEEDS.get());
        basicItem(TorqueItems.CRUSHED_SEEDS.get());
        basicItem(TorqueItems.JET_FUEL_BUCKET.get());
        basicItem(TorqueItems.LUBRICANT_BUCKET.get());
        basicItem(TorqueItems.PRESSURE_GAUGE.get());

        withExistingParent(String.valueOf(TorqueItems.FLUID_TANK_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.FLUID_TANK.get()).getPath());
        withExistingParent(String.valueOf(TorqueItems.FLUID_PIPE_ITEM.get()).toLowerCase(),
                "torquecraft:block/pipe");
        withExistingParent(String.valueOf(TorqueItems.STEAM_PIPE_ITEM.get()).toLowerCase(),
                "torquecraft:block/pipe");
        withExistingParent(String.valueOf(TorqueItems.TUNGSTEN_BLOCK_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.TUNGSTEN_BLOCK.get()).getPath());

        withExistingParent(String.valueOf(TorqueItems.STEAM_ENGINE_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.STEAM_ENGINE.get()).getPath());

        withExistingParent(String.valueOf(TorqueItems.MECHANICAL_FAN_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.MECHANICAL_FAN.get()).getPath());

        withExistingParent(String.valueOf(TorqueItems.GRINDER_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.GRINDER.get()).getPath());

        withExistingParent(String.valueOf(TorqueItems.PUMP_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.PUMP.get()).getPath());

        withExistingParent(String.valueOf(TorqueItems.SHAFT_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TorqueBlocks.SHAFT.get()).getPath());
    }
}
