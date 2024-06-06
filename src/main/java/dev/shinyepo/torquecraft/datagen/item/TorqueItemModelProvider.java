package dev.shinyepo.torquecraft.datagen.item;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.datagen.helpers.CustomItemModelProvider;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TorqueItemModelProvider extends CustomItemModelProvider {
    public TorqueItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TorqueCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Materials
        basicMaterialItem(TorqueItems.NETHER_QUARTZ_DUST.get());
        basicMaterialItem(TorqueItems.TUNGSTEN_INGOT.get());

        //Block Materials
        withExistingParent(TorqueItems.TUNGSTEN_BLOCK_ITEM.get(), TorqueBlocks.TUNGSTEN_BLOCK.get());

        basicItem(TorqueItems.CANOLA_SEEDS.get());
        basicItem(TorqueItems.CRUSHED_SEEDS.get());
        basicItem(TorqueItems.JET_FUEL_BUCKET.get());
        basicItem(TorqueItems.LUBRICANT_BUCKET.get());
        basicItem(TorqueItems.PRESSURE_GAUGE.get());
        basicItem(TorqueItems.ROTARY_WRENCH.get());

        withExistingParent(TorqueItems.FLUID_TANK_ITEM.get(), TorqueBlocks.FLUID_TANK.get());
        withExistingParent(TorqueItems.PUMP_ITEM.get(), TorqueBlocks.PUMP.get());
        withExistingParent(TorqueItems.STEAM_ENGINE_ITEM.get(), TorqueBlocks.STEAM_ENGINE.get());
        withExistingParent(TorqueItems.BEVEL_GEARS_ITEM.get(), TorqueBlocks.BEVEL_GEARS.get());
        withExistingParent(TorqueItems.THREE_WAY_ITEM.get(), TorqueBlocks.THREE_WAY.get());

        withExistingParent(TorqueItems.FLUID_PIPE_ITEM.get(), "torquecraft:block/pipe");
        withExistingParent(TorqueItems.STEAM_PIPE_ITEM.get(), "torquecraft:block/pipe");

        withExistingParent(TorqueItems.ALLOY_FURNACE_ITEM.get(), "furnace/", TorqueBlocks.ALLOY_FURNACE.get());
        withExistingParent(TorqueItems.MECHANICAL_FAN_ITEM.get(), "item/", TorqueBlocks.MECHANICAL_FAN.get());
        withExistingParent(TorqueItems.GRINDER_ITEM.get(), "item/", TorqueBlocks.GRINDER.get());
        withExistingParent(TorqueItems.SHAFT_ITEM.get(), "item/", TorqueBlocks.SHAFT.get());
        withExistingParent(TorqueItems.GEARBOX1_2_ITEM.get(), "gearbox/", TorqueBlocks.GEARBOX1_2.get());
        withExistingParent(TorqueItems.GEARBOX1_4_ITEM.get(), "gearbox/", TorqueBlocks.GEARBOX1_4.get());
        withExistingParent(TorqueItems.COOLING_RADIATOR_ITEM.get(), "radiator/", TorqueBlocks.COOLING_RADIATOR.get(), "_full");
    }
}
