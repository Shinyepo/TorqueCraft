package dev.shinyepo.torquecraft.registries.item;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.item.prefab.CanolaMeal;
import dev.shinyepo.torquecraft.item.prefab.PressureGauge;
import dev.shinyepo.torquecraft.item.prefab.RotaryMonitor;
import dev.shinyepo.torquecraft.item.prefab.RotaryWrench;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueItems {
    //Main registry for items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TorqueCraft.MODID);

    //Materials
    public static final Supplier<Item> TUNGSTEN_INGOT = ITEMS.registerSimpleItem("tungsten_ingot", new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(10).build()));
    public static final Supplier<Item> QUARTZ_DUST = ITEMS.registerSimpleItem("quartz_dust");
    public static final Supplier<Item> SILICON = ITEMS.registerSimpleItem("silicon");
    public static final Supplier<Item> CAST_IRON_INGOT = ITEMS.registerSimpleItem("cast_iron_ingot");
    public static final Supplier<Item> HSLA_INGOT = ITEMS.registerSimpleItem("hsla_ingot");
    public static final Supplier<Item> COPPER_DUST = ITEMS.registerSimpleItem("copper_dust");
    public static final Supplier<Item> IRON_DUST = ITEMS.registerSimpleItem("iron_dust");
    public static final Supplier<Item> GOLD_DUST = ITEMS.registerSimpleItem("gold_dust");
    public static final Supplier<Item> DIAMOND_DUST = ITEMS.registerSimpleItem("diamond_dust");
    public static final Supplier<Item> EMERALD_DUST = ITEMS.registerSimpleItem("emerald_dust");
    public static final Supplier<Item> NETHERITE_DUST = ITEMS.registerSimpleItem("netherite_dust");
    public static final Supplier<Item> OBSIDIAN_DUST = ITEMS.registerSimpleItem("obsidian_dust", new Item.Properties());

    //Components
    public static final Supplier<Item> HSLA_ROD = ITEMS.registerSimpleItem("hsla_rod", new Item.Properties());
    public static final Supplier<Item> HSLA_STEEL_SHAFT = ITEMS.registerSimpleItem("hsla_steel_shaft", new Item.Properties());
    public static final Supplier<Item> HSLA_GEAR = ITEMS.registerSimpleItem("hsla_gear", new Item.Properties());
    public static final Supplier<Item> SHARP_HSLA_GEAR = ITEMS.registerSimpleItem("sharp_hsla_gear", new Item.Properties());
    public static final Supplier<Item> HSLA_GEARS_2 = ITEMS.registerSimpleItem("hsla_gears_2", new Item.Properties());
    public static final Supplier<Item> HSLA_GEARS_4 = ITEMS.registerSimpleItem("hsla_gears_4", new Item.Properties());
    public static final Supplier<Item> HSLA_GEARS_8 = ITEMS.registerSimpleItem("hsla_gears_8", new Item.Properties());
    public static final Supplier<Item> HSLA_GEARS_16 = ITEMS.registerSimpleItem("hsla_gears_16", new Item.Properties());
    public static final Supplier<Item> HSLA_CASING = ITEMS.registerSimpleItem("hsla_casing", new Item.Properties());
    public static final Supplier<Item> HSLA_PISTON = ITEMS.registerSimpleItem("hsla_piston", new Item.Properties());
    public static final Supplier<Item> HSLA_TANK = ITEMS.registerSimpleItem("hsla_tank", new Item.Properties());
    public static final Supplier<Item> HSLA_PLATE = ITEMS.registerSimpleItem("hsla_plate", new Item.Properties());
    public static final Supplier<Item> CIRCUIT_MODULE = ITEMS.registerSimpleItem("circuit_module", new Item.Properties());

    //Gears/Shafts
    public static final Supplier<BlockItem> HSLA_SHAFT_ITEM = ITEMS.registerSimpleBlockItem("hsla_shaft", TorqueBlocks.HSLA_SHAFT);
    public static final Supplier<BlockItem> HSLA_THREE_WAY_ITEM = ITEMS.registerSimpleBlockItem("hsla_three_way", TorqueBlocks.HSLA_THREE_WAY);
    public static final Supplier<BlockItem> HSLA_BEVEL_GEARS_ITEM = ITEMS.registerSimpleBlockItem("hsla_bevel_gears", TorqueBlocks.HSLA_BEVEL_GEARS);
    public static final Supplier<BlockItem> HSLA_GEARBOX1_2_ITEM = ITEMS.registerSimpleBlockItem("hsla_gearbox1_2", TorqueBlocks.HSLA_GEARBOX1_2);
    public static final Supplier<BlockItem> HSLA_GEARBOX1_4_ITEM = ITEMS.registerSimpleBlockItem("hsla_gearbox1_4", TorqueBlocks.HSLA_GEARBOX1_4);

    //Items
    public static final Supplier<Item> CRUSHED_SEEDS = ITEMS.register("crushed_seeds", (r) -> new CanolaMeal(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,r))));

    //TOOLS
    public static final Supplier<Item> PRESSURE_GAUGE = ITEMS.register("pressure_gauge", (r) -> new PressureGauge(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,r))));
    public static final Supplier<Item> ROTARY_MONITOR = ITEMS.register("rotary_monitor", (r) -> new RotaryMonitor(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,r))));
    public static final Supplier<Item> ROTARY_WRENCH = ITEMS.register("rotary_wrench", (r) -> new RotaryWrench(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,r))));

    //Block items
    public static final Supplier<BlockItem> TUNGSTEN_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("tungsten_block", TorqueBlocks.TUNGSTEN_BLOCK);
    public static final Supplier<BlockItem> CAST_IRON_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("cast_iron_block", TorqueBlocks.CAST_IRON_BLOCK);
    public static final Supplier<BlockItem> HSLA_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("hsla_block", TorqueBlocks.HSLA_BLOCK);
    public static final Supplier<BlockItem> ALLOY_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("alloy_furnace", TorqueBlocks.ALLOY_FURNACE);

    //machine block items
    public static final Supplier<BlockItem> STEAM_ENGINE_ITEM = ITEMS.registerSimpleBlockItem("steam_engine", TorqueBlocks.STEAM_ENGINE);
    public static final Supplier<BlockItem> MECHANICAL_FAN_ITEM = ITEMS.registerSimpleBlockItem("mechanical_fan", TorqueBlocks.MECHANICAL_FAN);
    public static final Supplier<BlockItem> GRINDER_ITEM = ITEMS.registerSimpleBlockItem("grinder", TorqueBlocks.GRINDER);
    public static final Supplier<BlockItem> PUMP_ITEM = ITEMS.registerSimpleBlockItem("pump", TorqueBlocks.PUMP);
    public static final Supplier<BlockItem> SPRINKLER_ITEM = ITEMS.registerSimpleBlockItem("sprinkler", TorqueBlocks.SPRINKLER);
    public static final Supplier<BlockItem> VACUUM_ITEM = ITEMS.registerSimpleBlockItem("vacuum", TorqueBlocks.VACUUM);

    public static final Supplier<BlockItem> COOLING_RADIATOR_ITEM = ITEMS.registerSimpleBlockItem("cooling_radiator", TorqueBlocks.COOLING_RADIATOR);
    public static final Supplier<BlockItem> FLUID_TANK_ITEM = ITEMS.registerSimpleBlockItem("fluid_tank", TorqueBlocks.FLUID_TANK);

    //PIPES
//    public static final Supplier<BlockItem> FLUID_PIPE_ITEM = ITEMS.registerSimpleBlockItem("fluid_pipe", TorqueBlocks.FLUID_PIPE);
//    public static final Supplier<BlockItem> STEAM_PIPE_ITEM = ITEMS.registerSimpleBlockItem("steam_pipe", TorqueBlocks.STEAM_PIPE);


    //Seeds
    public static final Supplier<BlockItem> CANOLA_SEEDS = ITEMS.registerSimpleBlockItem("canola_seeds", TorqueBlocks.CANOLA_CROP);

    //fluid buckets
    public static final Supplier<BucketItem> LUBRICANT_BUCKET = ITEMS.register("lubricant_bucket", (r) -> new BucketItem(TorqueFluids.SOURCE_LUBRICANT.get(),
            new Item.Properties().setId(ResourceKey.create(Registries.ITEM,r)).craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final Supplier<BucketItem> JET_FUEL_BUCKET = ITEMS.register("jet_fuel_bucket", (r) -> new BucketItem(TorqueFluids.SOURCE_JET_FUEL.get(),
            new Item.Properties().setId(ResourceKey.create(Registries.ITEM,r)).craftRemainder(Items.BUCKET).stacksTo(1)));


}
