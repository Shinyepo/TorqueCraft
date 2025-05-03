package dev.shinyepo.torquecraft.registries.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.AlloyFurnaceEntity;
import dev.shinyepo.torquecraft.block.entities.FluidTankEntity;
import dev.shinyepo.torquecraft.block.entities.rotary.*;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.BevelGearsEntity;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ShaftEntity;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ThreeWayEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TorqueCraft.MODID);

    public static final Supplier<BlockEntityType<AlloyFurnaceEntity>> ALLOY_FURNACE_ENTITY = BLOCK_ENTITIES.register("alloy_furnace_be", () -> new BlockEntityType<>(AlloyFurnaceEntity::new, TorqueBlocks.ALLOY_FURNACE.get()));
    public static final Supplier<BlockEntityType<SteamEngineEntity>> STEAM_ENGINE_ENTITY = BLOCK_ENTITIES.register("steam_engine_be", () -> new BlockEntityType<>(SteamEngineEntity::new, TorqueBlocks.STEAM_ENGINE.get()));
    public static final Supplier<BlockEntityType<MechanicalFanEntity>> MECHANICAL_FAN_ENTITY = BLOCK_ENTITIES.register("mechanical_fan_be", () -> new BlockEntityType<>(MechanicalFanEntity::new, TorqueBlocks.MECHANICAL_FAN.get()));
    public static final Supplier<BlockEntityType<GrinderEntity>> GRINDER_ENTITY = BLOCK_ENTITIES.register("grinder_be", () -> new BlockEntityType<>(GrinderEntity::new, TorqueBlocks.GRINDER.get()));
    public static final Supplier<BlockEntityType<PumpEntity>> PUMP_ENTITY = BLOCK_ENTITIES.register("pump_be", () -> new BlockEntityType<>(PumpEntity::new, TorqueBlocks.PUMP.get()));
    public static final Supplier<BlockEntityType<SprinklerEntity>> SPRINKLER_ENTITY = BLOCK_ENTITIES.register("sprinkler_be", () -> new BlockEntityType<>(SprinklerEntity::new, TorqueBlocks.SPRINKLER.get()));
    public static final Supplier<BlockEntityType<FluidTankEntity>> FLUID_TANK_ENTITY = BLOCK_ENTITIES.register("fluid_tank_be", () -> new BlockEntityType<>(FluidTankEntity::new, TorqueBlocks.FLUID_TANK.get()));
    public static final Supplier<BlockEntityType<ShaftEntity>> SHAFT_ENTITY = BLOCK_ENTITIES.register("shaft_be", () -> new BlockEntityType<>(ShaftEntity::new, TorqueBlocks.HSLA_SHAFT.get()));
    public static final Supplier<BlockEntityType<ThreeWayEntity>> THREE_WAY_ENTITY = BLOCK_ENTITIES.register("three_way_be", () -> new BlockEntityType<>(ThreeWayEntity::new, TorqueBlocks.HSLA_THREE_WAY.get()));
    public static final Supplier<BlockEntityType<BevelGearsEntity>> BEVEL_GEARS_ENTITY = BLOCK_ENTITIES.register("bevel_gears_be", () -> new BlockEntityType<>(BevelGearsEntity::new, TorqueBlocks.HSLA_BEVEL_GEARS.get()));
    public static final Supplier<BlockEntityType<GearboxEntity>> GEARBOX_ENTITY = BLOCK_ENTITIES.register("gearbox_be", () -> new BlockEntityType<>(GearboxEntity::new, TorqueBlocks.HSLA_GEARBOX1_2.get(), TorqueBlocks.HSLA_GEARBOX1_4.get()));
    public static final Supplier<BlockEntityType<VacuumEntity>> VACUUM_ENTITY = BLOCK_ENTITIES.register("vacuum_be", () -> new BlockEntityType<>(VacuumEntity::new, TorqueBlocks.VACUUM.get()));

}
