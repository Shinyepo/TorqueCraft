package dev.shinyepo.torquecraft.registries.item;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.item.prefab.CanolaMeal;
import dev.shinyepo.torquecraft.item.prefab.PressureGauge;
import dev.shinyepo.torquecraft.item.prefab.RotaryWrench;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueItems {
    //Main registry for items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TorqueCraft.MODID);

    //Items
    public static final Supplier<Item> TUNGSTEN_INGOT = ITEMS.registerSimpleItem("tungsten_ingot", new Item.Properties().food(new FoodProperties.Builder().fast().alwaysEdible().nutrition(10).build()));
    public static final Supplier<Item> CRUSHED_SEEDS = ITEMS.register("crushed_seeds", () -> new CanolaMeal(new Item.Properties()));

    //TOOLS
    public static final Supplier<Item> PRESSURE_GAUGE = ITEMS.register("pressure_gauge", () -> new PressureGauge(new Item.Properties()));
    public static final Supplier<Item> ROTARY_WRENCH = ITEMS.register("rotary_wrench", () -> new RotaryWrench(new Item.Properties()));

    //Block items
    public static final Supplier<BlockItem> TUNGSTEN_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("tungsten_block", TorqueBlocks.TUNGSTEN_BLOCK);

    //machine block items
    public static final Supplier<BlockItem> STEAM_ENGINE_ITEM = ITEMS.registerSimpleBlockItem("steam_engine", TorqueBlocks.STEAM_ENGINE);
    public static final Supplier<BlockItem> MECHANICAL_FAN_ITEM = ITEMS.registerSimpleBlockItem("mechanical_fan", TorqueBlocks.MECHANICAL_FAN);
    public static final Supplier<BlockItem> GRINDER_ITEM = ITEMS.registerSimpleBlockItem("grinder", TorqueBlocks.GRINDER);
    public static final Supplier<BlockItem> PUMP_ITEM = ITEMS.registerSimpleBlockItem("pump", TorqueBlocks.PUMP);

    public static final Supplier<BlockItem> SHAFT_ITEM = ITEMS.registerSimpleBlockItem("shaft", TorqueBlocks.SHAFT);
    public static final Supplier<BlockItem> THREE_WAY_ITEM = ITEMS.registerSimpleBlockItem("three_way", TorqueBlocks.THREE_WAY);
    public static final Supplier<BlockItem> BEVEL_GEARS_ITEM = ITEMS.registerSimpleBlockItem("bevel_gears", TorqueBlocks.BEVEL_GEARS);
    public static final Supplier<BlockItem> COOLING_RADIATOR_ITEM = ITEMS.registerSimpleBlockItem("cooling_radiator", TorqueBlocks.COOLING_RADIATOR);

    //PIPES
    public static final Supplier<BlockItem> FLUID_PIPE_ITEM = ITEMS.registerSimpleBlockItem("fluid_pipe", TorqueBlocks.FLUID_PIPE);
    public static final Supplier<BlockItem> STEAM_PIPE_ITEM = ITEMS.registerSimpleBlockItem("steam_pipe", TorqueBlocks.STEAM_PIPE);

    public static final Supplier<BlockItem> FLUID_TANK_ITEM = ITEMS.registerSimpleBlockItem("fluid_tank", TorqueBlocks.FLUID_TANK);



    //Seeds
    public static final Supplier<Item> CANOLA_SEEDS = ITEMS.register("canola_seeds", () -> new ItemNameBlockItem(TorqueBlocks.CANOLA_CROP.get(), new Item.Properties()));

    //fluid buckets
    public static final Supplier<BucketItem> LUBRICANT_BUCKET = ITEMS.register("lubricant_bucket", () -> new BucketItem(TorqueFluids.SOURCE_LUBRICANT.get(),
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final Supplier<BucketItem> JET_FUEL_BUCKET = ITEMS.register("jet_fuel_bucket", () -> new BucketItem(TorqueFluids.SOURCE_JET_FUEL.get(),
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));


}
