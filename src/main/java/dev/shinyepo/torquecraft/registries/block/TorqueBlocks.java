package dev.shinyepo.torquecraft.registries.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.prefab.AlloyFurnace;
import dev.shinyepo.torquecraft.block.prefab.CanolaCrop;
import dev.shinyepo.torquecraft.block.prefab.CoolingRadiator;
import dev.shinyepo.torquecraft.block.prefab.FluidTank;
import dev.shinyepo.torquecraft.block.prefab.rotary.*;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.BevelGears;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.Gearbox;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.Shaft;
import dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.ThreeWay;
import dev.shinyepo.torquecraft.config.GearboxRatio;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TorqueCraft.MODID);

    public static final Supplier<Block> CAST_IRON_BLOCK = BLOCKS.register("cast_iron_block",(r) -> new Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> HSLA_BLOCK = BLOCKS.register("hsla_block", (r) -> new Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> TUNGSTEN_BLOCK = BLOCKS.register("tungsten_block", (r) -> new Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", (r) -> new AlloyFurnace(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));

    //Gears/Shafts
    public static final Supplier<Block> HSLA_SHAFT = BLOCKS.register("hsla_shaft", (r) -> new Shaft(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> HSLA_THREE_WAY = BLOCKS.register("hsla_three_way", (r) -> new ThreeWay(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> HSLA_BEVEL_GEARS = BLOCKS.register("hsla_bevel_gears", (r) -> new BevelGears(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> HSLA_GEARBOX1_2 = BLOCKS.register("hsla_gearbox1_2", (r) -> new Gearbox(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE), GearboxRatio.RATIO_2));
    public static final Supplier<Block> HSLA_GEARBOX1_4 = BLOCKS.register("hsla_gearbox1_4", (r) -> new Gearbox(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE), GearboxRatio.RATIO_4));

    //Machines
    public static final Supplier<Block> STEAM_ENGINE = BLOCKS.register("steam_engine",(r) -> new SteamEngine(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> MECHANICAL_FAN = BLOCKS.register("mechanical_fan",(r) -> new MechanicalFan(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> GRINDER = BLOCKS.register("grinder",(r) -> new Grinder(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> PUMP = BLOCKS.register("pump",(r) -> new Pump(BlockBehaviour.Properties.of().destroyTime(1.5f).setId(ResourceKey.create(Registries.BLOCK,r)).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> SPRINKLER = BLOCKS.register("sprinkler", (r) -> new Sprinkler(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> COOLING_RADIATOR = BLOCKS.register("cooling_radiator",(r) -> new CoolingRadiator(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));
    public static final Supplier<Block> VACUUM = BLOCKS.register("vacuum", (r) -> new Vacuum(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));

    //Crops
    public static final Supplier<Block> CANOLA_CROP = BLOCKS.register("canola_crop", (r) -> new CanolaCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).setId(ResourceKey.create(Registries.BLOCK,r)).noOcclusion().noCollission()));
//    public static final Supplier<Block> FLUID_TANK = BLOCKS.register("fluid_tank", (r) -> new Vacuum(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion().mapColor(MapColor.STONE)));

    //FLUIDS
    public static final Supplier<LiquidBlock> LUBRICANT_BLOCK = BLOCKS.register("lubricant_block", (r) -> new LiquidBlock(TorqueFluids.SOURCE_LUBRICANT.get(), BlockBehaviour.Properties.of().replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY).setId(ResourceKey.create(Registries.BLOCK,r))));
    public static final Supplier<LiquidBlock> JET_FUEL_BLOCK = BLOCKS.register("jet_fuel_block", (r) -> new LiquidBlock(TorqueFluids.SOURCE_JET_FUEL.get(), BlockBehaviour.Properties.of().replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY).setId(ResourceKey.create(Registries.BLOCK,r))));


//    public static final Supplier<Block> FLUID_TANK = BLOCKS.register("fluid_tank", (r) -> new FluidTank(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,r)).destroyTime(1.5f).noOcclusion()));
    //PIPES
//    public static final Supplier<Block> FLUID_PIPE = BLOCKS.register("fluid_pipe", FluidPipe::new);
//    public static final Supplier<Block> STEAM_PIPE = BLOCKS.register("steam_pipe", SteamPipe::new);
}
