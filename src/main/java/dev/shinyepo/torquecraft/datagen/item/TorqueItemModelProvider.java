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
        withExistingParent(TorqueItems.TUNGSTEN_BLOCK_ITEM.get(), TorqueBlocks.TUNGSTEN_BLOCK.get());
        withExistingParent(TorqueItems.HSLA_BLOCK_ITEM.get(), TorqueBlocks.HSLA_BLOCK.get());
        withExistingParent(TorqueItems.CAST_IRON_BLOCK_ITEM.get(), TorqueBlocks.CAST_IRON_BLOCK.get());

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

        withExistingParent(TorqueItems.ROTARY_MONITOR.get(), "torquecraft:block/rotary_monitor");
        basicItem(TorqueItems.CANOLA_SEEDS.get());
        basicItem(TorqueItems.CRUSHED_SEEDS.get());
        basicItem(TorqueItems.JET_FUEL_BUCKET.get());
        basicItem(TorqueItems.LUBRICANT_BUCKET.get());
        basicItem(TorqueItems.PRESSURE_GAUGE.get());
        basicItem(TorqueItems.ROTARY_WRENCH.get());

        withExistingParent(TorqueItems.FLUID_TANK_ITEM.get(), TorqueBlocks.FLUID_TANK.get());
        withExistingParent(TorqueItems.PUMP_ITEM.get(), TorqueBlocks.PUMP.get());
        withExistingParent(TorqueItems.STEAM_ENGINE_ITEM.get(), TorqueBlocks.STEAM_ENGINE.get());
        withExistingParent(TorqueItems.HSLA_BEVEL_GEARS_ITEM.get(), TorqueBlocks.HSLA_BEVEL_GEARS.get());
        withExistingParent(TorqueItems.HSLA_THREE_WAY_ITEM.get(), TorqueBlocks.HSLA_THREE_WAY.get());
        withExistingParent(TorqueItems.SPRINKLER_ITEM.get(), TorqueBlocks.SPRINKLER.get());
        withExistingParent(TorqueItems.VACUUM_ITEM.get(), TorqueBlocks.VACUUM.get());

        withExistingParent(TorqueItems.FLUID_PIPE_ITEM.get(), "torquecraft:block/pipe");
        withExistingParent(TorqueItems.STEAM_PIPE_ITEM.get(), "torquecraft:block/pipe");

        withExistingParent(TorqueItems.ALLOY_FURNACE_ITEM.get(), "furnace/", TorqueBlocks.ALLOY_FURNACE.get());
        withExistingParent(TorqueItems.MECHANICAL_FAN_ITEM.get(), "item/", TorqueBlocks.MECHANICAL_FAN.get());
        withExistingParent(TorqueItems.GRINDER_ITEM.get(), "item/", TorqueBlocks.GRINDER.get());
        withExistingParent(TorqueItems.HSLA_SHAFT_ITEM.get(), "item/", TorqueBlocks.HSLA_SHAFT.get());
        withExistingParent(TorqueItems.HSLA_GEARBOX1_2_ITEM.get(), "gearbox/", TorqueBlocks.HSLA_GEARBOX1_2.get());
        withExistingParent(TorqueItems.HSLA_GEARBOX1_4_ITEM.get(), "gearbox/", TorqueBlocks.HSLA_GEARBOX1_4.get());
        withExistingParent(TorqueItems.COOLING_RADIATOR_ITEM.get(), "radiator/", TorqueBlocks.COOLING_RADIATOR.get(), "_full");
    }
}
