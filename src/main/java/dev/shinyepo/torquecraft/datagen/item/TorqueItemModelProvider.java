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
        basicMaterialItem(TorqueItems.QUARTZ_DUST.get());
        basicMaterialItem(TorqueItems.SILICON.get());
        basicMaterialItem(TorqueItems.TUNGSTEN_INGOT.get());
        basicMaterialItem(TorqueItems.HSLA_INGOT.get());
        basicMaterialItem(TorqueItems.CAST_IRON_INGOT.get());
        basicMaterialItem(TorqueItems.IRON_DUST.get());
        basicMaterialItem(TorqueItems.COPPER_DUST.get());
        basicMaterialItem(TorqueItems.GOLD_DUST.get());
        basicMaterialItem(TorqueItems.DIAMOND_DUST.get());
        basicMaterialItem(TorqueItems.EMERALD_DUST.get());
        basicMaterialItem(TorqueItems.NETHERITE_DUST.get());
        basicMaterialItem(TorqueItems.OBSIDIAN_DUST.get());

        //Block Materials
        generateCubeItem(TorqueItems.TUNGSTEN_BLOCK_ITEM.get(), TorqueBlocks.TUNGSTEN_BLOCK.get());
        generateCubeItem(TorqueItems.HSLA_BLOCK_ITEM.get(), TorqueBlocks.HSLA_BLOCK.get());
        generateCubeItem(TorqueItems.CAST_IRON_BLOCK_ITEM.get(), TorqueBlocks.CAST_IRON_BLOCK.get());

        //Components
        basicComponentItem(TorqueItems.HSLA_CASING.get());
        basicComponentItem(TorqueItems.CIRCUIT_MODULE.get());
        basicComponentItem(TorqueItems.HSLA_ROD.get());
        basicComponentItem(TorqueItems.HSLA_STEEL_SHAFT.get());
        basicComponentItem(TorqueItems.HSLA_PISTON.get());
        basicComponentItem(TorqueItems.HSLA_TANK.get());
        basicComponentItem(TorqueItems.HSLA_PLATE.get());
        basicComponentItem(TorqueItems.SHARP_HSLA_GEAR.get());
        basicComponentItem(TorqueItems.HSLA_GEAR.get());
        basicComponentItem(TorqueItems.HSLA_GEARS_2.get());
        basicComponentItem(TorqueItems.HSLA_GEARS_4.get());
        basicComponentItem(TorqueItems.HSLA_GEARS_8.get());
        basicComponentItem(TorqueItems.HSLA_GEARS_16.get());

        withExistingParent(TorqueItems.ROTARY_MONITOR.get(), "torquecraft:block/rotary_monitor/block");
        basicItem(TorqueItems.CANOLA_SEEDS.get());
        basicItem(TorqueItems.CRUSHED_SEEDS.get());
        basicItem(TorqueItems.JET_FUEL_BUCKET.get());
        basicItem(TorqueItems.LUBRICANT_BUCKET.get());
        basicItem(TorqueItems.PRESSURE_GAUGE.get());
        basicItem(TorqueItems.ROTARY_WRENCH.get());

        withExistingParent(TorqueItems.FLUID_TANK_ITEM.get(), "torquecraft:block/fluid_tank/block");
        withExistingParent(TorqueItems.PUMP_ITEM.get(), TorqueBlocks.PUMP.get());
        withExistingParent(TorqueItems.STEAM_ENGINE_ITEM.get(), TorqueBlocks.STEAM_ENGINE.get());
        withExistingParent(TorqueItems.BEVEL_GEARS_ITEM.get(), TorqueBlocks.BEVEL_GEARS.get());
        withExistingParent(TorqueItems.THREE_WAY_ITEM.get(), TorqueBlocks.THREE_WAY.get());
        withExistingParent(TorqueItems.SPRINKLER_ITEM.get(), TorqueBlocks.SPRINKLER.get());
        withExistingParent(TorqueItems.VACUUM_ITEM.get(), TorqueBlocks.VACUUM.get());
        withExistingParent(TorqueItems.MECHANICAL_FAN_ITEM.get(), TorqueBlocks.MECHANICAL_FAN.get());
        withExistingParent(TorqueItems.GRINDER_ITEM.get(), TorqueBlocks.GRINDER.get());
        withExistingParent(TorqueItems.SHAFT_ITEM.get(), TorqueBlocks.SHAFT.get());

        withExistingParent(TorqueItems.FLUID_PIPE_ITEM.get(), "torquecraft:block/pipe");
        withExistingParent(TorqueItems.STEAM_PIPE_ITEM.get(), "torquecraft:block/pipe");

        withExistingParent(TorqueItems.ALLOY_FURNACE_ITEM.get(), "furnace/", TorqueBlocks.ALLOY_FURNACE.get());
        generateGearboxItem(TorqueItems.GEARBOX1_2_ITEM.get(), "1_2");
        generateGearboxItem(TorqueItems.GEARBOX1_4_ITEM.get(), "1_4");
        withExistingParent(TorqueItems.COOLING_RADIATOR_ITEM.get(), "radiator/", TorqueBlocks.COOLING_RADIATOR.get(), "_full");
    }
}
