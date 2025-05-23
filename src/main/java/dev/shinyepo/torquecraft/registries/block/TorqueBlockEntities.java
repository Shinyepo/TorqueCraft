package dev.shinyepo.torquecraft.registries.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.AlloyFurnaceEntity;
import dev.shinyepo.torquecraft.block.entities.FluidTankEntity;
import dev.shinyepo.torquecraft.block.entities.pipes.SteamPipeEntity;
import dev.shinyepo.torquecraft.block.entities.rotary.*;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.*;
import dev.shinyepo.torquecraft.factory.pipe.network.PressureFluidTransmitter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TorqueCraft.MODID);

    public static final Supplier<BlockEntityType<AlloyFurnaceEntity>> ALLOY_FURNACE_ENTITY = BLOCK_ENTITIES.register("alloy_furnace_be", () -> BlockEntityType.Builder.of(AlloyFurnaceEntity::new, TorqueBlocks.ALLOY_FURNACE.get()).build(null));
    public static final Supplier<BlockEntityType<SteamEngineEntity>> STEAM_ENGINE_ENTITY = BLOCK_ENTITIES.register("steam_engine_be", () -> BlockEntityType.Builder.of(SteamEngineEntity::new, TorqueBlocks.STEAM_ENGINE.get()).build(null));
    public static final Supplier<BlockEntityType<MechanicalFanEntity>> MECHANICAL_FAN_ENTITY = BLOCK_ENTITIES.register("mechanical_fan_be", () -> BlockEntityType.Builder.of(MechanicalFanEntity::new, TorqueBlocks.MECHANICAL_FAN.get()).build(null));
    public static final Supplier<BlockEntityType<GrinderEntity>> GRINDER_ENTITY = BLOCK_ENTITIES.register("grinder_be", () -> BlockEntityType.Builder.of(GrinderEntity::new, TorqueBlocks.GRINDER.get()).build(null));
    public static final Supplier<BlockEntityType<PumpEntity>> PUMP_ENTITY = BLOCK_ENTITIES.register("pump_be", () -> BlockEntityType.Builder.of(PumpEntity::new, TorqueBlocks.PUMP.get()).build(null));
    public static final Supplier<BlockEntityType<SprinklerEntity>> SPRINKLER_ENTITY = BLOCK_ENTITIES.register("sprinkler_be", () -> BlockEntityType.Builder.of(SprinklerEntity::new, TorqueBlocks.SPRINKLER.get()).build(null));
    public static final Supplier<BlockEntityType<PressureFluidTransmitter>> FLUID_PIPE_ENTITY = BLOCK_ENTITIES.register("fluid_pipe_be", () -> BlockEntityType.Builder.of(PressureFluidTransmitter::new, TorqueBlocks.FLUID_PIPE.get()).build(null));
    public static final Supplier<BlockEntityType<SteamPipeEntity>> STEAM_PIPE_ENTITY = BLOCK_ENTITIES.register("steam_pipe_be", () -> BlockEntityType.Builder.of(SteamPipeEntity::new, TorqueBlocks.STEAM_PIPE.get()).build(null));
    public static final Supplier<BlockEntityType<FluidTankEntity>> FLUID_TANK_ENTITY = BLOCK_ENTITIES.register("fluid_tank_be", () -> BlockEntityType.Builder.of(FluidTankEntity::new, TorqueBlocks.FLUID_TANK.get()).build(null));
    public static final Supplier<BlockEntityType<ShaftEntity>> SHAFT_ENTITY = BLOCK_ENTITIES.register("shaft_be", () -> BlockEntityType.Builder.of(ShaftEntity::new, TorqueBlocks.SHAFT.get()).build(null));
    public static final Supplier<BlockEntityType<ThreeWayEntity>> THREE_WAY_ENTITY = BLOCK_ENTITIES.register("three_way_be", () -> BlockEntityType.Builder.of(ThreeWayEntity::new, TorqueBlocks.THREE_WAY.get()).build(null));
    public static final Supplier<BlockEntityType<BevelGearsEntity>> BEVEL_GEARS_ENTITY = BLOCK_ENTITIES.register("bevel_gears_be", () -> BlockEntityType.Builder.of(BevelGearsEntity::new, TorqueBlocks.BEVEL_GEARS.get()).build(null));
    public static final Supplier<BlockEntityType<Gearbox1_2Entity>> GEARBOX1_2_ENTITY = BLOCK_ENTITIES.register("gearbox1_2_be", () -> BlockEntityType.Builder.of(Gearbox1_2Entity::new, TorqueBlocks.GEARBOX_1_2.get()).build(null));
    public static final Supplier<BlockEntityType<Gearbox1_4Entity>> GEARBOX1_4_ENTITY = BLOCK_ENTITIES.register("gearbox1_4_be", () -> BlockEntityType.Builder.of(Gearbox1_4Entity::new, TorqueBlocks.GEARBOX_1_4.get()).build(null));
    public static final Supplier<BlockEntityType<Gearbox1_8Entity>> GEARBOX1_8_ENTITY = BLOCK_ENTITIES.register("gearbox1_8_be", () -> BlockEntityType.Builder.of(Gearbox1_8Entity::new, TorqueBlocks.GEARBOX_1_8.get()).build(null));
    public static final Supplier<BlockEntityType<Gearbox1_16Entity>> GEARBOX1_16_ENTITY = BLOCK_ENTITIES.register("gearbox1_16_be", () -> BlockEntityType.Builder.of(Gearbox1_16Entity::new, TorqueBlocks.GEARBOX_1_16.get()).build(null));
    public static final Supplier<BlockEntityType<VacuumEntity>> VACUUM_ENTITY = BLOCK_ENTITIES.register("vacuum_be", () -> BlockEntityType.Builder.of(VacuumEntity::new, TorqueBlocks.VACUUM.get()).build(null));
    public static final Supplier<BlockEntityType<CentrifugeEntity>> CENTRIFUGE_ENTITY = BLOCK_ENTITIES.register("centrifuge_be", () -> BlockEntityType.Builder.of(CentrifugeEntity::new, TorqueBlocks.CENTRIFUGE.get()).build(null));

}
