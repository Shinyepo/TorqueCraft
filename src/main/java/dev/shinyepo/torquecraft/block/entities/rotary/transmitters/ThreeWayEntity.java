package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ThreeWayEntity extends RotaryTransmitter {
    public ThreeWayEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.THREE_WAY_ENTITY.get(), pPos, pBlockState, TransmitterConfig.THREE_WAY);
    }
}
