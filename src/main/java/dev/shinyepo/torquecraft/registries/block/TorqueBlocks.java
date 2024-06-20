package dev.shinyepo.torquecraft.registries.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.prefab.AlloyFurnace;
import dev.shinyepo.torquecraft.block.prefab.CanolaCrop;
import dev.shinyepo.torquecraft.block.prefab.CoolingRadiator;
import dev.shinyepo.torquecraft.block.prefab.FluidTank;
import dev.shinyepo.torquecraft.block.prefab.pipes.FluidPipe;
import dev.shinyepo.torquecraft.block.prefab.pipes.SteamPipe;
import dev.shinyepo.torquecraft.block.prefab.rotary.*;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.BevelGears;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.Gearbox;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.Shaft;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.ThreeWay;
import dev.shinyepo.torquecraft.config.GearboxRatio;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TorqueCraft.MODID);

    public static final Supplier<Block> CAST_IRON_BLOCK = BLOCKS.registerSimpleBlock("cast_iron_block", BlockBehaviour.Properties.of().destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE));
    public static final Supplier<Block> HSLA_BLOCK = BLOCKS.registerSimpleBlock("hsla_block", BlockBehaviour.Properties.of().destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE));
    public static final Supplier<Block> TUNGSTEN_BLOCK = BLOCKS.registerSimpleBlock("tungsten_block", BlockBehaviour.Properties.of().destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE));
    public static final Supplier<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", () -> new AlloyFurnace(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));

    //Gears/Shafts
    public static final Supplier<Block> HSLA_SHAFT = BLOCKS.register("hsla_shaft", () -> new Shaft(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> HSLA_THREE_WAY = BLOCKS.register("hsla_three_way", () -> new ThreeWay(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> HSLA_BEVEL_GEARS = BLOCKS.register("hsla_bevel_gears", () -> new BevelGears(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> HSLA_GEARBOX1_2 = BLOCKS.register("hsla_gearbox1_2", () -> new Gearbox(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE), GearboxRatio.RATIO_2));
    public static final Supplier<Block> HSLA_GEARBOX1_4 = BLOCKS.register("hsla_gearbox1_4", () -> new Gearbox(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE), GearboxRatio.RATIO_4));

    //Machines
    public static final Supplier<Block> STEAM_ENGINE = BLOCKS.register("steam_engine",() -> new SteamEngine(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> MECHANICAL_FAN = BLOCKS.register("mechanical_fan",() -> new MechanicalFan(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> GRINDER = BLOCKS.register("grinder",() -> new Grinder(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> PUMP = BLOCKS.register("pump",() -> new Pump(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> SPRINKLER = BLOCKS.register("sprinkler", () -> new Sprinkler(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> COOLING_RADIATOR = BLOCKS.register("cooling_radiator",() -> new CoolingRadiator(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));

    //Crops
    public static final Supplier<Block> CANOLA_CROP = BLOCKS.register("canola_crop", () -> new CanolaCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion().noCollission()));

    //FLUIDS
    public static final Supplier<LiquidBlock> LUBRICANT_BLOCK = BLOCKS.register("lubricant_block", () -> new LiquidBlock(TorqueFluids.SOURCE_LUBRICANT.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final Supplier<LiquidBlock> JET_FUEL_BLOCK = BLOCKS.register("jet_fuel_block", () -> new LiquidBlock(TorqueFluids.SOURCE_JET_FUEL.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));


    public static final Supplier<Block> FLUID_TANK = BLOCKS.register("fluid_tank", () -> new FluidTank(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().mapColor(MapColor.TERRACOTTA_WHITE)));
    //PIPES
    public static final Supplier<Block> FLUID_PIPE = BLOCKS.register("fluid_pipe", FluidPipe::new);
    public static final Supplier<Block> STEAM_PIPE = BLOCKS.register("steam_pipe", SteamPipe::new);
}
