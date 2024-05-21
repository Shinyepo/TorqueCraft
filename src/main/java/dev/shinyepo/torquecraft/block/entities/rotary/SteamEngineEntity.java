package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SteamEngineEntity extends RotarySource {
    public SteamEngineEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), pPos, pBlockState, SourceConfig.STEAM_ENGINE);
    }
}
