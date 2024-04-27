package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.GrinderEntity;
import dev.shinyepo.torquecraft.block.entities.MechanicalFanEntity;
import dev.shinyepo.torquecraft.block.entities.SteamEngineEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TorqueCraft.MODID);

    public static final Supplier<BlockEntityType<SteamEngineEntity>> STEAM_ENGINE_ENTITY = BLOCK_ENTITIES.register("steam_engine_be", () -> BlockEntityType.Builder.of(SteamEngineEntity::new, TorqueBlocks.STEAM_ENGINE.get()).build(null));
    public static final Supplier<BlockEntityType<MechanicalFanEntity>> MECHANICAL_FAN_ENTITY = BLOCK_ENTITIES.register("mechanical_fan_be", () -> BlockEntityType.Builder.of(MechanicalFanEntity::new, TorqueBlocks.MECHANICAL_FAN.get()).build(null));
    public static final Supplier<BlockEntityType<GrinderEntity>> GRINDER_ENTITY = BLOCK_ENTITIES.register("grinder_be", () -> BlockEntityType.Builder.of(GrinderEntity::new, TorqueBlocks.GRINDER.get()).build(null));
}
