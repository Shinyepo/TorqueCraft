package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TorqueCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TorqueCraft.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GENERAL = CREATIVE_MODE_TABS.register("torquecraft", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup."+ TorqueCraft.MODID)) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> TorqueItems.GRINDER_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                //Materials
                output.accept(TorqueItems.QUARTZ_DUST.get());
                output.accept(TorqueItems.IRON_DUST.get());
                output.accept(TorqueItems.COPPER_DUST.get());
                output.accept(TorqueItems.GOLD_DUST.get());
                output.accept(TorqueItems.EMERALD_DUST.get());
                output.accept(TorqueItems.DIAMOND_DUST.get());
                output.accept(TorqueItems.NETHERITE_DUST.get());
                output.accept(TorqueItems.OBSIDIAN_DUST.get());
                output.accept(TorqueItems.SILICON.get());
                output.accept(TorqueItems.HSLA_INGOT.get());
                output.accept(TorqueItems.CAST_IRON_INGOT.get());
                output.accept(TorqueItems.TUNGSTEN_INGOT.get());

                //Block Materials
                output.accept(TorqueItems.HSLA_BLOCK_ITEM.get());
                output.accept(TorqueItems.CAST_IRON_BLOCK_ITEM.get());
                output.accept(TorqueItems.TUNGSTEN_BLOCK_ITEM.get());

                //Components
                output.accept(TorqueItems.HSLA_ROD.get());
                output.accept(TorqueItems.HSLA_PLATE.get());
                output.accept(TorqueItems.HSLA_STEEL_SHAFT.get());
                output.accept(TorqueItems.HSLA_TANK.get());
                output.accept(TorqueItems.HSLA_PISTON.get());
                output.accept(TorqueItems.HSLA_GEAR.get());
                output.accept(TorqueItems.SHARP_HSLA_GEAR.get());
                output.accept(TorqueItems.HSLA_GEARS_2.get());
                output.accept(TorqueItems.HSLA_GEARS_4.get());
                output.accept(TorqueItems.HSLA_GEARS_8.get());
                output.accept(TorqueItems.HSLA_GEARS_16.get());
                output.accept(TorqueItems.HSLA_CASING.get());
                output.accept(TorqueItems.CIRCUIT_MODULE.get());

                //shafts/gears
                output.accept(TorqueItems.HSLA_SHAFT_ITEM.get());
                output.accept(TorqueItems.HSLA_THREE_WAY_ITEM.get());
                output.accept(TorqueItems.HSLA_BEVEL_GEARS_ITEM.get());
                output.accept(TorqueItems.HSLA_GEARBOX1_2_ITEM.get());
                output.accept(TorqueItems.HSLA_GEARBOX1_4_ITEM.get());
                //
                output.accept(TorqueItems.CANOLA_SEEDS.get());
                output.accept(TorqueItems.CRUSHED_SEEDS.get());
                //machines
                output.accept(TorqueItems.ALLOY_FURNACE_ITEM.get());
                output.accept(TorqueItems.PUMP_ITEM.get());
                output.accept(TorqueItems.STEAM_ENGINE_ITEM.get());
                output.accept(TorqueItems.GRINDER_ITEM.get());
                output.accept(TorqueItems.MECHANICAL_FAN_ITEM.get());
                output.accept(TorqueItems.COOLING_RADIATOR_ITEM.get());
                //fluids
                output.accept(TorqueItems.FLUID_TANK_ITEM.get());
                output.accept(TorqueItems.LUBRICANT_BUCKET.get());
                output.accept(TorqueItems.JET_FUEL_BUCKET.get());
                //Pipe
                output.accept(TorqueItems.FLUID_PIPE_ITEM.get());
                output.accept(TorqueItems.STEAM_PIPE_ITEM.get());
                //Tools
                output.accept(TorqueItems.PRESSURE_GAUGE.get());
                output.accept(TorqueItems.ROTARY_WRENCH.get());
                output.accept(TorqueItems.ROTARY_MONITOR.get());
            }).build());
}
