package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.*;
import dev.shinyepo.torquecraft.block.entities.pipes.FluidPipeEntity;
import dev.shinyepo.torquecraft.block.entities.pipes.SteamPipeEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TorqueCraft.MODID);

    public static final Supplier<BlockEntityType<SteamEngineEntity>> STEAM_ENGINE_ENTITY = BLOCK_ENTITIES.register("steam_engine_be", () -> BlockEntityType.Builder.of(SteamEngineEntity::new, TorqueBlocks.STEAM_ENGINE.get()).build(null));
    public static final Supplier<BlockEntityType<MechanicalFanEntity>> MECHANICAL_FAN_ENTITY = BLOCK_ENTITIES.register("mechanical_fan_be", () -> BlockEntityType.Builder.of(MechanicalFanEntity::new, TorqueBlocks.MECHANICAL_FAN.get()).build(null));
    public static final Supplier<BlockEntityType<GrinderEntity>> GRINDER_ENTITY = BLOCK_ENTITIES.register("grinder_be", () -> BlockEntityType.Builder.of(GrinderEntity::new, TorqueBlocks.GRINDER.get()).build(null));
    public static final Supplier<BlockEntityType<PumpEntity>> PUMP_ENTITY = BLOCK_ENTITIES.register("pump_be", () -> BlockEntityType.Builder.of(PumpEntity::new, TorqueBlocks.PUMP.get()).build(null));
    public static final Supplier<BlockEntityType<FluidPipeEntity>> FLUID_PIPE_ENTITY = BLOCK_ENTITIES.register("fluid_pipe_be", () -> BlockEntityType.Builder.of(FluidPipeEntity::new, TorqueBlocks.FLUID_PIPE.get()).build(null));
    public static final Supplier<BlockEntityType<SteamPipeEntity>> STEAM_PIPE_ENTITY = BLOCK_ENTITIES.register("steam_pipe_be", () -> BlockEntityType.Builder.of(SteamPipeEntity::new, TorqueBlocks.STEAM_PIPE.get()).build(null));
    public static final Supplier<BlockEntityType<FluidTankEntity>> FLUID_TANK_ENTITY = BLOCK_ENTITIES.register("fluid_tank_be", () -> BlockEntityType.Builder.of(FluidTankEntity::new, TorqueBlocks.FLUID_TANK.get()).build(null));
    public static final Supplier<BlockEntityType<ShaftEntity>> SHAFT_ENTITY = BLOCK_ENTITIES.register("shaft_be", () -> BlockEntityType.Builder.of(ShaftEntity::new, TorqueBlocks.SHAFT.get()).build(null));

}
